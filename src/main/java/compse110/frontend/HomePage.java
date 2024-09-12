package compse110.frontend;

import compse110.frontend.Entity.SearchInfo;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.application.Platform;

public class HomePage extends Application implements MessageCallback{
    private static final MessageBroker broker = MessageBroker.getInstance();
    private Label backendLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TrainFinder");

        //Listen for responses from the backend
        broker.subscribe("exampleRequest_response", this);

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
        TextField departingStationField = new TextField();
        departingStationField.setPromptText("Oulu"); // Set hint text
        Label arrivalStationLabel = new Label("Arrival station (optional):");
        TextField arrivalStationField = new TextField();
        arrivalStationField.setPromptText("Helsinki"); // Set hint text
        Label departureDateLabel = new Label("Departure date:");
        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setValue(LocalDate.now()); // Set default value to today

        // Disable past dates in the DatePicker
        departureDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
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
                    message.setDeparting(departingStationField.getText());
                    message.setArriving(arrivalStationField.getText());
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

        // Example button to test connection to backend
        Button testBackendButton = new Button("Test connection to backend");
        testBackendButton.setStyle("-fx-background-color: #0BCAFF");
        testBackendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendFetchTrainRequest();
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
        gridPane.add(testBackendButton, 1, 6);
        gridPane.add(backendLabel, 1, 7);

        // Create the main layout with a BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleBox); // Add the titleBox at the top
        mainLayout.setCenter(gridPane); // Add the input fields in the center

        // Create scene and stage with full screen and centered content
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.setWidth(700);
        primaryStage.setMinHeight(800);

        primaryStage.setMaximized(false); // Maximize the stage for full screen
        primaryStage.show();
    }

    public void sendFetchTrainRequest() {
        Platform.runLater(() -> backendLabel.setText("Sent request to backend...\nWaiting for response..."));
        broker.publish("exampleRequest", "Sample payload");
    }

    @Override
    public void onMessageReceived(String topic, Object payload) {
        if(topic.equals("exampleRequest_response")) {
            Platform.runLater(() -> backendLabel.setText("Received response from backend with a payload:\n" + payload));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}