package compse110.frontend.Entity;

import compse110.Entity.Station;

import java.time.LocalDate;

public class SearchInfo {
    private Station departingStation;
    private Station arrivingStation;
    private LocalDate date;
    private boolean showCoolFacts;

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
}
