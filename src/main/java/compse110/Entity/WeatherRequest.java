package compse110.Entity;
import java.time.LocalDate;

public class WeatherRequest {
    private LocalDate date;
    private String cityName;
    private Double longitude;
    private Double latitude;

    public WeatherRequest(LocalDate date, Double longitude, Double latitude) {
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "WeatherRequest{" +
                "date=" + date +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
