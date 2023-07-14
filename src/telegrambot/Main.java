package telegrambot;

public class Main {

    public static void main(String[] args) {
        new CSVReader();
        new MainBot("chebureck_bot", "6308175043:AAHgo7Bjy7zRVRLRIgh6wB3L86JTOtibVTc");
        MainBot.getInstance().botConnect();
        MainBot.getInstance().loadMeetings();
        MainBot.getInstance().initSendingBotThread();
    }

}