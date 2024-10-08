package compse110.backend.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;
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
    public void onMessageReceived(EventType event, EventPayload payload) {
        executor.submit(() -> {
            try {
                handleEvent(event, payload);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: Log error
                broker.publish(EventType.ERROR_RESPONSE, null);
            }
        });
    }

    protected abstract void handleEvent(EventType event, EventPayload payload);
    public abstract void initialize();
    public abstract void shutdown();
}
