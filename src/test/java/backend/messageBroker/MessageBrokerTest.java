package backend.messageBroker;

import compse110.messagebroker.MessageBroker;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBrokerTest {
    private MessageBroker messageBroker;
    private MessageCallbackMock callback;
    private EventPayload payload;

    @BeforeEach
    void setUp() {
        messageBroker = MessageBroker.getInstance();
        callback = new MessageCallbackMock();
        payload = new Events.AbbreviationRequest.Payload("HEL");
    }

    @Test
    void testSubscribeAndPublish() {
        EventType eventType = EventType.ABBREVIATION_REQUEST;
        messageBroker.subscribe(eventType, callback);
        messageBroker.publish(eventType, payload);

        assertEquals(1, callback.getReceivedPayloads().size());
        assertEquals(payload, callback.getReceivedPayloads().get(0));
    }

    @Test
    void testUnsubscribe() {
        EventType eventType = EventType.ABBREVIATION_REQUEST;
        messageBroker.subscribe(eventType, callback);
        messageBroker.unsubscribe(eventType, callback);
        messageBroker.publish(eventType, payload);

        assertTrue(callback.getReceivedPayloads().isEmpty());
    }

    @Test
    void testPublishWithoutSubscribers() {
        EventType eventType = EventType.ABBREVIATION_REQUEST;
        messageBroker.publish(eventType, payload);

        assertTrue(callback.getReceivedPayloads().isEmpty());
    }
}