package compse110.messagebroker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compse110.backend.AbbrevConverterComponent;
import compse110.backend.exampleBackend.ExampleBackendComponent;
import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;

public class MessageBroker implements MessageBrokerInterface {
    private static MessageBroker instance;
    // Map to store subscribers for each topic
    private final Map<EventType, Set<MessageCallback>> subscribers = new HashMap<>();
    
    private static ExampleBackendComponent exampleBackendService;
    private static AbbrevConverterComponent abbrevConverterService;

    private MessageBroker() {
        //To prevent initialization
    }

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

        private static void initializeBackend() {
        exampleBackendService = new ExampleBackendComponent();
        exampleBackendService.initialize();
        // Initialize other backend services here as well
        abbrevConverterService = new AbbrevConverterComponent();
        abbrevConverterService.initialize();
    }
}