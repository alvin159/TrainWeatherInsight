package compse110.Entity;

import java.util.Date;
import java.util.List;

public class TimeTableRows {
        private String stationShortCode;
        private int stationUICCode;
        private String countryCode;
        private String type;
        private boolean trainStopping;
        private boolean commercialStop;
        private String commercialTrack;
        private boolean cancelled;
        private Date scheduledTime;
        private Date liveEstimateTime;
        private String estimateSource;
        private int differenceInMinutes;
        private List<Causes> causes;

        public TimeTableRows() {
        }

    public TimeTableRows(String stationShortCode, int stationUICCode, String countryCode, String type, boolean trainStopping, boolean commercialStop, String commercialTrack, boolean cancelled, Date scheduledTime, Date liveEstimateTime, String estimateSource, int differenceInMinutes, List<Causes> causes) {
        this.stationShortCode = stationShortCode;
        this.stationUICCode = stationUICCode;
        this.countryCode = countryCode;
        this.type = type;
        this.trainStopping = trainStopping;
        this.commercialStop = commercialStop;
        this.commercialTrack = commercialTrack;
        this.cancelled = cancelled;
        this.scheduledTime = scheduledTime;
        this.liveEstimateTime = liveEstimateTime;
        this.estimateSource = estimateSource;
        this.differenceInMinutes = differenceInMinutes;
        this.causes = causes;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTrainStopping() {
        return trainStopping;
    }

    public void setTrainStopping(boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public boolean isCommercialStop() {
        return commercialStop;
    }

    public void setCommercialStop(boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public String getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Date getLiveEstimateTime() {
        return liveEstimateTime;
    }

    public void setLiveEstimateTime(Date liveEstimateTime) {
        this.liveEstimateTime = liveEstimateTime;
    }

    public String getEstimateSource() {
        return estimateSource;
    }

    public void setEstimateSource(String estimateSource) {
        this.estimateSource = estimateSource;
    }

    public int getDifferenceInMinutes() {
        return differenceInMinutes;
    }

    public void setDifferenceInMinutes(int differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public List<Causes> getCauses() {
        return causes;
    }

    public void setCauses(List<Causes> causes) {
        this.causes = causes;
    }

    @Override
    public String toString() {
        return "TimeTableRows{" +
                "stationShortCode='" + stationShortCode + '\'' +
                ", stationUICCode=" + stationUICCode +
                ", countryCode='" + countryCode + '\'' +
                ", type='" + type + '\'' +
                ", trainStopping=" + trainStopping +
                ", commercialStop=" + commercialStop +
                ", commercialTrack='" + commercialTrack + '\'' +
                ", cancelled=" + cancelled +
                ", scheduledTime=" + scheduledTime +
                ", liveEstimateTime=" + liveEstimateTime +
                ", estimateSource='" + estimateSource + '\'' +
                ", differenceInMinutes=" + differenceInMinutes +
                ", causes=" + causes +
                '}';
    }
}
