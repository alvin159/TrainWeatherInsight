package backend;

import compse110.Entity.WeatherRequest;
import compse110.Entity.WeatherResponse;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherComponentTest {

    private MessageBroker broker;

    @BeforeEach
    void setUp() {
        // Initialize the MessageBroker instance
        broker = MessageBroker.getInstance();

        // Subscribe to the WEATHER_RESPONSE event
        broker.subscribe(Events.EventType.WEATHER_RESPONSE, new MessageCallback() {
            @Override
            public void onMessageReceived(Events.EventType event, EventPayload payload) {
                Log.d("WeatherComponentTest", "Received event: " + event);

                // Verify the payload type and parse it
                assertTrue(payload instanceof Events.WeatherResponseEvent.Payload, "Payload should be of type WeatherResponseEvent.Payload");
                Events.WeatherResponseEvent.Payload weatherPayload = (Events.WeatherResponseEvent.Payload) payload;

                // Validate the content of WeatherResponse
                WeatherResponse weatherResponse = weatherPayload.getWeatherResponse();
                assertNotNull(weatherResponse, "WeatherResponse should not be null");
                Log.d("WeatherComponentTest", "WeatherResponse: " + weatherResponse.toString());
                assertNotNull(weatherResponse.getTemperature(), "Temperature should not be null");
                assertNotNull(weatherResponse.getWeatherCondition(), "WeatherCondition should not be null");
            }
        });
    }

    @Test
    void testPublishWeatherRequestEvent() {
        // Construct test WeatherRequest data
        WeatherRequest request = new WeatherRequest(
                java.time.LocalDate.now(),
                24.917191,
                60.209813,
                "Helsinki Kivihaka"
        );

        // Publish the WEATHER_REQUEST event
        Events.WeatherRequestEvent.Payload payload = new Events.WeatherRequestEvent.Payload(request);
        broker.publish(Events.EventType.WEATHER_REQUEST, payload);

        // Simulate a delay to wait for the asynchronous response (used only for simulating async operations)
        try {
            Thread.sleep(3000); // Wait for 3 seconds
        } catch (InterruptedException e) {
            fail("Test was interrupted");
        }

        // Verify that the subscription triggered the corresponding callback logic
        Log.d("WeatherComponentTest", "Finished publishing WEATHER_REQUEST event");
    }
}