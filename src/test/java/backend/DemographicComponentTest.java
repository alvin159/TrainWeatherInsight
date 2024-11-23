package backend;

import compse110.Entity.DemographicRequest;
import compse110.Entity.DemographicResponse;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DemographicComponentTest {

    private MessageBroker broker;

    @BeforeEach
    void setUp() {
        // Initialize the MessageBroker instance
        broker = MessageBroker.getInstance();

        // Subscribe to the DEMOGRAPHIC_RESPONSE event
        broker.subscribe(Events.EventType.DEMOGRAPHIC_RESPONSE, new MessageCallback() {
            @Override
            public void onMessageReceived(Events.EventType event, EventPayload payload) {
                Log.d("DemographicComponentTest", "Received event: " + event);

                // Verify the payload type and parse it
                assertTrue(payload instanceof Events.DemographicResponseEvent.Payload, "Payload should be of type DemographicResponseEvent.Payload");
                Events.DemographicResponseEvent.Payload demographicPayload = (Events.DemographicResponseEvent.Payload) payload;

                // Validate the content of DemographicResponse
                if (demographicPayload.getDemographicResponse() != null) {
                    DemographicResponse demographicResponse = demographicPayload.getDemographicResponse();
                    assertNotNull(demographicResponse, "DemographicResponse should not be null");
                    Log.d("DemographicComponentTest", "DemographicResponse: " + demographicResponse.toString());
                    assertTrue(demographicResponse.getPopulation() > 0, "Population should be greater than 0");
                    assertTrue(demographicResponse.getLandArea() > 0, "Land area should be greater than 0");
                    assertTrue(demographicResponse.getPopulationDensity() > 0, "Population density should be greater than 0");
                } else {
                    // Handle cases where the response is null (e.g., city not found)
                    Log.d("DemographicComponentTest", "No demographic data found for the given city.");
                    assertNotNull(demographicPayload.getErrorMessage(), "Error message should not be null");
                }
            }
        });
    }

    @Test
    void testPublishDemographicRequestEvent() {
        // Construct test DemographicRequest data
        DemographicRequest request = new DemographicRequest(
                "Helsinki","Helsinki Kivihaka" // Test with a valid station name
        );

        // Publish the DEMOGRAPHIC_REQUEST event
        Events.DemographicRequestEvent.Payload payload = new Events.DemographicRequestEvent.Payload(request);
        broker.publish(Events.EventType.DEMOGRAPHIC_REQUEST, payload);

        // Simulate a delay to wait for the asynchronous response (used only for simulating async operations)
        try {
            Thread.sleep(3000); // Wait for 3 seconds
        } catch (InterruptedException e) {
            fail("Test was interrupted");
        }

        // Verify that the subscription triggered the corresponding callback logic
        Log.d("DemographicComponentTest", "Finished publishing DEMOGRAPHIC_REQUEST event");
    }
}