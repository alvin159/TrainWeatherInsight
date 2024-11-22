package compse110.Entity;
import java.time.LocalDate;
/**
 * Represents a request for weather information based on specific geographic criteria and date.
 *
 * <p>This class encapsulates the following details:
 * <ul>
 *     <li><b>Date:</b> The date for which weather data is requested.</li>
 *     <li><b>Station Name:</b> The name of the weather station.</li>
 *     <li><b>Latitude:</b> The geographic latitude of the location.</li>
 *     <li><b>Longitude:</b> The geographic longitude of the location.</li>
 * </ul>
 *
 * <p>It is typically used to request weather data for a specific date and location
 * from weather data APIs.
 */
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
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
