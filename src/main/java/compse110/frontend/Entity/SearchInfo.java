package compse110.frontend.Entity;

import java.time.LocalDate;

public class SearchInfo {
    private String departing;
    private String arriving;
    private LocalDate date;
    private boolean showCoolFacts;

    public String getDepartingCity() {
        return departing;
    }

    public void setDeparting(String departing) {
        this.departing = departing;
    }

    public String getArrivingCity() {
        return arriving;
    }

    public void setArriving(String arriving) {
        this.arriving = arriving;
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
