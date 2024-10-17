package compse110.backend.Entity;

import compse110.Entity.Station;

import java.util.Date;
import java.util.List;

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
