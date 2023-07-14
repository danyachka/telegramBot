public class EnrollMenu implements BotFunction {
    @Override
    public void onStart(Long id) {
        String answer = CSVReader.getInstance().getText("enroll-start");

        TeleBot.getBot().sendMsg(id, answer);
    }

    @Override
    public void onUpdate(Long id, String messageText) {

    }
}
