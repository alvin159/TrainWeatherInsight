package compse110.messagebroker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compse110.backend.DemographicComponent;
import compse110.backend.TrainComponent;
import compse110.backend.WeatherComponent;
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

    @Override
    public void subscribe(EventType event, MessageCallback callback) {
        // Add the subscriber to the set of subscribers for the given event type
        subscribers.computeIfAbsent(event, k -> new HashSet<>()).add(callback);
    }

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

    // Private function for initializing all backend components
    private static void initializeBackend() {
        SearchStationComponent searchStationComponent = new SearchStationComponent();
        searchStationComponent.initialize();

        DemographicComponent demographicComponent = new DemographicComponent();
        demographicComponent.initialize();

        TrainComponent trainComponent = new TrainComponent();
        trainComponent.initialize();

        WeatherComponent weatherComponent = new WeatherComponent();
        weatherComponent.initialize();

        // Initialize other backend services here as well
        // Otherwise Events won't go to them/they cant send response Events
    }
}