package compse110.Entity;
import java.time.LocalDate;

public class WeatherRequest {
    private LocalDate date;
    private String stationName;
    private Double longitude;
    private Double latitude;

    public WeatherRequest(LocalDate date, Double longitude, Double latitude, String stationName) {
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.stationName = stationName;
    }

    public WeatherRequest() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "WeatherRequest{" +
                "date=" + date +
                ", cityName='" + stationName + '\'' +
                '}';
    }
}
