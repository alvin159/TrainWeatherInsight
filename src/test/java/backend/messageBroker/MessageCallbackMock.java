package backend.messageBroker;
import java.util.ArrayList;
import java.util.List;

import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;
import compse110.messagebroker.MessageCallback;

public class MessageCallbackMock implements MessageCallback {
    private final List<EventPayload> receivedPayloads = new ArrayList<>();

    @Override
    public void onMessageReceived(EventType event, EventPayload payload) {
        receivedPayloads.add(payload);
    }

    public List<EventPayload> getReceivedPayloads() {
        return receivedPayloads;
    }
}