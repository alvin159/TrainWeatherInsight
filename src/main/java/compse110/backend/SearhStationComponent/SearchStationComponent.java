package compse110.backend.SearhStationComponent;

import java.util.List;

import compse110.Entity.Station;
import compse110.Entity.TimeTableRows;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.SearchStationRequest;
import compse110.backend.utils.BackendComponent;

/**
 * The SearchStationComponent class is responsible for handling search station requests.
 * It extends the BackendComponent class and utilizes the StationInfoFetcher class to search for stations.
 * 
 * Listens for SEARCH_STATION_REQUEST and ADD_STATION_NAME_REQUEST events and responds with
 * SEARCH_STATION_RESPONSE and ADD_STATION_NAME_REQUEST events.
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
        } else if (event == EventType.ADD_STATION_NAME_REQUEST && payload instanceof Events.AddStationNameRequest.Payload) {
            Events.AddStationNameRequest.Payload addStationNameRequest = getPayload(event, payload);
            List<TimeTableRows> timeTableRows = stationInfoFetcher.addTimeTableRowsStationName(addStationNameRequest.getTimeTableRows());
            broker.publish(Events.AddStationNameResponse.TOPIC, new Events.AddStationNameResponse.Payload(timeTableRows));
        }
    }

    public void initialize() {
        broker.subscribe(EventType.SEARCH_STATION_REQUEST, this);
        broker.subscribe(EventType.ADD_STATION_NAME_REQUEST, this);
        stationInfoFetcher = StationInfoFetcher.getInstance();
    }

    public void shutdown() {
        broker.unsubscribe(EventType.SEARCH_STATION_REQUEST, this);
        broker.unsubscribe(EventType.ADD_STATION_NAME_REQUEST, this);
        executor.shutdown();
    }
    
}
