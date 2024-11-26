package compse110.frontend.Entity;

import java.time.LocalDate;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import compse110.Entity.Station;
import compse110.Utils.Events;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The UIComponentFactory class provides factory pattern for creating reusable UI elements.
 * DatePicker is for creating date picker component to select the trip date.
 * Nested class LatestSearches is for creating latest searches component.
 * Nested class StationSearchHandler is for handling station search requests and responses.

 */
public class UIComponentFactory {
    private static final MessageBroker broker = MessageBroker.getInstance();
    
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

    public static class LatestSearches{
        public static VBox createLatestSearchesComponent(TextField departingStationField, TextField arrivingStationField) {
            VBox vBox = new VBox(7);
            Label label = new Label("Latest searches");
            label.setStyle("fx-font-weight: bold;'");
            vBox.getChildren().add(label);
        
            List<String> latestSearches = getLatestSearches();
            if(latestSearches.isEmpty()) {
                vBox.getChildren().clear();
                return vBox;
            }
            for (String search : latestSearches) {
                String[] stations = search.split(":");
                if (stations.length != 2) continue;
                Button searchText = new Button(stations[0] + " -> " + stations[1]);
                searchText.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 3 7;");

                searchText.setOnMouseClicked(event -> {
                    departingStationField.setText(stations[0]);
                    broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(stations[0], departingStationField.getId()));
                    if(stations[1].equals("Anywhere")) {
                        arrivingStationField.setText("");
                    }
                    else {
                        arrivingStationField.setText(stations[1]);
                        broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(stations[1], arrivingStationField.getId()));
                    }
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

    public class StationSearchHandler{
        public TextField departingStationField = new TextField();
        public Station departStation = null;
        public TextField arrivalStationField = new TextField();
        public Station arriveStation = null;
        private ContextMenu contextMenu = new ContextMenu();

        public StationSearchHandler() {
            departingStationField.setId("departingStationField"); //Set the ID for identification of the drop-down box below
            arrivalStationField.setId("arrivalStationField");
            
            departingStationField.setOnKeyReleased(this::handleTypingOnStationSearch);
            departingStationField.setOnMousePressed(this::handleFocusingOnTextField);
            arrivalStationField.setOnKeyReleased(this::handleTypingOnStationSearch);
            arrivalStationField.setOnMousePressed(this::handleFocusingOnTextField);
        }

        private void handleTypingOnStationSearch(KeyEvent event) {
            TextField source = (TextField) event.getSource();
            String newValue = source.getText();
            if (newValue.length() < 2) {
                contextMenu.hide();  // Hide recommendation list when inputting less than 2 characters
            } else {
                broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(newValue, source.getId()));
            }
        }

        private void handleFocusingOnTextField(MouseEvent event) {
            TextField source = (TextField) event.getSource();
            if (source.getText().length() < 2) {
                contextMenu.hide();  // Hide recommendation list when inputting less than 2 characters
            } else if (!contextMenu.isShowing()) { // Don't send request if the context menu is already showing
                broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(source.getText(), source.getId()));
            }
        }

        // Method to handle station search response and update the context menu
        public void handleStationSearchResponse(Events.SearchStationResponse.Payload responsePayload) {
            List<Station> filteredStations = responsePayload.getStationList();
            String textFieldId = responsePayload.getTextFieldId();

            if(textFieldId.equals("departingStationField")) {
                departStation = null;
            } else if (textFieldId.equals("arrivalStationField")) {
                arriveStation = null;
            }

            Platform.runLater(() -> {
                Log.i("Filtered stations: " + filteredStations.size());

                if (filteredStations.isEmpty()) {
                    contextMenu.hide();
                    return;
                }

                contextMenu.getItems().clear();
                for (Station station : filteredStations) {

                    //Ensures that station will be set if user has typed the full name of the station, but not clicked that station from the drop-down list
                    if(departingStationField.getText().equals(station.getStationName() ) && textFieldId.equals("departingStationField")) {
                        departStation = station;
                        break;
                    } else if ( arrivalStationField.getText().equals(station.getStationName()) && textFieldId.equals("arrivalStationField")) {
                        arriveStation = station;
                        break;
                    }

                    MenuItem item = new MenuItem(station.getStationName()); // set results
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (textFieldId.equals("departingStationField")) {
                                departStation = station;
                            } else {
                                arriveStation = station;
                            }
                            TextField textField = textFieldId.equals("departingStationField") ? departingStationField : arrivalStationField;
                            textField.setText(station.getStationName());
                            contextMenu.hide();
                        }
                    });
                    contextMenu.getItems().add(item);
                }

                if (!contextMenu.isShowing()) {
                    // Use localToScreen to get the absolute position of the TextField on the screen
                    TextField textField = textFieldId.equals("departingStationField") ? departingStationField : arrivalStationField;
                    double screenX = textField.localToScreen(textField.getBoundsInLocal()).getMinX();
                    double screenY = textField.localToScreen(textField.getBoundsInLocal()).getMaxY();

                    // Show ContextMenu below TextField
                    contextMenu.show(textField, screenX, screenY);
                }
            });
        }
    
        public boolean handlePressingSearch() {
            // Check departing Station Field is empty
            if (departingStationField.getText().isEmpty() || departStation == null) {
                // Show input alert
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Input departing station");
                alert.setContentText("Please input departing station name or short code");
                alert.showAndWait();
                return false;

            } else if (departingStationField.getText().equals(arrivalStationField.getText())) {
                // Check Departing and arrive station is same?
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Departing station and arrive station can not same name");
                alert.setContentText("Please input departing station name or short code again");
                alert.showAndWait();
                return false;
            }

            UIComponentFactory.LatestSearches.addSearchToLatestSearches(departingStationField, arrivalStationField);
            return true;
        }
    }
}
