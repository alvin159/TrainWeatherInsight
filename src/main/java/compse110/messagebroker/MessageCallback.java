package compse110.messagebroker;

import compse110.Entity.Events.EventType;

public interface MessageCallback {
    void onMessageReceived(EventType event, Object payload);
}
