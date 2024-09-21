package compse110.Entity;

public class Station {

    private boolean passengerTraffic;
    private String type;
    private String stationName;
    private String stationShortCode;
    private int stationUICCode;
    private String countryCode;
    private double longitude;
    private double latitude;

    public Station() {
    }

    public Station(boolean passengerTraffic, String type, String stationName, String stationShortCode, int stationUICCode, String countryCode, double longitude, double latitude) {
        this.passengerTraffic = passengerTraffic;
        this.type = type;
        this.stationName = stationName;
        this.stationShortCode = stationShortCode;
        this.stationUICCode = stationUICCode;
        this.countryCode = countryCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public boolean isPassengerTraffic() {
        return passengerTraffic;
    }

    public void setPassengerTraffic(boolean passengerTraffic) {
        this.passengerTraffic = passengerTraffic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public int getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(int stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String showInfo() {
        return "Station Name: " + stationName +
                "\nStation ShortCode: " + stationShortCode +
                "\nPassengerTraffic: " + passengerTraffic +
                "\nLongitude: " + longitude +
                "\nLatitude: " + latitude + "\n\n";
    }

    @Override
    public String toString() {
        return "Station{" +
                "passengerTraffic=" + passengerTraffic +
                ", type='" + type + '\'' +
                ", stationName='" + stationName + '\'' +
                ", stationShortCode='" + stationShortCode + '\'' +
                ", stationUICCode=" + stationUICCode +
                ", countryCode='" + countryCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
