package compse110.Entity;

public class AbbreviationResponse {
    private String stationName;

    public AbbreviationResponse(String stationName) {
        this.stationName = stationName;
    }

    public AbbreviationResponse() {}

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "AbbreviationResponse{" +
                "stationName='" + stationName + '\'' +
                '}';
    }
}
