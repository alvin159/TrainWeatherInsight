package compse110.backend;

import compse110.Entity.Events.EventType;
import compse110.messagebroker.MessageCallback;

public class TrainComponent implements MessageCallback {
    @Override
    public void onMessageReceived(EventType event, Object payload) {

    }
}
