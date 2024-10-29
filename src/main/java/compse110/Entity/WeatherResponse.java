package compse110.Entity;

public class WeatherResponse {
    private final String cityName;
    private final double temperature;
    private final String weatherCondition;
    private final String weatherIcon;

    public WeatherResponse(String cityName, double temperature, String weatherCondition, String weatherIcon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherIcon = weatherIcon;
    }

    public String getCityName() {
        return cityName;
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
                "cityName='" + cityName + '\'' +
                ", temperature=" + temperature +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                '}';
    }
}

