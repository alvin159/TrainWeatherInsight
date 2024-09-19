package compse110.backend;

import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class AbbrevConverterComponent implements MessageCallback {
    private static final MessageBroker broker = MessageBroker.getInstance();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // API URL to fetch the station data
    private static final String STATION_DATA_URL = "https://rata.digitraffic.fi/api/v1/metadata/stations";

    // Cache structure: Map<stationShortCode, stationName>
    private final Map<String, String> stationCache = new HashMap<>();

    @Override
    public void onMessageReceived(String topic, Object payload) {
        if (topic.equals("abbrevConverterRequest")) {
            String stationShortCode = (String) payload;
            System.out.println("Request received for station short code: " + stationShortCode);
            executorService.submit(() -> {
                try {
                    String stationName = getStationName(stationShortCode);
                    broker.publish("abbrevConverterResponse", stationName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Fetch the station name based on the short code, using cache if available
    private String getStationName(String stationShortCode) throws Exception {
        // Check if the data is already in cache
        if (stationCache.containsKey(stationShortCode)) {
            System.out.println("Using cached data for station: " + stationShortCode);
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

    private JSONArray fetchStationDataFromAPI() throws Exception {
        URL url = new URL(STATION_DATA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Set the necessary headers based on Postman values
        conn.setRequestProperty("User-Agent", "PostmanRuntime/7.42.0");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        conn.setRequestProperty("Connection", "keep-alive");

        // Check if the server returned the content encoded with GZIP
        String encoding = conn.getContentEncoding();
        InputStream inputStream;

        // If the content is GZIP encoded, use GZIPInputStream to decompress it
        if ("gzip".equalsIgnoreCase(encoding)) {
            inputStream = new GZIPInputStream(conn.getInputStream());
        } else {
            inputStream = conn.getInputStream();  // Handle uncompressed data
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        // Print the decompressed content for debugging
        //System.out.println("Decompressed JSON Response: " + content.toString());

        // Parse the decompressed content into a JSON array
        return new JSONArray(content.toString());
    }

    // Find the station name based on the short code in the fetched station data
    private String findStationName(String stationShortCode, JSONArray stationData) {
        for (int i = 0; i < stationData.length(); i++) {
            JSONObject station = stationData.getJSONObject(i);
            if (station.has("stationName") && station.has("stationShortCode")) {
                String shortCode = station.getString("stationShortCode");
                if (shortCode.equals(stationShortCode)) {
                    return station.getString("stationName");
                }
            }
        }
        return null; // Return null if station not found
    }

    public void initialize() {
        broker.subscribe("abbrevConverterRequest", this);
    }

    public void shutdown() {
        broker.unsubscribe("abbrevConverterRequest", this);
        executorService.shutdown();
    }
}
