package compse110.backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.backend.utils.BackendComponent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import compse110.Utils.Events.DemographicRequestEvent;
import compse110.Entity.*;
import java.io.FileReader;
import java.io.IOException;
/**
 * Handles demographic data requests and responses in an event-driven architecture.
 *
 * <p>This class is responsible for:
 * <ul>
 *     <li>Receiving demographic data requests (e.g., city and station names).</li>
 *     <li>Fetching relevant demographic data (e.g., population, land area, density) from a JSON file.</li>
 *     <li>Publishing a response event with the requested demographic information or an error message if data is unavailable.</li>
 * </ul>
 *
 * <p>The class integrates with a message broker for subscribing to and publishing events, and
 * processes JSON data using the Gson library.
 * 
 * Listens for DEMOGRAPHIC_REQUEST events and responds with DEMOGRAPHIC_RESPONSE events.
 */
public class DemographicComponent extends BackendComponent {

    @Override
    protected void handleEvent(EventType event, EventPayload payload) {

        if (event == EventType.DEMOGRAPHIC_REQUEST && payload instanceof DemographicRequestEvent.Payload) {
            Events.DemographicRequestEvent.Payload demographicRequestPayload = getPayload(event, payload);
            String stationName = demographicRequestPayload.getDemographicRequest().getStationName();
            // Gets the city name by splitting station name where the space is
            String cityName = stationName.split("\\s+")[0];
            DemographicResponse response = fetchDemographicData(cityName);
            if ( response!= null) {
                broker.publish(EventType.DEMOGRAPHIC_RESPONSE, new Events.DemographicResponseEvent.Payload(response,stationName));
            } else {

                Events.DemographicResponseEvent.Payload errorPayload = new Events.DemographicResponseEvent.Payload(null,stationName);
                errorPayload.setErrorMessage("No demographic information found in " + cityName);
                broker.publish(EventType.DEMOGRAPHIC_RESPONSE, errorPayload);
            }
        }
    }

    private DemographicResponse fetchDemographicData(String cityName) {

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

                System.out.println("City: " + cityName);
                System.out.println("Population: " + population);
                System.out.println("Land Area: " + landArea);
                System.out.println("Population Density: " + populationDensity);
                return response;

            } else {
                System.out.println("City not found.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize() {
        broker.subscribe(EventType.DEMOGRAPHIC_REQUEST, this);
    }

    @Override
    public void shutdown() {
        broker.unsubscribe(EventType.DEMOGRAPHIC_REQUEST, this);
        executor.shutdown();
    }

}

