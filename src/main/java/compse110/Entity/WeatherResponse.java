package compse110.Entity;

import com.google.gson.JsonArray;
/**
 * Represents a response containing weather information for a specific location and time.
 *
 * <p>This class encapsulates the following details:
 * <ul>
 *     <li><b>Temperature:</b> The current temperature at the location.</li>
 *     <li><b>Weather Condition:</b> A textual description of the current weather condition (e.g., "Cloudy", "Sunny").</li>
 *     <li><b>Weather Icon:</b> The identifier for the weather condition's corresponding icon.</li>
 *     <li><b>Hours:</b> A JSON array containing hourly weather details.</li>
 * </ul>
 *
 * <p>It is typically used to encapsulate weather data retrieved from a weather API.
 */
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

