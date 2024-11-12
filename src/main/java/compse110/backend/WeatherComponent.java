package compse110.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.WeatherRequestEvent;
import compse110.Entity.*;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import compse110.backend.utils.BackendComponent;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherComponent extends BackendComponent{

    private static final String API_KEY = "XRDPDDZZDR7SX9EMEYPPS9CTK";
    private static final MessageBroker broker = MessageBroker.getInstance();
    private static final OkHttpClient client = new OkHttpClient();

    @Override
    public void handleEvent(Events.EventType event, EventPayload payload) {
        if(event == EventType.WEATHER_REQUEST && payload instanceof WeatherRequestEvent.Payload) {
            try {
                Events.WeatherRequestEvent.Payload weatherRequestPayload = getPayload(event, payload);
                String stationName = weatherRequestPayload.getWeatherRequest().getStationName();
            
                // Format the LocalDate to a String
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust format as needed
                String weatherDate = weatherRequestPayload.getWeatherRequest().getDate().format(formatter);
                Double longitude = weatherRequestPayload.getWeatherRequest().getLongitude();
                Double latitude = weatherRequestPayload.getWeatherRequest().getLatitude();
                String weatherJSONdata = fetchWeatherData(weatherDate, latitude, longitude);
                WeatherResponse weatherResponse = parseWeatherData(weatherJSONdata, latitude, longitude);
                broker.publish(EventType.WEATHER_RESPONSE, new Events.WeatherResponseEvent.Payload(weatherResponse, stationName) );
            
            } catch (IOException e) {
                Log.e("WeatherComponent", "Error fetching weather data for the city: " + e.getMessage());
            }
        }
    }

    private String fetchWeatherData(String date, Double latitude, Double longitude) throws IOException {
        // Build the Weather API URL using the city name
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                     + latitude + "," + longitude + "/" + date + "?key=" + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Make the API call
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse the response
            String responseData = response.body().string();
            Log.d("WeatherComponent", responseData);
            return responseData;
        }
    }

    private WeatherResponse parseWeatherData(String jsonResponse, Double latitude, Double longitude) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Get the current time in Finland (UTC+3)
        LocalDateTime currentFinnishTime = LocalDateTime.now(ZoneId.of("Europe/Helsinki"));

        // Extract weather data from the current hour's forecast
        JsonObject currentConditions = jsonObject
                .getAsJsonArray("days")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("hours")
                .get(currentFinnishTime.getHour())
                .getAsJsonObject();


        JsonArray first24HoursData = new JsonArray();

        JsonArray hoursArray = jsonObject
                .getAsJsonArray("days")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("hours");
        
        for (int i = 0; i < 24; i++) {
            JsonObject hourlyData = hoursArray.get(i).getAsJsonObject();
            double first24HoursTemperature = hourlyData.get("temp").getAsDouble();
            String first24HoursWeatherCondition = hourlyData.get("conditions").getAsString();
            String first24HoursWeatherIcon = hourlyData.get("icon").getAsString();
        
            // Create a new JsonObject for this hour's data
            JsonObject hourData = new JsonObject();
            hourData.addProperty("temperature", first24HoursTemperature);
            hourData.addProperty("conditions", first24HoursWeatherCondition);
            hourData.addProperty("icon", first24HoursWeatherIcon);
        
            // Add this hour's data to the first24HoursData array
            first24HoursData.add(hourData);
        }

        // Extract temperature, conditions, and weather icon
        double temperature = currentConditions.get("temp").getAsDouble();
        String weatherCondition = currentConditions.get("conditions").getAsString();
        String weatherIcon = currentConditions.get("icon").getAsString();

        // Create a WeatherResponse and send it to the MessageBroker
        WeatherResponse weatherResponse = new WeatherResponse(temperature, weatherCondition, weatherIcon, first24HoursData);
        return weatherResponse;
    }

    public void initialize() {
        broker.subscribe(EventType.WEATHER_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.WEATHER_REQUEST, this);
    }
}
