package compse110.Entity;

import com.google.gson.JsonArray;

public class WeatherResponse {
    private final double temperature;
    private final String weatherCondition;
    private final String weatherIcon;
    private final JsonArray hours;

    public WeatherResponse(double temperature, String weatherCondition, String weatherIcon, JsonArray hours) {
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherIcon = weatherIcon;
        this.hours = hours; // Initialize hours in constructor
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public JsonArray getHours() {
        return hours;
    }

    @Override
    public String toString() {
        return "WeatherPayload{" +
                ", temperature=" + temperature +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                '}';
    }
}

