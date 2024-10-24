package compse110.frontend;

import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.WeatherRequestEvent;
import compse110.Entity.Station;
import compse110.Entity.WeatherRequest;
import compse110.Utils.Log;
import compse110.frontend.Entity.SearchInfo;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;

public class HomePage extends Application implements MessageCallback{

    private static final MessageBroker broker = MessageBroker.getInstance();
    private Label backendLabel;
    private TextField departingStationField;
    private TextField arrivalStationField;
    private ContextMenu contextMenu;
    private Station departStation;
    private Station arriveStation;
    private DatePicker departureDatePicker;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TrainFinder");

        //Listen for responses from the backend
        broker.subscribe(EventType.ABBREVIATION_RESPONSE, this);
        broker.subscribe(EventType.SEARCH_STATION_RESPONSE, this);

        // Create UI elements
        Label titleLabel = new Label("TrainFinder");
        titleLabel.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 50px;");
        titleLabel.setPrefSize(200, 200); // Set background size to 200x200
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER); // Center the text inside the label

        // Wrap the titleLabel in a VBox to center it horizontally
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER); // Center VBox contents

        Label welcomeLabel = new Label("Welcome to search any train connections and information about your destination");
        Label departingStationLabel = new Label("Departing station:");
        departingStationField = new TextField();
        departingStationField.setPromptText("Oulu"); // Set hint text
        departingStationField.setId("departingStationField"); //Set the ID for identification of the drop-down box below
        Label arrivalStationLabel = new Label("Arrival station (optional):");
        arrivalStationField = new TextField();
        arrivalStationField.setPromptText("Helsinki"); // Set hint text
        arrivalStationField.setId("arrivalStationField");

        contextMenu = new ContextMenu();
        departingStationField.setOnKeyReleased(this::handleTypingOnStationSearch);
        departingStationField.setOnMousePressed(this::handleFocusingOnTextField);
        arrivalStationField.setOnKeyReleased(this::handleTypingOnStationSearch);
        arrivalStationField.setOnMousePressed(this::handleFocusingOnTextField);

        Label departureDateLabel = new Label("Departure date:");
        departureDatePicker = new DatePicker();
        departureDatePicker.setValue(LocalDate.now()); // Set default value to today

        // Disable past dates and limit to 14 days in the DatePicker
        departureDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusDays(14); // Calculate the maximum date (14 days from today)

                // Disable dates that are empty, before today, or after maxDate
                setDisable(empty || date.isBefore(today) || date.isAfter(maxDate));
            }
        });


        CheckBox showCoolFactsCheckBox = new CheckBox("Show cool facts about cities");
        
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #0BCAFF");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Check departing Station Field is empty
                if (departingStationField.getText().isEmpty()) {
                    // Show input alert
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alert");
                    alert.setHeaderText("Input departing station");
                    alert.setContentText("Please input departing station name or short code");
                    alert.showAndWait();

                } else if (departingStationField.getText().equals(arrivalStationField.getText())) {
                    // Check Departing and arrive station is same?
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alert");
                    alert.setHeaderText("Departing station and arrive station can not same name");
                    alert.setContentText("Please input departing station name or short code again");
                    alert.showAndWait();
                } else {
                    // Create a message entity
                    SearchInfo message = new SearchInfo();
                    // Put data to message
                    message.setDepartingStation(departStation);
                    message.setArrivingStation(arriveStation);
                    
                    sendWeatherRequest(departStation.getStationName().trim().split("\\s+")[0]);
                    sendWeatherRequest(arriveStation.getStationName().trim().split("\\s+")[0]);

                    message.setDate(departureDatePicker.getValue());
                    message.setShowCoolFacts(showCoolFactsCheckBox.isSelected());
                    // Open new window
                    InformationPage infoPage = new InformationPage(); // Create an instance of InformationPage
                    Stage infoStage = new Stage(); // Create a new Stage (window)
                    try {
                        infoPage.start(infoStage, message); // Call the start method of InformationPage to display it
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Close the current HomePage window
                    primaryStage.close();
                }

            }
        });

        // Label to display the response from the backend to user
        backendLabel = new Label();
        backendLabel.setPrefWidth(400); // Set the preferred width to 400 pixels
        backendLabel.setStyle("-fx-font-weight: bold;");

        // Create layout for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setVgap(30);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(welcomeLabel, 0, 1, 2, 1);
        gridPane.add(departingStationLabel, 0, 2);
        gridPane.add(departingStationField, 1, 2);
        gridPane.add(arrivalStationLabel, 0, 3);
        gridPane.add(arrivalStationField, 1, 3);
        gridPane.add(departureDateLabel, 0, 4);
        gridPane.add(departureDatePicker, 1, 4);
        gridPane.add(showCoolFactsCheckBox, 0, 5);
        gridPane.add(searchButton, 1, 5);
        gridPane.add(backendLabel, 1, 7);

        // Create the main layout with a BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleBox); // Add the titleBox at the top
        mainLayout.setCenter(gridPane); // Add the input fields in the center

        // Create scene and stage with full screen and centered content
        primaryStage.setScene(new Scene(mainLayout));

        primaryStage.setMaximized(true); // Maximize the stage for full screen
        primaryStage.show();
    }

    private void sendWeatherRequest(String stationName) {
        // Get the selected date from the DatePicker
        LocalDate selectedDate = departureDatePicker.getValue(); // Ensure this variable is accessible
    
        // Create a WeatherRequest object with the selected date and the station name
        WeatherRequest weatherRequest = new WeatherRequest(selectedDate, stationName);
    
        // Create the WeatherRequestEvent.Payload
        WeatherRequestEvent.Payload weatherPayload = new WeatherRequestEvent.Payload(weatherRequest);
    
        // Publish the weather request event through the MessageBroker
        broker.publish(EventType.WEATHER_REQUEST, weatherPayload);
        System.out.print("Fetching weather for " + stationName + " on " + selectedDate);
        // Optionally, update the UI to inform the user that weather data is being fetched
        Platform.runLater(() -> backendLabel.setText("Fetching weather for " + stationName + " on " + selectedDate));
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
    private void handleStationSearch(Events.SearchStationResponse.Payload responsePayload) {
        List<Station> filteredStations = responsePayload.getStationList();
        String textFieldId = responsePayload.getTextFieldId();
        Platform.runLater(() -> {
            Log.i("Filtered stations: " + filteredStations.size());

            if (!filteredStations.isEmpty()) {
                contextMenu.getItems().clear();
                for (Station station : filteredStations) {
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
            } else {
                contextMenu.hide();  // Hide ContextMenu when no match
            }
        });
    }

    @Override
    public void onMessageReceived(EventType eventType, EventPayload payload) {
        if (eventType == Events.SearchStationResponse.TOPIC && payload instanceof Events.SearchStationResponse.Payload) {
            Events.SearchStationResponse.Payload responsePayload = (Events.SearchStationResponse.Payload) payload;
            handleStationSearch(responsePayload);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}