package compse110.frontend.Entity;

public class Forecast {

    private double temperature;

    private String weatherStatus;

    private String weatherImageSrc;

    private ForecastDetails details;

    public Forecast(double temperature, String weatherStatus, String weatherImageSrc, ForecastDetails details) {
        this.temperature = temperature;
        this.weatherStatus = weatherStatus;
        this.weatherImageSrc = weatherImageSrc;
        this.details = details;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public String getWeatherImageSrc() {
        return weatherImageSrc;
    }

    public void setWeatherImageSrc(String weatherImageSrc) {
        this.weatherImageSrc = weatherImageSrc;
    }

    public ForecastDetails getDetails() {
        return details;
    }

    public void setDetails(ForecastDetails details) {
        this.details = details;
    }
}
