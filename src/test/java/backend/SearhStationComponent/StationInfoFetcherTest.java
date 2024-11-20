package backend.SearhStationComponent;

import compse110.Entity.Station;
import compse110.backend.SearhStationComponent.StationInfoFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StationInfoFetcherTest {

    private StationInfoFetcher stationInfoFetcher;

    @BeforeEach
    public void setUp() {
        stationInfoFetcher = StationInfoFetcher.getInstance();
    }

    @Test
    public void testGetInstance() {
        StationInfoFetcher anotherInstance = StationInfoFetcher.getInstance();
        assertNotNull(anotherInstance, "Instance should not be null");
        assertEquals(stationInfoFetcher, anotherInstance, "Both instances should be the same");
    }

    @Test
    public void testSearchStations() {
        String query = "Helsinki";
        List<Station> result = stationInfoFetcher.searchStations(query);
        assertNotNull(result, "Result list should not be null");
        assertFalse(result.isEmpty(), "Result list should not be empty for a valid query");
        assertTrue(result.stream().allMatch(station ->
                        station.getStationName().toLowerCase().contains(query.toLowerCase())
                                || station.getStationShortCode().toLowerCase().contains(query.toLowerCase())),
                "All returned stations should match the query"
        );
    }

    @Test
    public void testGetStationByShortCode() {
        String shortCode = "HKI"; // Example short code for Helsinki
        Station station = stationInfoFetcher.getStationByShortCode(shortCode);
        assertNotNull(station, "Station should not be null for a valid short code");
        assertEquals(shortCode, station.getStationShortCode(), "Short code should match the input");
    }

    @Test
    public void testCacheRefresh() {
        List<Station> stations = stationInfoFetcher.searchStations("Helsinki");
        assertNotNull(stations, "Stations list should not be null after refreshing cache");
        assertFalse(stations.isEmpty(), "Stations list should not be empty after refreshing cache");
    }
}
