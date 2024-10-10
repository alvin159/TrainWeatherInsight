package compse110.Entity;

public class DemographicRequest {
    private String cityName;

    public DemographicRequest(String cityName) {
        this.cityName = cityName;
    }

    public DemographicRequest() {}

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "DemographicInfoRequest{" +
                "cityName='" + cityName + '\'' +
                '}';
    }
}

