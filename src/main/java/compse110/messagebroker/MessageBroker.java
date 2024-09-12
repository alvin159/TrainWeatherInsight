package compse110.messagebroker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import compse110.backend.exampleBackend.ExampleBackendComponent;

public class MessageBroker implements MessageBrokerInterface {
    private static MessageBroker instance;
    // Map to store subscribers for each topic
    private final Map<String, Set<MessageCallback>> subscribers = new HashMap<>();
    private static ExampleBackendComponent exampleBackendService;

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
    public void publish(String topic, Object payload) {
        // Check if there are any subscribers for the given topic
        if (subscribers.containsKey(topic)) {
            // Notify all subscribers of the topic
            for (MessageCallback callback : subscribers.get(topic)) {
                callback.onMessageReceived(topic, payload);
            }
        }
    }

    @Override
    public void subscribe(String topic, MessageCallback callback) {
        // Add the subscriber to the set of subscribers for the given topic
        subscribers.computeIfAbsent(topic, k -> new HashSet<>()).add(callback);
    }

    @Override
    public void unsubscribe(String topic, MessageCallback callback) {
        // Remove the subscriber from the set of subscribers for the given topic
        if (subscribers.containsKey(topic)) {
            subscribers.get(topic).remove(callback);
            // If no subscribers are left for the topic, remove the topic from the map
            if (subscribers.get(topic).isEmpty()) {
                subscribers.remove(topic);
            }
        }
    }

    private static void initializeBackend() {
        exampleBackendService = new ExampleBackendComponent();
        exampleBackendService.initialize();
        // Initialize other backend services here as well
    }
}