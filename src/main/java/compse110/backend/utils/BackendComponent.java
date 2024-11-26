package compse110.backend.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;

/**
 * The BackendComponent class is an abstract base class for backend components that provides a
 * framework for handling events and managing the lifecycle of backend components.
 * It implements the MessageCallback interface to receive messages and process them
 * asynchronously using an ExecutorService. This way backend components processing will
 * not block frontend GUI or other backend components.
 */
public abstract class BackendComponent implements MessageCallback {
    protected static final MessageBroker broker = MessageBroker.getInstance();
    protected final ExecutorService executor = Executors.newSingleThreadExecutor();

    protected <T extends EventPayload> T getPayload(EventType event, Object payload) {
        if (payload instanceof EventPayload) {
            return (T) payload;
        }
        throw new IllegalArgumentException("Payload is not of the expected type for event: " + event);
    }

    @Override
    public void onMessageReceived(EventType event, EventPayload payload) {
        executor.submit(() -> {
            try {
                handleEvent(event, payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the event. This method must be implemented by subclasses to define specific event handling logic.
     *
     * @param event   the event type
     * @param payload the event payload
     */
    protected abstract void handleEvent(EventType event, EventPayload payload);

    /**
     * Initializes the backend component. This method must be implemented by subclasses to define specific initialization logic.
     */
    public abstract void initialize();

    /**
     * Shuts down the backend component. This method must be implemented by subclasses to define specific shutdown logic.
     */
    public abstract void shutdown();
}
