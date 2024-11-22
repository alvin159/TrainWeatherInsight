package compse110.backend.SearhStationComponent;

import java.util.List;

import compse110.Entity.Station;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.SearchStationRequest;
import compse110.backend.utils.BackendComponent;

/**
 * The SearchStationComponent class is responsible for handling search station requests.
 * It extends the BackendComponent class and utilizes the StationInfoFetcher class to search for stations.
 * 
 * The component should only be used through Events
 */
public class SearchStationComponent extends BackendComponent {
    private StationInfoFetcher stationInfoFetcher;

    /**
     * Receives Event from message broker and handles that event.
     * 
     * @param event   The type of event to handle.
     * @param payload The payload associated with the event.
     */
    @Override
    protected void handleEvent(EventType event, EventPayload payload) {
        if(event == EventType.SEARCH_STATION_REQUEST && payload instanceof SearchStationRequest.Payload) {
            Events.SearchStationRequest.Payload searchStationRequest = getPayload(event, payload);
            List<Station> stationsToReturn = stationInfoFetcher.searchStations(searchStationRequest.getCurrentSearchInput());
            String textFieldId = searchStationRequest.getTextFieldId();
            broker.publish(Events.SearchStationResponse.TOPIC, new Events.SearchStationResponse.Payload(stationsToReturn, textFieldId));

        }
    }

    public void initialize() {
        broker.subscribe(EventType.SEARCH_STATION_REQUEST, this);
        stationInfoFetcher = StationInfoFetcher.getInstance();
    }

    public void shutdown() {
        broker.unsubscribe(EventType.SEARCH_STATION_REQUEST, this);
        executor.shutdown();
    }
    
}
