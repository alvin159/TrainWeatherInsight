package backend.SearhStationComponent;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.SearchStationRequest;
import compse110.messagebroker.MessageBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.messageBroker.MessageCallbackMock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchStationComponentTest {

    private MessageBroker messageBroker;
    private MessageCallbackMock callback;

    @BeforeEach
    public void setUp() {
        messageBroker = MessageBroker.getInstance();
        callback = new MessageCallbackMock();
        messageBroker.subscribe(EventType.SEARCH_STATION_RESPONSE, callback);
    }

    @Test
    void testSubscribeAndPublish() {
        messageBroker.publish(EventType.SEARCH_STATION_REQUEST, new SearchStationRequest.Payload("Helsinki as", "field1"));

        try {
            Thread.sleep(1000); // 1 second
        } catch (InterruptedException e) {
            fail("Test was interrupted");
        }
        assertEquals(1, callback.getReceivedPayloads().size());
        Events.SearchStationResponse.Payload payload = null;
        try {
            payload = (Events.SearchStationResponse.Payload) callback.getReceivedPayloads().get(0);
        } catch (Exception e) {
            fail("Payload is not of type SearchStationResponse");
        }
        assertEquals(1, payload.getStationList().size());
        assertEquals("Helsinki asema", payload.getStationList().get(0).getStationName());
    }

    //Test with string that gives no results
    @Test
    void testSubscribeAndPublishNoResults() {
        messageBroker.publish(EventType.SEARCH_STATION_REQUEST, new SearchStationRequest.Payload("New Yo", "field1"));

        try {
            Thread.sleep(1000); // 1 second
        } catch (InterruptedException e) {
            fail("Test was interrupted");
        }
        assertEquals(1, callback.getReceivedPayloads().size());
        Events.SearchStationResponse.Payload payload = null;
        try {
            payload = (Events.SearchStationResponse.Payload) callback.getReceivedPayloads().get(0);
        } catch (Exception e) {
            fail("Payload is not of type SearchStationResponse");
        }
        assertEquals(0, payload.getStationList().size());
    }
}
