package compse110.Entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import compse110.Entity.Events.EventType;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;

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
    public void onMessageReceived(EventType event, Object payload) {
        executor.submit(() -> {
            try {
                handleEvent(event, payload);
            } catch (Exception e) {
                e.printStackTrace();
                broker.publish(EventType.ERROR_RESPONSE, "Failed to process request due to an unexpected error.");
            }
        });
    }

    protected abstract void handleEvent(EventType event, Object payload);
    public abstract void initialize();
    public abstract void shutdown();
}
