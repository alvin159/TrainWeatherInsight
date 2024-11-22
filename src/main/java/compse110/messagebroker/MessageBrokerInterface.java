package compse110.messagebroker;

import compse110.Utils.Events.EventType;
import compse110.Utils.EventPayload;

/**
 * The MessageBroker class is a singleton that manages the publishing and subscribing of events.
 * It allows components to subscribe to specific event types and receive notifications when those events are published.
 * 
 * This class implements the MessageBrokerInterface and provides methods to publish events, 
 * subscribe to events, and unsubscribe from events. It also initializes backend services when the singleton instance is created.
 * 
 * Example usage:
 * MessageBroker broker = MessageBroker.getInstance();
 * broker.subscribe(EventType.SOME_EVENT, this);
 * broker.publish(EventType.SOME_EVENT, new EventPayload());
 * broker.unsubscribe(EventType.SOME_EVENT, this);
 */
public interface MessageBrokerInterface {

    /**
     * Publishes an event to all components that are subscribed to the event type.
     *
     * @param event   The type of event to publish.
     * @param payload The payload associated with the event.
     */
    void publish(EventType event, EventPayload payload);

    /**
     * Other components can use subscribe() if they want to receive events of that type.
     * That way message broker will direct these events to the correct components.
     *
     * @param event    the type of event to subscribe to
     * @param callback the instance of the component that wants to subscribe to the event. Usually "this"
     */
    void subscribe(EventType event, MessageCallback callback);

    /**
     * Other components can unsubscribe from the event type if they are no longer want to receive events of that type.
     * 
     * @param event The event type to unsubscribe from.
     * @param callback The callback to be removed from the subscribers list.
     * 
     */
    void unsubscribe(EventType event, MessageCallback callback);
}