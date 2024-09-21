package compse110.frontend.Entity;

import java.util.Date;

public class TrainInformation {

    private int Id;

    private String trainName;

    private Date departureTime;

    private Date estimatedTime; // Can be null

    private String track; //i.e. Track 1 or Track 2A 3B

    private String arriveStationName;

    private Date arriveTime;

    private Forecast forecast;

    public TrainInformation() {
    }




}
