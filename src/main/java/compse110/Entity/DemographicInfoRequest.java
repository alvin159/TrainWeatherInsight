package compse110.Entity;

public class DemographicInfoRequest {
    private String cityName;

    public DemographicInfoRequest(String cityName) {
        this.cityName = cityName;
    }

    public DemographicInfoRequest() {}

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

