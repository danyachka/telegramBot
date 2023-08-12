package telegrambot.botfunctions;

import telegrambot.CSVReader;
import telegrambot.MainBot;
import telegrambot.Meeting;
import telegrambot.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StartMenu implements BotFunction {

    public void onStart(User user) {
        String answer = CSVReader.getInstance().getText("main-start");

        MainBot.getInstance().sendMsg(user.getChatId(), answer);
    }

    @Override
    public void onUpdate(User user, String messageText) {
        String answer = null;
        BotFunction botFunction = null;

        if (messageText.contains("/enroll")) {
            botFunction = new EnrollMenu(messageText);
        }

        if (messageText.equalsIgnoreCase("/meetings")) {
            answer = sendMeetingsList();
        }

        if (answer != null) {
            MainBot.getInstance().sendMsg(user.getChatId(), answer);
        }
        if (botFunction != null) {
            botFunction.onStart(user);
            MainBot.getInstance().addBotFunctionToUser(user.getChatId(), botFunction);
        }
    }

    private String sendMeetingsList() {
        String answer = CSVReader.getInstance().getText("meetings-list");

        StringBuilder meetings = new StringBuilder();
        long time = System.currentTimeMillis();
        int i = 0;

        for (Meeting meeting: MainBot.getInstance().getMeetings()) {
            if (meeting.getTime() < time) continue;
            i++;
            Date date = new Date(meeting.getTime());
            String meetingTime = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);

            String meetingText = "\n" + i + ". " + meeting.getName() + " " + meetingTime;
            meetings.append(meetingText);

        }

        answer += "\n" + meetings;
        return answer;
    }
}
