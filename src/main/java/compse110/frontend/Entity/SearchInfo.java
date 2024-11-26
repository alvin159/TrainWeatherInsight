package compse110.frontend.Entity;

import com.google.gson.JsonArray;
import compse110.Entity.Station;

import java.time.LocalDate;

/**
 * The SearchInfo class represents the information required for a train timetable search query.
 * It includes details about the departing and arriving stations, the date of travel,
 * whether to show cool facts, and weather data for both the departing and arriving locations.
 */
public class SearchInfo {
    private Station departingStation;
    private Station arrivingStation;
    private LocalDate date;
    private boolean showCoolFacts;

    private JsonArray departingWeatherData;
    private JsonArray arrivingWeatherData;

    public SearchInfo() {
    }

    public Station getDepartingStation() {
        return departingStation;
    }

    public void setDepartingStation(Station departingStation) {
        this.departingStation = departingStation;
    }

    public Station getArrivingStation() {
        return arrivingStation;
    }

    public void setArrivingStation(Station arrivingStation) {
        this.arrivingStation = arrivingStation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isShowCoolFacts() {
        return showCoolFacts;
    }

    public void setShowCoolFacts(boolean showCoolFacts) {
        this.showCoolFacts = showCoolFacts;
    }

    public JsonArray getDepartingWeatherData() {
        return departingWeatherData;
    }

    public void setDepartingWeatherData(JsonArray departingWeatherData) {
        this.departingWeatherData = departingWeatherData;
    }

    public JsonArray getArrivingWeatherData() {
        return arrivingWeatherData;
    }

    public void setArrivingWeatherData(JsonArray arrivingWeatherData) {
        this.arrivingWeatherData = arrivingWeatherData;
    }
}
