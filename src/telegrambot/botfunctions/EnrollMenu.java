package telegrambot.botfunctions;

import telegrambot.CSVReader;
import telegrambot.MainBot;

public class EnrollMenu implements BotFunction {
    @Override
    public void onStart(Long id) {
        String answer = CSVReader.getInstance().getText("enroll-start");

        MainBot.getInstance().sendMsg(id, answer);
    }

    @Override
    public void onUpdate(Long id, String messageText) {

    }
}
