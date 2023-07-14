public interface BotFunction {

    public void onStart(Long id);

    public void onUpdate(Long id, String messageText);
}
