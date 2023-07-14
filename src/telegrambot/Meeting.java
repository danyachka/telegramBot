package telegrambot;

import java.util.ArrayList;

public class Meeting {

    private String name;

    private long time;

    private String link;

    private boolean isSend = false;

    private ArrayList<Long> users = new ArrayList<>();

    public Meeting(String name, long time, String link) {
        this.name = name;
        this.time = time;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend() {
        isSend = true;
    }

    public ArrayList<Long> getUsers() {
        return users;
    }

    public String getLink() {
        return link;
    }

    public void updateUsers() {
        // Here need to update users by using GoogleSheetsAPI
    }
}
