package telegrambot.botfunctions;

import telegrambot.CSVReader;
import telegrambot.MainBot;

public class StartMenu implements BotFunction {

    public void onStart(Long id) {
        String answer = CSVReader.getInstance().getText("main-start");

        MainBot.getInstance().sendMsg(id, answer);
    }

    @Override
    public void onUpdate(Long id, String messageText) {
        String answer = null;
        BotFunction botFunction = null;

        if (messageText.equalsIgnoreCase("enroll")) {
            botFunction = new EnrollMenu();
        }

        if (messageText.equalsIgnoreCase("gaga")) {
            answer = CSVReader.getInstance().getText("dolbaeb");;
        }

        if (answer != null) {
            MainBot.getInstance().sendMsg(id, answer);
        }
        if (botFunction != null) {
            botFunction.onStart(id);
            MainBot.getInstance().addBotFunctionToUser(id, botFunction);
        }
    }
}
