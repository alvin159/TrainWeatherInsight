package compse110.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import compse110.Entity.TrainData;
import compse110.Utils.API_Config;
import compse110.messagebroker.MessageCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class TrainComponent implements MessageCallback {
    @Override
    public void onMessageReceived(String topic, Object payload) {

    }

    public List<TrainData> getTrainData(String departureStationShortCode) {
        return getTrainData(departureStationShortCode, null);
    }

    public List<TrainData> getTrainData(String departureStationShortCode, String arrivalStationShortCode) {

        //create a OkHttp client
        OkHttpClient client = new OkHttpClient();
        //create a request
        Request request = new Request.Builder()
                .url(API_Config.TRAIN_DATA_URL + departureStationShortCode + (arrivalStationShortCode != null ? "/" + arrivalStationShortCode : ""))
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
}
