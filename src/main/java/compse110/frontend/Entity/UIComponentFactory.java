package compse110.frontend.Entity;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.FileReader;
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

    public static VBox createLatestSearchesComponent(TextField departingStationField, TextField arrivingStationField) {
        VBox vBox = new VBox();
        Label label = new Label("Latest searches");
        vBox.getChildren().add(label);

        List<String> latestSearches = getLatestSearches();
        for (String search : latestSearches) {
            Text searchText = new Text(search);
            searchText.setOnMouseClicked(event -> {
                String[] stations = search.split(" to ");
                if (stations.length == 2) {
                    departingStationField.setText(stations[0]);
                    arrivingStationField.setText(stations[1]);
                }
            });
            vBox.getChildren().add(searchText);
        }

        return vBox;
    }

    private static List<String> getLatestSearches() {
        List<String> searches = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/data/latest_searches.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            jsonObject.getAsJsonArray("searches").forEach(jsonElement -> searches.add(jsonElement.getAsString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searches;
    }
}
