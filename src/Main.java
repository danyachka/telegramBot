import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.meta.generics.UpdatesReader;

public class Main {

    public static void main(String[] args) {
        new CSVReader();
        TeleBot.setBot(new TeleBot("chebureck_bot", "6308175043:AAHgo7Bjy7zRVRLRIgh6wB3L86JTOtibVTc"));
        TeleBot.getBot().botConnect();
    }

}