package telegrambot;

public class User {

    private final String nickName;

    private final long userId;

    private final long chatId;

    //private final String link;

    public User(String nickName, long chatId, long userId) {
        this.nickName = nickName;
        this.userId = chatId;
        this.chatId = userId;
        //this.link = link;
    }

    public String getNickName() {
        return nickName;
    }

    public long getChatId() {
        return chatId;
    }

    public long getUserId() {
        return userId;
    }
}
