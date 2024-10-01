package compse110.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import compse110.Utils.API_Config;
import compse110.Entity.Station;
import compse110.Entity.Events.EventType;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AbbrevConverterComponent implements MessageCallback {
    private static final MessageBroker broker = MessageBroker.getInstance();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Cache structure: Map<stationShortCode, stationName>
    //private final Map<String, String> stationCache = new HashMap<>();

    // New Cache structure：Map<String, AbbreviationObject>
    Map<String, AbbreviationObject> stationCache = new HashMap<>();

    // TODO: Update this function to handle the new Events
    @Override
    public void onMessageReceived(EventType event, Object payload) {
        if (event == EventType.ABBREVIATION_REQUEST && payload instanceof AbbreviationObject) {
            AbbreviationObject abbreviationObject = (AbbreviationObject) payload;
            String stationShortCode = abbreviationObject.getStationShortCodeRequest();
            //System.out.println("Request received for station short code: " + stationShortCode);
            executorService.submit(() -> {
                try {
                    // Fetch data from the cache or call the API to retrieve it
                    AbbreviationObject cachedObject = getStationFromCacheOrAPI(stationShortCode);

                    // Set the station name retrieved from the cache or API into the request object
                    abbreviationObject.setStationNameResponse(cachedObject.getStationResponse());

                    // publish AbbreviationObject response
                    // TODO: Illegal response, see what the response has to be from Events.AbbreviationResponse.Payload
                    broker.publish(EventType.ABBREVIATION_RESPONSE, abbreviationObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
/*
    // Fetch the station name based on the short code, using cache if available
    private String getStationName(String stationShortCode) throws Exception {
        // Check if the data is already in cache
        if (stationCache.containsKey(stationShortCode)) {
            //System.out.println("Using cached data for station: " + stationShortCode);
            return stationCache.get(stationShortCode);
        }

        // If not in cache, fetch the data from the API
        JSONArray stationData = fetchStationDataFromAPI();
        String stationName = findStationName(stationShortCode, stationData);

        // If found, cache the result
        if (stationName != null) {
            stationCache.put(stationShortCode, stationName);
        }
        return stationName != null ? stationName : "Station Not Found";
    }
*/


    private AbbreviationObject getStationFromCacheOrAPI(String stationShortCode) throws Exception {
        // Check if the cache already contains the AbbreviationObject corresponding to the stationShortCode
        if (stationCache.containsKey(stationShortCode)) {
            System.out.println("Using cached data for station: " + stationShortCode);
            return stationCache.get(stationShortCode);  // return directly from cache
        }

        // If not in cache, fetch the data from the API
        Map<String, Station> stationData = fetchStationDataFromAPI();
        Station station = findStationByName(stationShortCode, stationData);


        // create new AbbreviationObject and restore it in cache
        AbbreviationObject abbrevObject = new AbbreviationObject(stationShortCode);
        abbrevObject.setStationNameResponse(station);
        stationCache.put(stationShortCode, abbrevObject);  // restore AbbreviationObject into cache
        return abbrevObject;
    }


//    private JSONArray fetchStationDataFromAPI() throws Exception {
//        URL url = new URL(API_Config.TRAIN_STATION_URL);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//
//        // Set the necessary headers based on Postman values
//        conn.setRequestProperty("User-Agent", "PostmanRuntime/7.42.0");
//        conn.setRequestProperty("Accept", "*/*");
//        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
//        conn.setRequestProperty("Connection", "keep-alive");
//
//        // Check if the server returned the content encoded with GZIP
//        String encoding = conn.getContentEncoding();
//        InputStream inputStream;
//
//        // If the content is GZIP encoded, use GZIPInputStream to decompress it
//        if ("gzip".equalsIgnoreCase(encoding)) {
//            inputStream = new GZIPInputStream(conn.getInputStream());
//        } else {
//            inputStream = conn.getInputStream();  // Handle uncompressed data
//        }
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//
//        in.close();
//        conn.disconnect();
//
//        // Print the decompressed content for debugging
//        //System.out.println("Decompressed JSON Response: " + content.toString());
//
//        // Parse the decompressed content into a JSON array
//        return new JSONArray(content.toString());
//    }

    public Map<String, Station> fetchStationDataFromAPI() {
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
            List<Station> stations = new Gson().fromJson(response.body().string(), new TypeToken<List<Station>>() {}.getType());
            // Create a Map to save station information
            Map<String, Station> stationMap = new HashMap<>();
            for (Station station : stations) {
                stationMap.put(station.getStationShortCode(), station);
            }
            return stationMap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Find the station name based on the short code in the fetched station data
//    private String findStationName(String stationShortCode, JSONArray stationData) {
//        for (int i = 0; i < stationData.length(); i++) {
//            JSONObject station = stationData.getJSONObject(i);
//            if (station.has("stationName") && station.has("stationShortCode")) {
//                String shortCode = station.getString("stationShortCode");
//                if (shortCode.equals(stationShortCode)) {
//                    return station.getString("stationName");
//                }
//            }
//        }
//        return null; // Return null if station not found
//    }

    private Station findStationByName(String stationShortCode, Map<String, Station> stationMap) {
        return stationMap.get(stationShortCode);
    }

    public void initialize() {
        broker.subscribe(EventType.ABBREVIATION_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.ABBREVIATION_REQUEST, this);
        executorService.shutdown();
    }
}
