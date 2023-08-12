package telegrambot;

import java.util.ArrayList;

public class Meeting {

    private final String code;

    private final String name;

    private final long time;

    private final String description;

    private final String link;

    private boolean isSend = false;

    private ArrayList<User> users = new ArrayList<>();

    public Meeting(String name, long time, String link, String description, String code) {
        this.code = code;
        this.name = name;
        this.time = time;
        this.link = link;
        this.description = description;
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getLink() {
        return link;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void updateUsers() {
        // Here need to update users by using GoogleSheetsAPI
    }
}
