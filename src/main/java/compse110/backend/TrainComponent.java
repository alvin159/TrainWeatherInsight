package compse110.backend;

import compse110.Entity.TimeTableRows;
import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import compse110.Entity.TrainData;
import compse110.Utils.API_Config;
import compse110.Utils.Log;
import compse110.frontend.Entity.TrainInformation;
import compse110.messagebroker.MessageCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainComponent implements MessageCallback {
    @Override
    public void onMessageReceived(EventType event, EventPayload payload) {

    }

    public List<TrainData> getTrainData(Date departingDate, String departureStationShortCode) {
        return getTrainData(departingDate, departureStationShortCode, null);
    }

    public List<TrainData> getTrainData(Date departingDate, String departureStationShortCode, String arrivalStationShortCode) {

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
            // Handle the response
            // Use gson to transfer response to json
            return new Gson().fromJson(response.body().string(), new TypeToken<List<TrainData>>() {}.getType());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<TrainInformation> getTrainInformation(Date departingDate, String departureStationShortCode) {
        return getTrainInformation(departingDate, departureStationShortCode, null);
    }

    public List<TrainInformation> getTrainInformation(Date departingDate, String departureStationShortCode, String arrivalStationShortCode) {

        List<TrainData> trainDatas = getTrainData(departingDate, departureStationShortCode, arrivalStationShortCode);

        if (trainDatas == null) {
            return new ArrayList<>();
        }
        List<TrainInformation> trainInformation = new ArrayList<>();
        for (int i = 0; i < trainDatas.size(); i++) {
            TrainData trainData = trainDatas.get(i);
            TrainInformation information = new TrainInformation();
            information.setId(i);
            information.setTrainName(trainData.getTrainType() + " " + trainData.getTrainNumber());
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

}
