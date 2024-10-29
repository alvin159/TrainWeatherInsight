package compse110.Entity;

import compse110.frontend.Entity.Forecast;

import java.util.Date;

public class TrainInformation {

    private int Id;

    private String trainName;

    private Date departureTime;

    private long duration;

    private Date estimatedTime; // Can be null

    private String departureTrack; //i.e. Track 1 or Track 2A 3B

    private String arriveStationName;

    private Date arriveTime;

    private String arriveTrack;

    private Forecast forecast;

    public TrainInformation() {
    }

    public TrainInformation(int id, String trainName, Date departureTime, long duration, Date estimatedTime, String departureTrack, String arriveStationName, Date arriveTime, String arriveTrack, Forecast forecast) {
        Id = id;
        this.trainName = trainName;
        this.departureTime = departureTime;
        this.duration = duration;
        this.estimatedTime = estimatedTime;
        this.departureTrack = departureTrack;
        this.arriveStationName = arriveStationName;
        this.arriveTime = arriveTime;
        this.arriveTrack = arriveTrack;
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

    public String getDepartureTrack() {
        return departureTrack;
    }

    public void setDepartureTrack(String departureTrack) {
        this.departureTrack = departureTrack;
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

    public String getArriveTrack() {
        return arriveTrack;
    }

    public void setArriveTrack(String arriveTrack) {
        this.arriveTrack = arriveTrack;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
