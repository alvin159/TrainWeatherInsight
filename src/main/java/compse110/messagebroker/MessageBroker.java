package compse110.messagebroker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compse110.backend.TrainComponent;
import compse110.backend.SearhStationComponent.SearchStationComponent;
import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;

public class MessageBroker implements MessageBrokerInterface {
    private static MessageBroker instance;
    // Map to store subscribers for each topic
    private final Map<EventType, Set<MessageCallback>> subscribers = new HashMap<>();
    
    

    private MessageBroker() {
        // To prevent initialization
        // getInstance() should be used instead when getting messagebroker instance
    }

    /**
     * Returns the singleton instance of the MessageBroker class.
     * If the instance is null, it initializes the instance and the backend.
     *
     * @return the singleton instance of MessageBroker
     */
    public static synchronized MessageBroker getInstance() {
        if (instance == null) {
            instance = new MessageBroker();
            initializeBackend();
        }
        return instance;
    }

    /**
     * Publishes an event to all components that are subscribed to the event type.
     *
     * @param event   The type of event to publish.
     * @param payload The payload associated with the event.
     */
    @Override
    public void publish(EventType event, EventPayload payload) {
        // Check if there are any subscribers for the given event type
        if (subscribers.containsKey(event)) {
            // Notify all subscribers of the event type
            for (MessageCallback callback : subscribers.get(event)) {
            callback.onMessageReceived(event, payload);
            }
        }
    }

    /**
     * Other components can use subscribe() if they are interested of that specific event type.
     * That way message broker will direct these events to the correct components.
     *
     * @param event    the type of event to subscribe to
     * @param callback the instance of the component that wants to subscribe to the event. Usually "this"
     */
    @Override
    public void subscribe(EventType event, MessageCallback callback) {
        // Add the subscriber to the set of subscribers for the given event type
        subscribers.computeIfAbsent(event, k -> new HashSet<>()).add(callback);
    }

    /**
     * Other components can unsubscribe from the event type if they are no longer interested in that event.
     * 
     * @param event The event type to unsubscribe from.
     * @param callback The callback to be removed from the subscribers list.
     * 
     */
    @Override
    public void unsubscribe(EventType event, MessageCallback callback) {
        // Remove the subscriber from the set of subscribers for the given event type
        if (subscribers.containsKey(event)) {
            subscribers.get(event).remove(callback);
            // If no subscribers are left for the event type, remove the event type from the map
            if (subscribers.get(event).isEmpty()) {
                subscribers.remove(event);
            }
        }
    }

    private static void initializeBackend() {
        SearchStationComponent searchStationComponent = new SearchStationComponent();
        searchStationComponent.initialize();

        TrainComponent trainComponent = new TrainComponent();
        trainComponent.initialize();

        // Initialize other backend services here as well
    }
}