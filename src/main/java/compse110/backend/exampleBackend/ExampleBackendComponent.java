package compse110.backend.exampleBackend;

import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleBackendComponent implements MessageCallback {
    private static final MessageBroker broker = MessageBroker.getInstance();
    // Executor service to handle the request asynchronously
    // Otherwise the backend would freeze the whole application while it's processing
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onMessageReceived(String topic, Object payload) {
        if (topic.equals("exampleRequest")) {
            System.out.println("Request message received in backend: " + payload);
            executorService.submit(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                broker.publish("exampleRequest_response", "Sample response from backend");
            });
        }
    }

    public void initialize() {
        broker.subscribe("exampleRequest", this);
    }

    public void shutdown() {
        broker.unsubscribe("exampleRequest", this);
        executorService.shutdown();
    }
}