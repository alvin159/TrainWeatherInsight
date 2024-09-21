package compse110.backend;

import compse110.Entity.Station;

public class AbbreviationObject {

    // Request field for the station short code
    private String stationShortCodeRequest;

    // Response field for the full station name
    private Station stationResponse;

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
    public Station getStationResponse() {
        return stationResponse;
    }

    // Setter for stationNameResponse
    public void setStationNameResponse(Station stationResponse) {
        this.stationResponse = stationResponse;
    }

    // For demonstration
    @Override
    public String toString() {
        return "AbbreviationObject{" +
                "stationShortCodeRequest='" + stationShortCodeRequest + '\'' +
                ", stationNameResponse='" + stationResponse + '\'' +
                '}';
    }
}
