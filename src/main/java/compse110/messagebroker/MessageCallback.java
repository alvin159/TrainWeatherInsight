package compse110.messagebroker;

public interface MessageCallback {
    void onMessageReceived(String topic, Object payload);
}
