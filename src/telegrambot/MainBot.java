package telegrambot;

import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambot.botfunctions.BotFunction;
import telegrambot.botfunctions.StartMenu;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.HashMap;

public class MainBot extends TelegramLongPollingBot {

    private static final int RECONNECT_PAUSE =  10000;

    private static final int WAIT_PAUSE =  5 * 60 * 1000;

    private static MainBot instance;
    private HashMap<Long, ArrayList<BotFunction>> users = new HashMap<>();

    private ArrayList<Meeting> meetings = new ArrayList<>();
    private final String userName;
    private final String token;


    public MainBot(String userName, String token) {
        this.userName = userName;
        this.token = token;

        instance = this;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("new update " + update.getUpdateId());

        if (update.getMessage() != null)
        {
            if (update.getMessage().hasText())
            {
                Message message = update.getMessage();
                String messageText = message.getText();

                User user = new User(message.getFrom().getUserName(), message.getChatId(), message.getFrom().getId());
                long id = user.getChatId();

                ArrayList<BotFunction> menus = users.get(id);

                if (menus == null || messageText.equalsIgnoreCase("/start")) {
                    BotFunction thisMenu = new StartMenu();
                    menus = new ArrayList<>();
                    menus.add(thisMenu);
                    users.put(id, menus);
                    thisMenu.onStart(user);
                    return;
                }

                if (messageText.equalsIgnoreCase("/back")) {
                    if (menus.size() > 1) {
                        menus.remove(menus.size() - 1);
                        menus.get(menus.size() - 1).onStart(user);
                    } else {
                        this.sendMsg(id, "Вы в начале");
                    }
                    return;
                }
                BotFunction currentMenu = menus.get(menus.size() - 1);

                currentMenu.onUpdate(user, messageText);
            }
        }
    }

    public void botConnect() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            System.out.println("TelegramAPI started. Look for messages");
        }  catch (TelegramApiException e) {
            System.out.println("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMsg(long chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void setInstance(MainBot instance) {
        MainBot.instance = instance;
    }

    public static MainBot getInstance() {
        return instance;
    }

    public void addBotFunctionToUser(long id, BotFunction botFunction) {
        users.get(id).add(botFunction);
    }

    public void removeLastBotFunction(long id) {
        ArrayList<BotFunction> list = users.get(id);
        list.remove(list.size() - 1);
    }

    public void initSendingBotThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long time = System.currentTimeMillis();

                    for (Meeting meeting: meetings) {
                        if (meeting.getTime() > time && !meeting.isSend()) {
                            meeting.updateUsers();

                            String answer = CSVReader.getInstance().getText("invite-message");
                            answer.replace("%n", meeting.getName());
                            answer.replace("%l", meeting.getLink());

                            for (User user: meeting.getUsers()) {
                                instance.sendMsg(user.getChatId(), answer);
                            }
                        }
                    }

                    try {
                        Thread.sleep(WAIT_PAUSE);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }

    public Meeting getMeeting(String code) {
        for (Meeting meeting: meetings) {
            if (meeting.getCode().equals(code)) {
                return meeting;
            }
        }
        return null;
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void loadMeetings() {
        // Here need to load existing meetings by GoogleSheetsAPI
    }

}
