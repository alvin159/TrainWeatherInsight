package compse110.Entity;

public class WeatherResponse {
    private double temp;
    private String description;

    public WeatherResponse(double temp, String description) {
        this.temp = temp;
        this.description = description;
    }

    public WeatherResponse() {}

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // 重写 toString 方法
    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temp=" + temp +
                ", description='" + description + '\'' +
                '}';
    }
}
