package compse110.backend.SearhStationComponent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import compse110.Entity.Station;
import compse110.Entity.TimeTableRows;
import compse110.Utils.API_Config;
import compse110.Utils.Log;
import compse110.backend.Entity.StationStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The StationInfoFetcher class is responsible for fetching and caching station information.
 * It uses a singleton pattern to ensure only one instance of the class is created.
 * The class fetches station data from an API and caches it in a local file for a 7 days.
 * It also provides methods to get Station class by its short code.
 */
public class StationInfoFetcher {

    private static StationInfoFetcher instance;

    private static final String TAG = "StationInfoFetcher"; // for debugging

    private static final int CACHE_MAX_DAY = 7; // 7 days

    private static final String cacheFilePath = "Cache/station.json";

    private static final int MAX_STATION_RECOMMENDER_COUNT = 15;

    private List<Station> stations;

    public static StationInfoFetcher getInstance() {
        if (instance == null) {
            instance = new StationInfoFetcher();
        }
        return instance;
    }

    private StationInfoFetcher() {
        // private constructor to hide the implicit public one
        stations = getStationsFromFile();
        if (stations == null) {
            stations = refreshCache();
        }
    }

    private List<Station> getStationsFromFile() {
        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            try (FileReader reader = new FileReader(cacheFile)) {
                StationStorage storage = new Gson().fromJson(reader, StationStorage.class);
                if (storage != null && storage.getCacheTime() != null) {
                    long diff = new Date().getTime() - storage.getCacheTime().getTime();
                    if (diff < CACHE_MAX_DAY * 24 * 60 * 60 * 1000) { // check if the cache is expired
                        Log.d(TAG, "has cache");
                        return storage.getStations();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(TAG, "Failed to read cache file may be no cache");
                return null;
            }
        }
        return null;
    }


    private List<Station> refreshCache() {
        List<Station> stations = fetchStationDataFromAPI();
        if (stations != null) {
            StationStorage storage = new StationStorage(new Date(), stations);

            File cacheFile = new File(cacheFilePath);

            try {
                if (!cacheFile.exists()) {
                    File parentDir = cacheFile.getParentFile(); // get parent directory
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs(); // create parent directory
                        Log.i(TAG, "create cache directory");
                    }
                    cacheFile.createNewFile(); // create new file
                }

                // write to file
                try (FileWriter writer = new FileWriter(cacheFile)) {
                    new Gson().toJson(storage, writer);
                    Log.i(TAG, "save cache to file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "refresh cache");
        return stations;
    }

    private static List<Station> fetchStationDataFromAPI() {
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

    public List<Station> searchStations(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return this.stations.stream()
                .filter(station -> station.getStationName().toLowerCase().contains(lowerCaseQuery)
                        || station.getStationShortCode().toLowerCase().contains(lowerCaseQuery))
                .limit(MAX_STATION_RECOMMENDER_COUNT)
                .collect(Collectors.toList());
    }

    public Station getStationByShortCode(String shortCode) {
        return this.stations.stream()
                .filter(station -> station.getStationShortCode().equals(shortCode))
                .findFirst()
                .orElse(null);
    }

    public List<TimeTableRows> addTimeTableRowsStationName(List<TimeTableRows> timeTableRows) {
        List<TimeTableRows> result = new ArrayList<>();
        for (TimeTableRows row : timeTableRows) {
            row.setStationName(getStationByShortCode(row.getStationShortCode()).getStationName());
            result.add(row);
        }
        return result;
    }
}
