package compse110.frontend;

import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Log;
import compse110.frontend.Entity.SearchInfo;
import compse110.frontend.Entity.UIComponentFactory;
import compse110.frontend.Entity.UIComponentFactory.StationSearchHandler;
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
import javafx.stage.WindowEvent;

public class HomePage extends Application implements MessageCallback{

    private static final MessageBroker broker = MessageBroker.getInstance();
    private DatePicker departureDatePicker;
    private UIComponentFactory uiComponentFactory;
    private StationSearchHandler stationSearchHandler;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TrainFinder");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        //Listen for responses from the backend
        broker.subscribe(EventType.ABBREVIATION_RESPONSE, this);
        broker.subscribe(EventType.SEARCH_STATION_RESPONSE, this);

        uiComponentFactory = new UIComponentFactory();
        stationSearchHandler = uiComponentFactory.new StationSearchHandler();

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
        Label arrivalStationLabel = new Label("Arrival station (optional):");

        // TODO if not debug remove it!!
        if (Log.getIsDebug()) {
            stationSearchHandler.departingStationField.setText("Oulu asema");
            broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(stationSearchHandler.departingStationField.getText(), stationSearchHandler.departingStationField.getId()));
            stationSearchHandler.arrivalStationField.setText("Helsinki asema");
            broker.publish(Events.SearchStationRequest.TOPIC, new Events.SearchStationRequest.Payload(stationSearchHandler.arrivalStationField.getText(), stationSearchHandler.arrivalStationField.getId()));
        }

        Label departureDateLabel = new Label("Departure date:\n(require arrive station)");
        departureDatePicker = UIComponentFactory.departureDatePickerCreator(LocalDate.now());
        
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #0BCAFF");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(stationSearchHandler.handlePressingSearch() == false) {
                    return;
                }

                // Create a message entity
                SearchInfo message = new SearchInfo();
                // Put data to message
                message.setDepartingStation(stationSearchHandler.departStation);
                message.setArrivingStation(stationSearchHandler.arriveStation);
                
                message.setDate(departureDatePicker.getValue());

                if (stationSearchHandler.arrivalStationField.getText().isEmpty()) {
                    message.setArrivingStation(null);
                }
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
        });

        // Label to display the response from the backend to user
        Label backendLabel = new Label();
        backendLabel.setPrefWidth(400); // Set the preferred width to 400 pixels
        backendLabel.setStyle("-fx-font-weight: bold;");

        VBox latestSearches = UIComponentFactory.LatestSearches.createLatestSearchesComponent(stationSearchHandler.departingStationField, stationSearchHandler.arrivalStationField);

        // Create layout for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setVgap(30);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(welcomeLabel, 0, 1, 2, 1);
        gridPane.add(departingStationLabel, 0, 2);
        gridPane.add(stationSearchHandler.departingStationField, 1, 2);
        gridPane.add(arrivalStationLabel, 0, 3);
        gridPane.add(stationSearchHandler.arrivalStationField, 1, 3);
        gridPane.add(departureDateLabel, 0, 4);
        gridPane.add(departureDatePicker, 1, 4);
        gridPane.add(latestSearches, 1, 5);
        gridPane.add(searchButton, 1, 6);
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

    @Override
    public void onMessageReceived(EventType eventType, EventPayload payload) {
        if (eventType == Events.SearchStationResponse.TOPIC && payload instanceof Events.SearchStationResponse.Payload) {
            Events.SearchStationResponse.Payload responsePayload = (Events.SearchStationResponse.Payload) payload;
            stationSearchHandler.handleStationSearchResponse(responsePayload);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}