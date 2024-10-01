package compse110.backend.exampleBackend;

import compse110.Entity.Events;
import compse110.Entity.Events.EventType;
import compse110.Entity.Station;
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
    public void onMessageReceived(EventType event, Object payload) {
        if (event.equals(EventType.ABBREVIATION_REQUEST)) {
            System.out.println("Request message received in backend: " + payload);
            executorService.submit(() -> {
                try {
                    // Cast the payload to the appropriate type
                    Events.AbbreviationRequest.Payload requestPayload = (Events.AbbreviationRequest.Payload) payload;
                    String stationShortCode = requestPayload.getStationShortCode();

                    // Fake backend processing time
                    Thread.sleep(5000);

                    Station station = new Station();
                    Events.AbbreviationResponse.Payload responsePayload = new Events.AbbreviationResponse.Payload(station);

                    // Publish the response
                    broker.publish(EventType.ABBREVIATION_RESPONSE, responsePayload);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    broker.publish(EventType.ERROR_RESPONSE, "Failed to process abbreviation request due to interruption.");
                } catch (Exception e) {
                    e.printStackTrace();
                    broker.publish(EventType.ERROR_RESPONSE, "Failed to process abbreviation request due to an unexpected error.");
                }
            });
        }
    }

    public void initialize() {
        broker.subscribe(EventType.ABBREVIATION_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.ABBREVIATION_REQUEST, this);
        executorService.shutdown();
    }
}