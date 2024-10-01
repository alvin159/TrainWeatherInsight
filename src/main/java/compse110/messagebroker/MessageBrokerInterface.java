package compse110.messagebroker;

import compse110.Entity.Events.EventType;;

// Interface for the message broker only
public interface MessageBrokerInterface {
    void publish(EventType event, Object payload);
    void subscribe(EventType event, MessageCallback callback);
    void unsubscribe(EventType event, MessageCallback callback);
}