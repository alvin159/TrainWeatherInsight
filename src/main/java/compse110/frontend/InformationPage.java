package compse110.frontend;

import compse110.frontend.Entity.SearchInfo;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InformationPage extends Application {

    private SearchInfo message;
    // Main method to start with SearchInfo object
    public void start(Stage primaryStage, SearchInfo message) {
        primaryStage.setTitle("Information Page");

        // throw Exception if message is null
        if (message == null) {
            primaryStage.close();
            return;
        } else {
            this.message = message;
            // debug
            System.out.println(message);
        }

        // Main layout container
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(20);

        // Departure Information Layout
        VBox departInfoBox = new VBox();
        departInfoBox.setPadding(new Insets(15));
        departInfoBox.setSpacing(10);
        departInfoBox.setStyle("-fx-background-color: lightblue;");
        Label departInfoHeader = new Label(message.getDepartingCity() + " information now");



        // Temperature, Weather, and Population Details
        HBox departInfoDetails = new HBox();
        departInfoDetails.setSpacing(30);
        departInfoDetails.setAlignment(Pos.CENTER);

        VBox temperatureBox = new VBox(new Label("+15"), new Label("Temperature"));
        temperatureBox.setAlignment(Pos.CENTER);

        // Placeholder for weather icon and condition (we can dynamically load this if needed)
        VBox weatherBox = new VBox(new ImageView(new Image("https://openweathermap.org/img/wn/10d@2x.png")), new Label("Partly cloudy"));
        weatherBox.setAlignment(Pos.CENTER);

        VBox populationBox = new VBox(new Label("200 000"), new Label("Population"));
        populationBox.setAlignment(Pos.CENTER);

        VBox areaBox = new VBox(new Label("15 000 km²"), new Label("Area"));
        areaBox.setAlignment(Pos.CENTER);

        VBox densityBox = new VBox(new Label("200"), new Label("Population Density"));
        densityBox.setAlignment(Pos.CENTER);

        departInfoDetails.getChildren().addAll(temperatureBox, weatherBox, populationBox, areaBox, densityBox);

        departInfoBox.getChildren().addAll(departInfoHeader, departInfoDetails);

        // Button for showing detailed forecast (expanded functionality)
        Button toggleForecastButton = new Button("See detailed forecast");
        toggleForecastButton.setOnAction(e -> {
            // Expand to show the detailed forecast information
        });
        departInfoBox.getChildren().add(toggleForecastButton);
        
        // Train Schedule Table (simplified for this example)
        VBox trainScheduleBox = new VBox();
        trainScheduleBox.setSpacing(10);
        trainScheduleBox.setStyle("-fx-background-color: #f0f0f0;");

        Label scheduleHeader = new Label();
        // TODO search user input
        if (message.getArrivingCity().isEmpty()) {
            scheduleHeader.setText(message.getDepartingCity() + " on track");
        } else {
            scheduleHeader.setText("Trains " + message.getDepartingCity() + " -> " + message.getArrivingCity());
        }

        // TODO change to list view to show

        HBox trainInfo = new HBox();
        trainInfo.setSpacing(50);
        trainInfo.setAlignment(Pos.CENTER);

        Label departureTime = new Label("12:00 " + message.getDepartingCity());
        Label duration = new Label("5 hrs 0 minutes");
        Label arrivalTime = new Label("17:00 " + message.getArrivingCity());

        trainInfo.getChildren().addAll(departureTime, duration, arrivalTime);
        trainScheduleBox.getChildren().addAll(scheduleHeader, trainInfo);

        // Display cool facts based on SearchInfo's isShowCoolFacts()
        Label coolFactsLabel = new Label();
        if (message.isShowCoolFacts()) {
            coolFactsLabel.setText("Cool facts about cities are enabled.");
        } else {
            coolFactsLabel.setText("No cool facts selected.");
        }
        coolFactsLabel.setStyle("-fx-font-size: 14px; -fx-padding: 20px;");


        // Adding all sections to the root layout
        root.getChildren().add(addheaderView());

        root.getChildren().addAll(coolFactsLabel, departInfoBox, trainScheduleBox);


        if (!message.getArrivingCity().isEmpty()) {// if no any arriving city will not show this part

            // Departure Information Layout
            VBox arriveInfoBox = new VBox();
            arriveInfoBox.setPadding(new Insets(15));
            arriveInfoBox.setSpacing(10);
            arriveInfoBox.setStyle("-fx-background-color: lightblue;");
            Label arriveInfoHeader = new Label(message.getArrivingCity());

            // Temperature, Weather, and Population Details
            HBox arriveInfoDetails = new HBox();
            arriveInfoDetails.setSpacing(30);
            arriveInfoDetails.setAlignment(Pos.CENTER);

            VBox arriveTemperatureBox = new VBox(new Label("+15"), new Label("Temperature"));
            temperatureBox.setAlignment(Pos.CENTER);

            // Placeholder for weather icon and condition (we can dynamically load this if needed)
            VBox arriveWeatherBox = new VBox(new ImageView(new Image("file:partly_cloudy.png")), new Label("Partly cloudy"));
            weatherBox.setAlignment(Pos.CENTER);

            VBox arrivePopulationBox = new VBox(new Label("200 000"), new Label("Population"));
            populationBox.setAlignment(Pos.CENTER);

            VBox arriveAreaBox = new VBox(new Label("15 000 km²"), new Label("Area"));
            areaBox.setAlignment(Pos.CENTER);

            VBox arriveDensityBox = new VBox(new Label("200"), new Label("Population Density"));
            densityBox.setAlignment(Pos.CENTER);

            arriveInfoDetails.getChildren().addAll(arriveTemperatureBox, arriveWeatherBox, arrivePopulationBox, arriveAreaBox, arriveDensityBox);

            arriveInfoBox.getChildren().addAll(arriveInfoHeader, arriveInfoDetails);

            // Button for showing detailed forecast (expanded functionality)
            Button togglearriveForecastButton = new Button("See detailed forecast");
            toggleForecastButton.setOnAction(e -> {
                // Expand to show the detailed forecast information
            });
            arriveInfoBox.getChildren().add(togglearriveForecastButton);

            // add view to root view
            root.getChildren().add(arriveInfoBox);

        }



        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Maximize the stage for full screen
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        // This method is required, but unused in this scenario
        // Leave it empty or redirect to the overloaded start method
    }

    private HBox addheaderView() {
        // Header layout for Departing and Arriving fields
        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label departingLabel = new Label("Departing station:");
        TextField departingField = new TextField();
        departingField.setText(message.getDepartingCity());  // Use departing city from SearchInfo

        Label arrivingLabel = new Label("Arrival station (optional):");
        TextField arrivingField = new TextField();
        arrivingField.setText(message.getArrivingCity());   // Use arriving city from SearchInfo

        Label dateLabel = new Label("Departure date:");
        TextField dateField = new TextField();
        dateField.setText(message.getDate().toString());               // Use date from SearchInfo

        // Search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //on Click
            }
        });

        header.getChildren().addAll(departingLabel, departingField, arrivingLabel, arrivingField, dateLabel, dateField, searchButton);

        return header;
    }

//    private void a
}
