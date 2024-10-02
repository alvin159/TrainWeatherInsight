package compse110.frontend.Entity;

import java.util.Date;

public class TrainInformation {

    private int Id;

    private String trainName;

    private Date departureTime;

    private long duration;

    private Date estimatedTime; // Can be null

    private String track; //i.e. Track 1 or Track 2A 3B

    private String arriveStationName;

    private Date arriveTime;

    private Forecast forecast;

    public TrainInformation() {
    }

    public TrainInformation(int id, String trainName, Date departureTime, Date estimatedTime, String track, String arriveStationName, Date arriveTime, Forecast forecast) {
        Id = id;
        this.trainName = trainName;
        this.departureTime = departureTime;
        this.estimatedTime = estimatedTime;
        this.track = track;
        this.arriveStationName = arriveStationName;
        this.arriveTime = arriveTime;
        this.forecast = forecast;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArriveStationName() {
        return arriveStationName;
    }

    public void setArriveStationName(String arriveStationName) {
        this.arriveStationName = arriveStationName;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
