package compse110.backend.Entity;

import compse110.Entity.Station;

import java.util.Date;
import java.util.List;

/**
 * The StationStorage class is responsible for storing a list of Station objects
 * along with a cache time indicating when the data was cached.
 * The data will be transformed from JSON to this class.
 */
public class StationStorage {

    private Date cacheTime;

    private List<Station> stations;

    public StationStorage() {
    }

    public StationStorage(Date cacheTime, List<Station> stations) {
        this.cacheTime = cacheTime;
        this.stations = stations;
    }

    public Date getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Date cacheTime) {
        this.cacheTime = cacheTime;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}
