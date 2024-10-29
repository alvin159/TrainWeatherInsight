package compse110.Entity;

public class WeatherResponse {
    private final double temperature;
    private final String weatherCondition;
    private final String weatherIcon;

    public WeatherResponse(double temperature, String weatherCondition, String weatherIcon) {
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherIcon = weatherIcon;
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

    @Override
    public String toString() {
        return "WeatherPayload{" +
                ", temperature=" + temperature +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                '}';
    }
}

