package compse110.backend;

public class AbbreviationObject {

    // Request field for the station short code
    private String stationShortCodeRequest;

    // Response field for the full station name
    private String stationNameResponse;

    // Constructor
    public AbbreviationObject(String stationShortCodeRequest) {
        this.stationShortCodeRequest = stationShortCodeRequest;
    }

    // Getter for stationShortCodeRequest
    public String getStationShortCodeRequest() {
        return stationShortCodeRequest;
    }

    // Setter for stationShortCodeRequest
    public void setStationShortCodeRequest(String stationShortCodeRequest) {
        this.stationShortCodeRequest = stationShortCodeRequest;
    }

    // Getter for stationNameResponse
    public String getStationNameResponse() {
        return stationNameResponse;
    }

    // Setter for stationNameResponse
    public void setStationNameResponse(String stationNameResponse) {
        this.stationNameResponse = stationNameResponse;
    }

    // For demonstration
    @Override
    public String toString() {
        return "AbbreviationObject{" +
                "stationShortCodeRequest='" + stationShortCodeRequest + '\'' +
                ", stationNameResponse='" + stationNameResponse + '\'' +
                '}';
    }
}
