package compse110.frontend.Entity;

import javafx.scene.image.ImageView;

/**
 * The Forecast class represents a weather forecast, encapsulating details such as 
 * temperature, weather status, a visual representation of the weather, and additional
 * forecast details. 
 */

public class Forecast {

    private double temperature;

    private String weatherStatus;

    private ImageView weatherImage;

    private ForecastDetails details;

    public Forecast(double temperature, String weatherStatus, ImageView weatherImage, ForecastDetails details) {
        this.temperature = temperature;
        this.weatherStatus = weatherStatus;
        this.weatherImage = weatherImage;
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

    public ImageView getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(ImageView weatherImage) {
        this.weatherImage = weatherImage;
    }

    public ForecastDetails getDetails() {
        return details;
    }

    public void setDetails(ForecastDetails details) {
        this.details = details;
    }
}
