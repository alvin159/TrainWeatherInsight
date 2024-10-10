package compse110.backend.SearhStationComponent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import compse110.Entity.Station;
import compse110.Utils.API_Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class StationInfoFetcher {

    //TODO: fetch station data from API
    public static List<Station> fetchStationDataFromAPI() {
        //create a OkHttp client
        OkHttpClient client = new OkHttpClient();
        //create a request
        Request request = new Request.Builder()
                .url(API_Config.TRAIN_STATION_URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Handle the response
            // Use gson to transfer response to json
            return new Gson().fromJson(response.body().string(), new TypeToken<List<Station>>() {}.getType());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
