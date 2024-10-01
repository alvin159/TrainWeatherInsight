package compse110.backend.exampleBackend;

import compse110.Entity.BackendComponent;
import compse110.Entity.Events;
import compse110.Entity.Events.EventType;
import compse110.Entity.Station;


public class ExampleBackendComponent extends BackendComponent {
    
    @Override
    protected void handleEvent(EventType event, Object payload) {
        try {
            if(event == EventType.ABBREVIATION_REQUEST) {
                Events.AbbreviationRequest.Payload request = getPayload(event, payload);
                String stationShortCode = request.getStationShortCode();
                Station station = new Station();

                Thread.sleep(5000);
                broker.publish(Events.AbbreviationResponse.TOPIC, new Events.AbbreviationResponse.Payload(station));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public void initialize() {
        broker.subscribe(EventType.ABBREVIATION_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.ABBREVIATION_REQUEST, this);
        executor.shutdown();
    }
}