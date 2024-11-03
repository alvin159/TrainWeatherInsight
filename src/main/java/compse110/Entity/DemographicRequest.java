package compse110.Entity;

public class DemographicRequest {
    private String cityName;
    private String stationName;
    public DemographicRequest(String cityName, String stationName) {
        this.cityName = cityName;
        this.stationName = stationName;
    }

    public DemographicRequest() {}

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "DemographicInfoRequest{" +
                "cityName='" + cityName + '\'' +
                '}';
    }
}

