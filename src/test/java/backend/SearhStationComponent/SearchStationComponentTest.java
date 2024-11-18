package backend.SearhStationComponent;

import compse110.Entity.Station;
import compse110.Utils.Events;
import compse110.Utils.EventPayload;
import compse110.Utils.Events.SearchStationRequest;
import compse110.backend.SearhStationComponent.SearchStationComponent;
import compse110.backend.SearhStationComponent.StationInfoFetcher;
import compse110.messagebroker.MessageBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class SearchStationComponentTest {

    private SearchStationComponent searchStationComponent;
    private StationInfoFetcher stationInfoFetcherMock;
    //private MessageBroker messageBrokerMock;

    @BeforeEach
    public void setUp() {
        //messageBrokerMock = MessageBroker.getInstance();
        stationInfoFetcherMock = mock(StationInfoFetcher.class);
        searchStationComponent = new SearchStationComponent();
        searchStationComponent.initialize();
        searchStationComponent.stationInfoFetcher = stationInfoFetcherMock; // Inject the mock,need to change stationInfoFetcher to public before testing
    }

    @Test
    public void testHandleEventWithValidRequest() {
        // Set up test data
        String searchInput = "Helsinki";
        String textFieldId = "field1";
        List<Station> mockStations = new ArrayList<>();
        mockStations.add(new Station(
                false,              // passengerTraffic
                "STATION",          // type
                "Helsinki Kivihaka",// stationName
                "Helsinki",         // cityName
                "KHK",              // stationShortCode
                1028,               // stationUICCode
                "FI",               // countryCode
                24.917191,          // longitude
                60.209813           // latitude
        ));

        // Configure the mock
        when(stationInfoFetcherMock.searchStations(searchInput)).thenReturn(mockStations);

        // Create the payload
        EventPayload payload = new SearchStationRequest.Payload(searchInput, textFieldId);

        // Invoke the method (need to change the handleEvent to public before testing)
        searchStationComponent.handleEvent(Events.EventType.SEARCH_STATION_REQUEST, payload);

        // Verify that the stationInfoFetcher was called with the correct input
        verify(stationInfoFetcherMock, times(1)).searchStations(searchInput);
    }
}
