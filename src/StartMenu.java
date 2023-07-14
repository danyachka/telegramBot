public class StartMenu implements BotFunction {

    public void onStart(Long id) {
        String answer = CSVReader.getInstance().getText("main-start");

        TeleBot.getBot().sendMsg(id, answer);
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
            TeleBot.getBot().sendMsg(id, answer);
        }
        if (botFunction != null) {
            botFunction.onStart(id);
            TeleBot.addBotFunctionToUser(id, botFunction);
        }
    }
}
