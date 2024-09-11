package compse110.messagebroker;

// Interface for the message broker only
public interface MessageBrokerInterface {
    void publish(String topic, Object payload);
    void subscribe(String topic, MessageCallback callback);
    void unsubscribe(String topic, MessageCallback callback);
}