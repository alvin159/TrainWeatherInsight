package compse110.messagebroker;

import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;

/**
 * The MessageCallback interface should be implemented by any class that 
 * wants to use Events from the MessageBroker.
 */
public interface MessageCallback {
    void onMessageReceived(EventType event, EventPayload payload);
}
