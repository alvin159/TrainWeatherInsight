package compse110.Entity;
import java.time.LocalDate;

public class WeatherRequest {
    private LocalDate date;
    private String cityName;

    public WeatherRequest(LocalDate date, String cityName) {
        this.date = date;
        this.cityName = cityName;
    }

    public WeatherRequest() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
