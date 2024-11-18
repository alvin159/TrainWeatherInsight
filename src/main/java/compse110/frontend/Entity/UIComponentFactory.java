package compse110.frontend.Entity;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIComponentFactory {
    public static DatePicker departureDatePickerCreator(LocalDate date) {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(date);
        datePicker.setDayCellFactory(lambdaDatePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusDays(14); // Calculate the maximum date (14 days from today)

                // Disable dates that are empty, before today, or after maxDate
                setDisable(empty || date.isBefore(today) || date.isAfter(maxDate));
            }
        });
        return datePicker;
    }

    public class LatestSearches{
        public static VBox createLatestSearchesComponent(TextField departingStationField, TextField arrivingStationField) {
            VBox vBox = new VBox();
            Label label = new Label("Latest searches");
            vBox.getChildren().add(label);
        
            List<String> latestSearches = getLatestSearches();
            if(latestSearches.isEmpty()) {
                vBox.getChildren().clear();
                return vBox;
            }
            for (String search : latestSearches) {
                String[] stations = search.split(":");
                if (stations.length != 2) continue;
                Text searchText = new Text(stations[0] + " -> " + stations[1]);

                searchText.setOnMouseClicked(event -> {
                    departingStationField.setText(stations[0]);
                    arrivingStationField.setText(stations[1].equals("Anywhere") ? "" : stations[1]);
                });
                vBox.getChildren().add(searchText);
            }
        
            return vBox;
        }
        
        public static List<String> getLatestSearches() {
            List<String> searches = new ArrayList<>();
            try (FileReader reader = new FileReader("src/main/resources/data/latest_searches.json")) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                jsonObject.getAsJsonArray("searches").forEach(jsonElement -> searches.add(jsonElement.getAsString()));
            } catch (FileNotFoundException e) {
                // File doesn't exist, so no searches have been made yet
                // This is normal behavior, so we will just return empty list so that the program can continue
                return searches;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return searches;
        }

        private static void saveLatestSearches(List<String> latestSearches) {
            JsonObject jsonObject = new JsonObject();
            JsonArray searchesArray = new JsonArray();
            latestSearches.forEach(search -> searchesArray.add(search));
            jsonObject.add("searches", searchesArray);
            // FileWriter will create the file if it doesn't exist and overwrite it if it does
            // So no need to verify here if it exists or not
            try (FileWriter file = new FileWriter("src/main/resources/data/latest_searches.json")) {
                file.write(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void addSearchToLatestSearches(TextField departingStationField, TextField arrivingStationField) {
            String departingStation = departingStationField.getText();
            String arrivingStation = arrivingStationField.getText();
            if(!departingStation.isEmpty()) {
                String arrivingStationToSave  = arrivingStation.isEmpty() ? "Anywhere" : arrivingStation;
                String stringToFormat = departingStation + ":" + arrivingStationToSave;
                List<String> latestSearches = UIComponentFactory.LatestSearches.getLatestSearches();
                if (latestSearches.contains(stringToFormat)) {
                    latestSearches.remove(stringToFormat);
                }
                latestSearches.add(0, stringToFormat);
                while (latestSearches.size() > 5) {
                    latestSearches.remove(latestSearches.size() - 1);
                }
                saveLatestSearches(latestSearches);
            }
        }
    }
}
