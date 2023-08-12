package telegrambot.botfunctions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import telegrambot.CSVReader;
import telegrambot.MainBot;
import telegrambot.Meeting;
import telegrambot.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnrollMenu implements BotFunction {

    private static InlineKeyboardMarkup markup = null;

    private final String text;

    private Meeting meeting;

    public EnrollMenu(String text) {
        this.text = text;
    }

    @Override
    public void onStart(User user) {
        String[] code = text.split(" ");

        if (code.length != 0) {
            meeting = MainBot.getInstance().getMeeting(code[1]);

            if (meeting != null) {

                if (meeting.getTime() > System.currentTimeMillis()) {
                    String answer = CSVReader.getInstance().getText("enroll-start").
                            replace("%n", meeting.getName());

                    SendMessage message = new SendMessage();
                    message.setChatId(user.getChatId());
                    message.setText(answer);
                    message.setReplyMarkup(getMarkup());

                    try {
                        MainBot.getInstance().execute(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        String answer = CSVReader.getInstance().getText("enroll-error");
        MainBot.getInstance().sendMsg(user.getChatId(), answer);

        StartMenu startMenu = new StartMenu();
        MainBot.getInstance().removeLastBotFunction(user.getChatId());
    }

    @Override
    public void onUpdate(User user, String messageText) {
        String answer = null;
        boolean needReaction = false;
        if (messageText.contains("/cancel")) {
            answer = CSVReader.getInstance().getText("enroll-cancel");
            needReaction = true;
        } else if (messageText.contains("/accept")) {
            Date date = new Date(meeting.getTime());
            String day = new SimpleDateFormat("dd-MM-yyyy").format(date);
            String hours = new SimpleDateFormat("HH:mm").format(date);

            answer = CSVReader.getInstance().getText("enroll-accept").
                    replace("%time", hours).replace("%data", day).
                    replace("%description", meeting.getDescription());
            needReaction = true;
        }

        if (needReaction) {
            MainBot.getInstance().sendMsg(user.getChatId(), answer);

            StartMenu startMenu = new StartMenu();
            startMenu.onStart(user);
            MainBot.getInstance().removeLastBotFunction(user.getChatId());
        }
    }

    private InlineKeyboardMarkup getMarkup() {
        if (markup == null) {
            markup = new InlineKeyboardMarkup();
            InlineKeyboardButton acceptButton = new InlineKeyboardButton();
            acceptButton.setText(CSVReader.getInstance().getText("accept"));
            acceptButton.setCallbackData("/accept");

            InlineKeyboardButton cancelButton = new InlineKeyboardButton();
            cancelButton.setText(CSVReader.getInstance().getText("cancel"));
            cancelButton.setCallbackData("/cancel");

            List<InlineKeyboardButton> list = new ArrayList<>(2);
            list.add(acceptButton);
            list.add(cancelButton);

            List<List<InlineKeyboardButton>> rows = new ArrayList<>(1);
            rows.add(list);

            markup.setKeyboard(rows);
        }

        return markup;
    }
}
