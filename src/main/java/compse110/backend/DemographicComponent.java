package compse110.backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import compse110.Utils.EventPayload;
import compse110.Utils.Events.EventType;
import compse110.messagebroker.MessageCallback;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import compse110.Utils.Events.DemographicRequestEvent;
import compse110.Entity.*;
import compse110.messagebroker.MessageBroker;
import java.io.FileReader;
import java.io.IOException;

public class DemographicComponent implements MessageCallback {

    private static final MessageBroker broker = MessageBroker.getInstance();

    @Override
    public void onMessageReceived(EventType event, EventPayload payload) {
        if (event == EventType.DEMOGRAPHIC_REQUEST && payload instanceof DemographicRequestEvent) {
            // TODO: Implement the logic to fetch demographic data
        }
    }

    private void fetchDemographicData(String cityName) throws IOException {

        try {
            FileReader reader = new FileReader("src/main/resources/data/demographic.json");
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject areaLabelObj = jsonObject.getAsJsonObject("dimension")
                    .getAsJsonObject("Alue")
                    .getAsJsonObject("category")
                    .getAsJsonObject("label");

            String cityCode = null;
            for (String key : areaLabelObj.keySet()) {
                if (areaLabelObj.get(key).getAsString().equals(cityName)) {
                    cityCode = key;
                    break;
                }
            }

            if (cityCode != null) {
                JsonObject areaIndexObj = jsonObject.getAsJsonObject("dimension")
                        .getAsJsonObject("Alue")
                        .getAsJsonObject("category")
                        .getAsJsonObject("index");

                int index = areaIndexObj.get(cityCode).getAsInt();

                JsonArray values = jsonObject.getAsJsonArray("value");

                int population = values.get(index * 4).getAsInt();           // Population
                double landArea = values.get(index * 4 + 2).getAsDouble();  // Land Area
                double populationDensity = values.get(index * 4 + 3).getAsDouble(); // Population Density

                DemographicResponse response = new DemographicResponse(population, landArea, populationDensity);
                sendDemographicResponse(response);

                System.out.println("City: " + cityName);
                System.out.println("Population: " + population);
                System.out.println("Land Area: " + landArea);
                System.out.println("Population Density: " + populationDensity);

            } else {
                System.out.println("City not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDemographicResponse(DemographicResponse response) {
        broker.publish(EventType.DEMOGRAPHIC_RESPONSE, response);
        System.out.println("Published DEMOGRAPHIC_RESPONSE event with payload: " + response);
    }

    public void initialize() {
        broker.subscribe(EventType.DEMOGRAPHIC_REQUEST, this);
    }

    public void shutdown() {
        broker.unsubscribe(EventType.DEMOGRAPHIC_REQUEST, this);
    }
}

