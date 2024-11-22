package compse110.Entity;
/**
 * Represents a request for demographic information associated with a specific city.
 *
 * <p>This class encapsulates the following details:
 * <ul>
 *     <li><b>City Name:</b> The name of the city for which demographic data is requested.</li>
 *     <li><b>Station Name:</b> The name of the station within the city.</li>
 * </ul>
 *
 * <p>It is typically used as a data carrier for making requests to retrieve demographic details
 * related to a specific city.
 */
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

