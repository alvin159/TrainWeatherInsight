package backend;

import compse110.Entity.DemographicRequest;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;

public class DemographicComponentTest {

    public static void main(String[] args) {

        MessageBroker broker = MessageBroker.getInstance();

        broker.subscribe(Events.EventType.DEMOGRAPHIC_RESPONSE, new MessageCallback() {
            @Override
            public void onMessageReceived(Events.EventType event, EventPayload payload) {
                Log.d("DemographicComponentTest", "Received event: " + event);
                Class<?> clazz = payload.getClass();
                Log.d("DemographicComponentTest", "Payload class: " + clazz.getName());
                if (payload instanceof Events.DemographicResponseEvent.Payload) {
                    Events.DemographicResponseEvent.Payload demographicRequestPayload = (Events.DemographicResponseEvent.Payload) payload;
                    Log.d("DemographicComponentTest", demographicRequestPayload.getDemographicResponse().toString());
                    Log.i("DemographicComponentTest", "City name: " + demographicRequestPayload.getDemographicResponse().getPopulation());
                }
            }
        });

        broker.publish(Events.EventType.DEMOGRAPHIC_REQUEST, new Events.DemographicRequestEvent.Payload(new DemographicRequest("Helsinki")));
        broker.publish(Events.DemographicRequestEvent.TOPIC, new Events.DemographicRequestEvent.Payload(new DemographicRequest("Oulu")));
        broker.publish(Events.DemographicRequestEvent.TOPIC, new Events.DemographicRequestEvent.Payload(new DemographicRequest("HKI")));
        broker.publish(Events.DemographicRequestEvent.TOPIC, new Events.DemographicRequestEvent.Payload(new DemographicRequest("Nokia")));
        broker.publish(Events.DemographicRequestEvent.TOPIC, new Events.DemographicRequestEvent.Payload(new DemographicRequest("nokia")));


//        broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload("HEL", "Helsinki"));
//
//
//        broker.subscribe(Events.EventType.SEARCH_STATION_RESPONSE, new MessageCallback() {
//            @Override
//            public void onMessageReceived(Events.EventType event, EventPayload payload) {
//                Log.d("DemographicComponentTest", "SEARCH_STATION_RESPONSE Received event: " + event);
//                Class<?> clazz = payload.getClass();
//                Log.d("DemographicComponentTest", "SEARCH_STATION_RESPONSE Payload class: " + clazz.getName());
//                Events.SearchStationResponse.Payload responsePayload = (Events.SearchStationResponse.Payload) payload;
//                Log.d("DemographicComponentTest", responsePayload.getStationList().toString());
//
//            }
//        });



    }
}
