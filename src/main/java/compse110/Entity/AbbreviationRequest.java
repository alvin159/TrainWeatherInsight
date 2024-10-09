package compse110.Entity;

public class AbbreviationRequest {
    private String stationShortCode;

    public AbbreviationRequest(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public AbbreviationRequest() {}

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    @Override
    public String toString() {
        return "AbbreviationRequest{" +
                "stationShortCode='" + stationShortCode + '\'' +
                '}';
    }
}
