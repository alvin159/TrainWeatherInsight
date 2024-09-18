package compse110.frontend.Entity;

public class CityInformation {

    private int Id;

    private String title;

    private Forecast forecast;

    private CityDetails cityDetails;

    public CityInformation(int id, String title, Forecast forecast, CityDetails cityDetails) {
        Id = id;
        this.title = title;
        this.forecast = forecast;
        this.cityDetails = cityDetails;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }
}
