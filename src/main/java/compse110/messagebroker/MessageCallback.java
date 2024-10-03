package compse110.messagebroker;

import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;

public interface MessageCallback {
    void onMessageReceived(EventType event, EventPayload payload);
}
