import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.HashMap;

public class TeleBot extends TelegramLongPollingBot {

    private static final int RECONNECT_PAUSE =  10000;

    private static HashMap<Long, ArrayList<BotFunction>> users = new HashMap<>();

    private static TeleBot bot;
    private String userName;
    private String token;


    public TeleBot(String userName, String token) {
        this.userName = userName;
        this.token = token;
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
                String messageText = update.getMessage().getText();
                long id = update.getMessage().getChatId();

                ArrayList<BotFunction> menus = users.get(id);

                if (menus == null || messageText.equalsIgnoreCase("/start")) {
                    BotFunction thisMenu = new StartMenu();
                    menus = new ArrayList<>();
                    menus.add(thisMenu);
                    users.put(id, menus);
                    thisMenu.onStart(id);
                    return;
                }

                if (messageText.equalsIgnoreCase("toStart")) {
                    if (menus.size() > 1) {
                        menus.remove(menus.size() - 1);
                        menus.get(menus.size() - 1).onStart(id);
                    } else {
                        this.sendMsg(id, "Вы в начале");
                    }
                    return;
                }
                BotFunction currentMenu = menus.get(menus.size() - 1);

                currentMenu.onUpdate(id, messageText);
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

    public static void setBot(TeleBot bot) {
        TeleBot.bot = bot;
    }

    public static TeleBot getBot() {
        return bot;
    }

    public static void addBotFunctionToUser(long id, BotFunction botFunction) {
        users.get(id).add(botFunction);
    }

}
