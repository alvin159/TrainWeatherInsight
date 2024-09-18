package compse110.frontend;

import compse110.frontend.Entity.*;
import compse110.frontend.Utils.StringUtils;
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
    private VBox root;


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
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(20);
        
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
        root.getChildren().add(addHeaderView());
        root.getChildren().add(coolFactsLabel);

        // TODO this only demo data
        Forecast forecast = new Forecast(18.2, "Cloudy", "https://openweathermap.org/img/wn/03d@2x.png", new ForecastDetails());
        CityDetails cityDetails = new CityDetails(200000, 15231.3, 192.3);

        CityInformation cityInformation = new CityInformation(0, message.getDepartingCity(), forecast, cityDetails);

        root.getChildren().add(addCityInformationView(cityInformation));
        root.getChildren().add(trainScheduleBox);

        if (!message.getArrivingCity().isEmpty()) {
            // if no any arriving city will not show this part

            // TODO this only demo data
            Forecast forecast1 = new Forecast(23.1, "clear sky", "https://openweathermap.org/img/wn/01d@2x.png", new ForecastDetails());
            CityDetails cityDetails1 = new CityDetails(100300, 21521.3, 215.2);

            CityInformation cityInformation1 = new CityInformation(0, message.getArrivingCity(), forecast1, cityDetails1);

            root.getChildren().add(addCityInformationView(cityInformation1));
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

    private HBox addHeaderView() {
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

    private VBox addCityInformationView(CityInformation cityInformation) {

        if (cityInformation == null) {
            return new VBox();
        }

        // Departure Information Layout
        VBox cityInfoBox = new VBox();
        cityInfoBox.setPadding(new Insets(15));
        cityInfoBox.setSpacing(10);
        cityInfoBox.setStyle("-fx-background-color: lightblue;");
        Label departInfoHeader = new Label(cityInformation.getTitle() + " information now");


        // Temperature, Weather, and Population Details
        HBox departInfoDetails = new HBox();
        departInfoDetails.setSpacing(30);
        departInfoDetails.setAlignment(Pos.CENTER);

        VBox temperatureBox = new VBox(new Label(String.format(StringUtils.celsius_data, cityInformation.getForecast().getTemperature())), new Label("Temperature"));
        temperatureBox.setAlignment(Pos.CENTER);

        // Placeholder for weather icon and condition (we can dynamically load this if needed)
        VBox weatherBox = new VBox(new ImageView(new Image(cityInformation.getForecast().getWeatherImageSrc())),
                new Label(cityInformation.getForecast().getWeatherStatus()));
        weatherBox.setAlignment(Pos.CENTER);

        VBox populationBox = new VBox(new Label(String.valueOf(cityInformation.getCityDetails().getPopulation())), new Label("Population"));
        populationBox.setAlignment(Pos.CENTER);

        VBox areaBox = new VBox(new Label(String.format(StringUtils.area_data, cityInformation.getCityDetails().getArea())), new Label("Area"));
        areaBox.setAlignment(Pos.CENTER);

        VBox densityBox = new VBox(new Label(String.valueOf(cityInformation.getCityDetails().getPopulationDensity())), new Label("Population Density"));
        densityBox.setAlignment(Pos.CENTER);

        departInfoDetails.getChildren().addAll(temperatureBox, weatherBox, populationBox, areaBox, densityBox);

        cityInfoBox.getChildren().addAll(departInfoHeader, departInfoDetails);

        // Button for showing detailed forecast (expanded functionality)
        Button toggleForecastButton = new Button("See detailed forecast");
        toggleForecastButton.setOnAction(e -> {
            // Expand to show the detailed forecast information
        });
        cityInfoBox.getChildren().add(toggleForecastButton);

        return cityInfoBox;

    }
}
