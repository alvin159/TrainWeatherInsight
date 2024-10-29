package compse110.backend;

import compse110.Entity.TimeTableRows;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import compse110.Entity.TrainData;
import compse110.Entity.TrainRequestError;
import compse110.Utils.API_Config;
import compse110.Utils.Log;
import compse110.backend.utils.BackendComponent;
import compse110.frontend.Entity.TrainInformation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainComponent extends BackendComponent {

    private List<TrainData> getTrainData(Date departingDate, String departureStationShortCode) {
        return getTrainData(departingDate, departureStationShortCode, null);
    }

    private List<TrainData> getTrainData(Date departingDate, String departureStationShortCode, String arrivalStationShortCode) {

        //create a OkHttp client
        OkHttpClient client = new OkHttpClient();

        Log.d(API_Config.TRAIN_DATA_URL + departureStationShortCode + (arrivalStationShortCode != null ? "/" + arrivalStationShortCode + "?departure_date=" + covertDateToString(departingDate) : ""));
        //create a request
        Request request = new Request.Builder()
                .url(API_Config.TRAIN_DATA_URL + departureStationShortCode + (arrivalStationShortCode != null ? "/" + arrivalStationShortCode + "?departure_date=" + covertDateToString(departingDate) : ""))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseString = response.body().string();

            try {
                // Try to handle the response
                // Use gson to transfer response to json
                List<TrainData> trainDataList = new Gson().fromJson(responseString, new TypeToken<List<TrainData>>() {}.getType());
                return trainDataList;
            }
            catch (JsonSyntaxException e) {
                // This error message gets thrown for example if no trains were found and the response is different than List<TrainData>
                TrainRequestError error = new Gson().fromJson(responseString, TrainRequestError.class);
                System.out.println(error.getCode());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private List<TrainInformation> getTrainInformation(List<TrainData> trainDataList, String departureStationShortCode) {
        if(trainDataList == null || trainDataList.isEmpty()) {
            return null;
        }
        List<TrainInformation> trainInformation = new ArrayList<>();
        for (int i = 0; i < trainDataList.size(); i++) {
            TrainData trainData = trainDataList.get(i);
            TrainInformation information = new TrainInformation();
            information.setId(i);
            information.setTrainName(trainData.getTrainType() + trainData.getTrainNumber());
            information.setDepartureTime(trainData.getDepartureDate());
            TimeTableRows rows = getTimeTableRowsFromTrainData(trainData, departureStationShortCode);
            if (rows != null) {
                information.setEstimatedTime(rows.getLiveEstimateTime());
                information.setTrack(rows.getCommercialTrack());
            }
            //get arriver station information
            TimeTableRows arriveRows = trainData.getTimeTableRows().get(trainData.getTimeTableRows().size() - 1);
            information.setArriveStationName(arriveRows.getStationShortCode());
            information.setArriveTime(arriveRows.getScheduledTime());
            trainInformation.add(information);
        }
        return trainInformation;
    }

    private String covertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private TimeTableRows getTimeTableRowsFromTrainData(TrainData trainData, String stationShortCode) {
        if (trainData == null) {
            return null;
        }
        List<TimeTableRows> timeTableRows = trainData.getTimeTableRows();
        for (TimeTableRows timeTableRow : timeTableRows) {
            if (timeTableRow.getStationShortCode().equals(stationShortCode)) {
                return timeTableRow;
            }
        }
        return null;
    }

    /**
     * Receives Event from message broker and handles that event.
     * 
     * @param event   The type of event to handle.
     * @param payload The payload associated with the event.
     * 
     * Note that BackendComponent.java catches any unexpected errors that might appear during this function.
     * Error handling for this function focuses more on handling known errors that might occur during the function.
     */
    @Override
    protected void handleEvent(EventType event, EventPayload payload) {
        if(event == Events.TrainRequestEvent.TOPIC && payload instanceof Events.TrainRequestEvent.Payload) {
            Events.TrainRequestEvent.Payload trainRequestPayload = getPayload(event, payload);
            List<TrainData> trainDataList = getTrainData(trainRequestPayload.getDepartingDate(), trainRequestPayload.getDepartureStationShortCode(), trainRequestPayload.getArrivalStationShortCode());
            List<TrainInformation> trainInformationList = getTrainInformation(trainDataList, trainRequestPayload.getDepartureStationShortCode());
            if (trainDataList != null) {
                broker.publish(Events.EventType.TRAIN_RESPONSE, new Events.TrainResponseEvent.Payload(trainInformationList));
            } else {

                Events.TrainResponseEvent.Payload errorPayload = new Events.TrainResponseEvent.Payload(null);
                errorPayload.setErrorMessage("No trains were found between " + trainRequestPayload.getDepartureStationShortCode() + " and " + trainRequestPayload.getArrivalStationShortCode() + " on " + covertDateToString(trainRequestPayload.getDepartingDate()));
                broker.publish(Events.EventType.TRAIN_RESPONSE, errorPayload);
            }
        }
    }

    @Override
    public void initialize() {
        broker.subscribe(EventType.TRAIN_REQUEST, this);
    }

    @Override
    public void shutdown() {
        broker.unsubscribe(EventType.TRAIN_REQUEST, this);
    }

}
