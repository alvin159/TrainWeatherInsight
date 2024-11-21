package compse110.backend.SearhStationComponent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The AbbrevConvert class provides functionality to convert station short codes
 * to their full names by fetching data from an external API and caching the results.
 * 
 * Usage example:
 * AbbrevConvert abbrevConvert = new AbbrevConvert();
 * String stationName = abbrevConvert.getStationFullName("HKI");
 */
public class AbbrevConvert {

    // API URL to fetch the station data
    private static final String STATION_DATA_URL = "https://rata.digitraffic.fi/api/v1/metadata/stations";

    // Cache structure: Map<stationShortCode, stationName>
    private final Map<String, String> stationCache = new HashMap<>();

    // Fetch the station name based on the short code, using cache if available
    /**
     * Retrieves the full name of a station given its short code.
     * 
     * @param stationShortCode The short code of the station.
     * @return String, The full name of the station, or null if the station name could not be found.
     * @throws Exception If there is an error fetching the station data from the API.
     */
    public String getStationFullName(String stationShortCode) throws Exception {
        // Check if the data is already in cache
        if (stationCache.containsKey(stationShortCode)) {
            //System.out.println("Using cached data for station: " + stationShortCode);
            return stationCache.get(stationShortCode);
        }

        // If not in cache, fetch the data from the API
        JsonArray stationData = fetchStationDataFromAPI();
        String stationName = findStationName(stationShortCode, stationData);

        // If found, cache the result
        if (stationName != null) {
            stationCache.put(stationShortCode, stationName);
        }
        return stationName != null ? stationName : null;
    }

    private JsonArray fetchStationDataFromAPI() throws Exception {
        URL url = new URI(STATION_DATA_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "gzip");

        InputStream inputStream = new GZIPInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the JSON response using Gson
        return JsonParser.parseString(response.toString()).getAsJsonArray();
    }

    private String findStationName(String stationShortCode, JsonArray stationData) {
        for (int i = 0; i < stationData.size(); i++) {
            JsonObject stationObject = stationData.get(i).getAsJsonObject();
            String shortCode = stationObject.get("stationShortCode").getAsString();
            if (shortCode.equals(stationShortCode)) {
                return stationObject.get("stationName").getAsString();
            }
        }
        return null;
    }
}