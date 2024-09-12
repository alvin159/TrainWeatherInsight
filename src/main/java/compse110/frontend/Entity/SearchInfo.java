package compse110.frontend.Entity;

import java.time.LocalDate;
import java.util.Date;

public class SearchInfo {

    private String Departing;

    private String Arriving;

    private LocalDate date;

    private boolean showCoolFacts;

    public SearchInfo() {
    }

    public String getDeparting() {
        return Departing;
    }

    public void setDeparting(String departing) {
        Departing = departing;
    }

    public String getArriving() {
        return Arriving;
    }

    public void setArriving(String arriving) {
        Arriving = arriving;
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

    @Override
    public String toString() {
        return "SearchInfo{" +
                "Departing='" + Departing + '\'' +
                ", Arriving='" + Arriving + '\'' +
                ", date=" + date +
                ", showCoolFacts=" + showCoolFacts +
                '}';
    }
}
