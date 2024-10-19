package compse110.Entity;

import java.util.Date;
import java.util.List;

// IMPORTANT! Gson library is required to run this code. DONT modify this class
public class TrainData {

    private int trainNumber;
    private Date departureDate;
    private int operatorUICCode;
    private String operatorShortCode;
    private String trainType;
    private String trainCategory;
    private String commuterLineID;
    private boolean runningCurrently;
    private boolean cancelled;
    private long version;
    private String timetableType;
    private Date timetableAcceptanceDate;
    private List<TimeTableRows> timeTableRows;

    public TrainData() {
    }

    public TrainData(int trainNumber, Date departureDate, int operatorUICCode, String operatorShortCode, String trainType, String trainCategory, String commuterLineID, boolean runningCurrently, boolean cancelled, long version, String timetableType, Date timetableAcceptanceDate, List<TimeTableRows> timeTableRows) {
        this.trainNumber = trainNumber;
        this.departureDate = departureDate;
        this.operatorUICCode = operatorUICCode;
        this.operatorShortCode = operatorShortCode;
        this.trainType = trainType;
        this.trainCategory = trainCategory;
        this.commuterLineID = commuterLineID;
        this.runningCurrently = runningCurrently;
        this.cancelled = cancelled;
        this.version = version;
        this.timetableType = timetableType;
        this.timetableAcceptanceDate = timetableAcceptanceDate;
        this.timeTableRows = timeTableRows;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(int operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public String getOperatorShortCode() {
        return operatorShortCode;
    }

    public void setOperatorShortCode(String operatorShortCode) {
        this.operatorShortCode = operatorShortCode;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    public boolean isRunningCurrently() {
        return runningCurrently;
    }

    public void setRunningCurrently(boolean runningCurrently) {
        this.runningCurrently = runningCurrently;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(String timetableType) {
        this.timetableType = timetableType;
    }

    public Date getTimetableAcceptanceDate() {
        return timetableAcceptanceDate;
    }

    public void setTimetableAcceptanceDate(Date timetableAcceptanceDate) {
        this.timetableAcceptanceDate = timetableAcceptanceDate;
    }

    public List<TimeTableRows> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRows> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    @Override
    public String toString() {
        return "TrainData{" +
                "trainNumber=" + trainNumber +
                ", departureDate=" + departureDate +
                ", operatorUICCode=" + operatorUICCode +
                ", operatorShortCode='" + operatorShortCode + '\'' +
                ", trainType='" + trainType + '\'' +
                ", trainCategory='" + trainCategory + '\'' +
                ", commuterLineID='" + commuterLineID + '\'' +
                ", runningCurrently=" + runningCurrently +
                ", cancelled=" + cancelled +
                ", version=" + version +
                ", timetableType='" + timetableType + '\'' +
                ", timetableAcceptanceDate=" + timetableAcceptanceDate +
                ", timeTableRows=" + timeTableRows +
                '}';
    }
}

