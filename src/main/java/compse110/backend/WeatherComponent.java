package compse110.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.WeatherRequestEvent;
import compse110.Entity.*;
import compse110.messagebroker.MessageBroker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import compse110.backend.utils.BackendComponent;

import java.io.IOException;
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
                String weatherName = weatherRequestPayload.getWeatherRequest().getCityName();
            
                // Format the LocalDate to a String
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust format as needed
                String weatherDate = weatherRequestPayload.getWeatherRequest().getDate().format(formatter);
            
                fetchWeatherData(weatherName, weatherDate);
            
            } catch (IOException e) {
                System.err.println("Error fetching weather data for the city: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void fetchWeatherData(String cityName, String date) throws IOException {
        // Build the Weather API URL using the city name
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                     + cityName + "/" + date + "?key=" + API_KEY;
        System.out.print(url);
                     // Create the request
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
            parseWeatherData(responseData, cityName);
        }
    }

    private void parseWeatherData(String jsonResponse, String cityName) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Get the current time in Finland (UTC+3)
        LocalDateTime currentFinnishTime = LocalDateTime.now(ZoneId.of("Europe/Helsinki"));

        // Extract weather data from the current hour's forecast
        JsonObject currentConditions = jsonObject
                .getAsJsonArray("days")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("hours")
                .get(currentFinnishTime.getHour())  // Get current hour in Finnish time
                .getAsJsonObject();

        // Extract temperature, conditions, and weather icon
        double temperature = currentConditions.get("temp").getAsDouble();
        String weatherCondition = currentConditions.get("conditions").getAsString();
        String weatherIcon = currentConditions.get("icon").getAsString();

        // Create a WeatherResponse and send it to the MessageBroker
        WeatherResponse WeatherResponse = new WeatherResponse(cityName, temperature, weatherCondition, weatherIcon);
        sendWeatherResponse(WeatherResponse);
    }

    private void sendWeatherResponse(WeatherResponse weatherResponse) {
        // Publish the weather data through the MessageBroker
        broker.publish(EventType.WEATHER_RESPONSE, new Events.WeatherResponseEvent.Payload(weatherResponse) );
    }

    public void initialize() {
        broker.subscribe(EventType.WEATHER_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.WEATHER_REQUEST, this);
    }
}
