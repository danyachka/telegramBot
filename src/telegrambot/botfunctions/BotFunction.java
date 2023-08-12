package telegrambot.botfunctions;

import telegrambot.User;

public interface BotFunction {

    public void onStart(User user);

    public void onUpdate(User user, String messageText);
}
