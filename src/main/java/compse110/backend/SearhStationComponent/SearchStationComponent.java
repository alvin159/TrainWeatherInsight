package compse110.backend.SearhStationComponent;

import java.util.List;

import compse110.Entity.Station;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.SearchStationRequest;
import compse110.backend.utils.BackendComponent;

public class SearchStationComponent extends BackendComponent {
    private StationInfoFetcher stationInfoFetcher;

    @Override
    protected void handleEvent(EventType event, EventPayload payload) {
        if(event == EventType.SEARCH_STATION_REQUEST && payload instanceof SearchStationRequest.Payload) {
            try {
                Events.SearchStationRequest.Payload searchStationRequest = getPayload(event, payload);
                List<Station> stationsToReturn = stationInfoFetcher.searchStations(searchStationRequest.getCurrentSearchInput());
                String textFieldId = searchStationRequest.getTextFieldId();
                broker.publish(Events.SearchStationResponse.TOPIC, new Events.SearchStationResponse.Payload(stationsToReturn, textFieldId));
            } catch (Exception e) {
                // TODO: Publish Events.ErrorResponse.TOPIC instead
                broker.publish(Events.EventType.ERROR_RESPONSE,null);
            }

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
