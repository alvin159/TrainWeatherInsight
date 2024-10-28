package compse110.frontend;

import compse110.Entity.Station;
import compse110.Entity.WeatherRequest;
import compse110.Entity.WeatherResponse;
import compse110.frontend.Controllers.TrainListCell;
import compse110.backend.SearhStationComponent.StationInfoFetcher;
import compse110.frontend.Entity.*;
import compse110.Utils.StringUtils;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.Utils.Events.EventType;
import compse110.Utils.Events.WeatherRequestEvent;
import compse110.Utils.Log;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
public class InformationPage extends Application implements MessageCallback {

    private static final MessageBroker broker = MessageBroker.getInstance();
    private SearchInfo message;
    private VBox root;
    private Station departStation;
    private Station arriveStation;
    private StationInfoFetcher stationInfoFetcher;
    ListView<TrainInformation> trainListView;
    VBox trainScheduleBox;
    private VBox departCityInfoView;
    private VBox arriveCityInfoView;

    // Main method to start with SearchInfo object
    public void start(Stage primaryStage, SearchInfo message) {
        primaryStage.setTitle("Information Page");

        broker.subscribe(EventType.TRAIN_RESPONSE, this);
        broker.subscribe(EventType.WEATHER_RESPONSE, this);

        // throw Exception if message is null
        if (message == null) {
            primaryStage.close();
            return;
        } else {
            this.message = message;
            // debug
            System.out.println(message);
        }

        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(20);

        stationInfoFetcher = StationInfoFetcher.getInstance(); // Initialize station info fetcher

        initView();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    @Override
    public void start(Stage primaryStage) {
        // This method is required, but unused in this scenario
        // Leave it empty or redirect to the overloaded start method
    }

    private void addSearchStationRecommendListener(TextField textField, ContextMenu contextMenu) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() < 2) {
                    contextMenu.hide();  // Hide recommendation list when inputting less than 2 characters
                } else {
                    // Start showing recommendations when typing more than two characters
                    List<Station> filteredStations = stationInfoFetcher.searchStations(newValue);
                    Log.i("Filtered stations: " + filteredStations.size());

                    if (!filteredStations.isEmpty()) {
                        contextMenu.getItems().clear();
                        for (Station station : filteredStations) {
                            MenuItem item = new MenuItem(station.getStationName()); // set results
                            item.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    if (textField.getId().equals("departingStationField")) {
                                        departStation = station;
                                    } else {
                                        arriveStation = station;
                                    }
                                    textField.setText(station.getStationName());
                                    contextMenu.hide();
                                }
                            });
                            contextMenu.getItems().add(item);
                        }

                        if (!contextMenu.isShowing()) {
                            // Use localToScreen to get the absolute position of the TextField on the screen
                            double screenX = textField.localToScreen(textField.getBoundsInLocal()).getMinX();
                            double screenY = textField.localToScreen(textField.getBoundsInLocal()).getMaxY();

                            // Show ContextMenu below TextField
                            contextMenu.show(textField, screenX, screenY);
                        }
                    } else {
                        contextMenu.hide();  // Hide ContextMenu when no match
                    }
                }
            }
        });

        // When the TextField gets focus, display the ContextMenu
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    contextMenu.hide();  // Hide ContextMenu when TextField loses focus
                }
            }
        });
    }
    
    private void initView() {
        HBox header = addHeaderView(); // Add header for search functionality
        root.getChildren().clear(); // clear previous children
        root.getChildren().add(header); // Add the search fields

        trainScheduleBox = new VBox();
        trainScheduleBox.setSpacing(10);
        trainScheduleBox.setStyle("-fx-background-color: #f0f0f0;");

        Label coolFactsLabel = new Label();
        if (message.isShowCoolFacts()) {
            coolFactsLabel.setText("Cool facts about cities are enabled.");
        } else {
            coolFactsLabel.setText("No cool facts selected.");
        }
        root.getChildren().add(coolFactsLabel);
        
        // TODO change to list view to show
        trainListView = new ListView<>();
        trainListView.setCellFactory(new Callback<ListView<TrainInformation>, ListCell<TrainInformation>>() {
            @Override
            public ListCell<TrainInformation> call(ListView<TrainInformation> param) {
                return new TrainListCell();
            }
        });

        //add data
        broker.publish(Events.TrainRequestEvent.TOPIC, new Events.TrainRequestEvent.Payload(Date.from(message.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()), message.getDepartingStation().getStationShortCode(), message.getArrivingStation() != null ? message.getArrivingStation().getStationShortCode() : null));

//        HBox trainInfo = new HBox();
//        trainInfo.setSpacing(50);
//        trainInfo.setAlignment(Pos.CENTER);
//
//        Label departureTime = new Label("12:00 " + message.getDepartingStation().getStationName());
//        Label duration = new Label("5 hrs 0 minutes");
//        Label arrivalTime = new Label("17:00 " + message.getArrivingStation().getStationName());
//
//        trainInfo.getChildren().addAll(departureTime, duration, arrivalTime);



        // clear children view first
        root.getChildren().clear();
        // Adding all sections to the root layout
        root.getChildren().add(addHeaderView());
        root.getChildren().add(coolFactsLabel);

        // TODO this only demo data
        broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getDepartingStation().getStationName())));

        // Add static city details and initialize placeholder departing city information view
        CityDetails cityDetails = new CityDetails(200000, 15231.3, 192.3);
        CityInformation departingCityInfo = new CityInformation(0, message.getDepartingStation().getStationName(), null, cityDetails);
        departCityInfoView = addCityInformationView(departingCityInfo);
        root.getChildren().add(departCityInfoView);
        root.getChildren().add(trainScheduleBox);

        if (message.getArrivingStation() != null) {
            // if no any arriving city will not show this part

            broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getDepartingStation().getStationName())));
            
            CityDetails cityDetails1 = new CityDetails(100300, 21521.3, 215.2);
            CityInformation arrivingCityInfo = new CityInformation(0, message.getArrivingStation().getStationName(), null, cityDetails1);
            arriveCityInfoView = addCityInformationView(arrivingCityInfo);
            root.getChildren().add(arriveCityInfoView);
        }
        // Your other view components like train schedules or city information can be added here...
    }

    
    private VBox createCityInformationView(String loadingText) {
        VBox cityInfoBox = new VBox();
        cityInfoBox.setPadding(new Insets(15));
        cityInfoBox.setSpacing(10);
        cityInfoBox.setStyle("-fx-background-color: lightblue;");

        Label temperatureLabel = new Label(loadingText);
        temperatureLabel.setId("temperatureLabel");
        ImageView weatherIcon = new ImageView();
        weatherIcon.setId("weatherIcon");
        Label weatherConditionLabel = new Label();
        weatherConditionLabel.setId("weatherConditionLabel");

        cityInfoBox.getChildren().addAll(temperatureLabel, weatherIcon, weatherConditionLabel);
        return cityInfoBox;
    }

    private HBox addHeaderView() {
        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label departingLabel = new Label("Departing station:");
        TextField departingStationField = new TextField();
        departingStationField.setText(message.getDepartingStation().getStationName());
        departingStationField.setId("departingStationField");

        Label arrivingLabel = new Label("Arrival station (optional):");
        TextField arrivalStationField = new TextField();
        arrivalStationField.setText(message.getArrivingStation() != null ? message.getArrivingStation().getStationName() : "");
        arrivalStationField.setId("arrivalStationField");

        Label dateLabel = new Label("Departure date:");
        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setValue(message.getDate());

        departureDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        Button searchButton = new Button("Search");
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
                } 
            }
        });

        // Add listeners for auto-complete functionality
        ContextMenu contextMenu = new ContextMenu();
        addSearchStationRecommendListener(departingStationField, contextMenu);
        addSearchStationRecommendListener(arrivalStationField, contextMenu);

        header.getChildren().addAll(departingLabel, departingStationField, arrivingLabel, arrivalStationField, dateLabel, departureDatePicker, searchButton);
        return header;
    }

    private VBox addCityInformationView(CityInformation cityInformation) {

        if (cityInformation == null) {
            return new VBox();
        }

        // City Information Layout
        VBox cityInfoBox = new VBox();
        cityInfoBox.setPadding(new Insets(15));
        cityInfoBox.setSpacing(10);
        cityInfoBox.setStyle("-fx-background-color: lightblue;");
        Label departInfoHeader = new Label(cityInformation.getTitle() + " information now");


        // Temperature, Weather, and Population Details
        HBox departInfoDetails = new HBox();
        departInfoDetails.setSpacing(30);
        departInfoDetails.setAlignment(Pos.CENTER);

        if (cityInformation.getForecast() == null) {
            return cityInfoBox;
        }

        //TODO
        InfoBox temperatureBox = new InfoBox(
            String.format(StringUtils.celsius_data, cityInformation.getForecast().getTemperature()), 
            "Temperature"
        );

        // Placeholder for weather icon and condition (we can dynamically load this if needed)
        VBox weatherBox = new VBox(new ImageView(new Image(cityInformation.getForecast().getWeatherImageSrc())),
                new Label(cityInformation.getForecast().getWeatherStatus()));
        weatherBox.setAlignment(Pos.CENTER);


        InfoBox populationBox = new InfoBox(
            String.valueOf(cityInformation.getCityDetails().getPopulation()), 
            "Population"
        );
        
        InfoBox areaBox = new InfoBox(
            String.format(StringUtils.area_data, cityInformation.getCityDetails().getArea()), 
            "Area"
        );

        InfoBox densityBox = new InfoBox(
            String.valueOf(cityInformation.getCityDetails().getPopulationDensity()), 
            "Population\n  Density"
        );

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

    private void updateCityInformationView(VBox cityInfoBox, WeatherResponse weatherResponse) {
        Forecast forecast = new Forecast(
            weatherResponse.getTemperature(),
            weatherResponse.getWeatherCondition(),
            "https://openweathermap.org/img/wn/" + weatherResponse.getWeatherIcon() + "@2x.png",
            new ForecastDetails()
        );
    
        // Update UI components (assuming cityInfoBox has elements like temperature label, weather icon, etc.)
        ((Label) cityInfoBox.lookup("#temperatureLabel")).setText(forecast.getTemperature() + "°C");
        ((ImageView) cityInfoBox.lookup("#weatherIcon")).setImage(new Image(forecast.getWeatherImageSrc()));
        ((Label) cityInfoBox.lookup("#weatherConditionLabel")).setText(forecast.getWeatherStatus());
    }
    

    /**
     * Receives Events from message broker.
     *
     * @param event   the type of the event received
     * @param payload the payload of the event
     */
    @Override
    public void onMessageReceived(EventType event, EventPayload payload) {
        if (event == Events.TrainResponseEvent.TOPIC && payload instanceof Events.TrainResponseEvent.Payload) {
            Events.TrainResponseEvent.Payload responsePayload = (Events.TrainResponseEvent.Payload) payload;
            //TODO: Handle error if no trains were found
            Platform.runLater(() -> {
                trainListView.getItems().clear();
                trainListView.getItems().addAll(responsePayload.getTrainInformationList());
                trainScheduleBox.getChildren().add(trainListView);
            });
        }
        else if (event == Events.TrainResponseEvent.TOPIC && payload instanceof Events.TrainResponseEvent.Payload) {
            Events.TrainResponseEvent.Payload responsePayload = (Events.TrainResponseEvent.Payload) payload;
            Platform.runLater(() -> {

                trainListView.getItems().clear();
                trainListView.getItems().addAll(responsePayload.getTrainInformationList());
                trainScheduleBox.getChildren().add(trainListView);

            });
        } else if (event == EventType.WEATHER_RESPONSE && payload instanceof WeatherResponse) {
            WeatherResponse weatherResponse = (WeatherResponse) payload;
            Platform.runLater(() -> {
                // Update the view for departing or arriving city based on the response
                if (weatherResponse.getCityName().equals(message.getDepartingStation().getStationName())) {
                    updateCityInformationView(departCityInfoView, weatherResponse);
                } else if (message.getArrivingStation() != null &&
                           weatherResponse.getCityName().equals(message.getArrivingStation().getStationName())) {
                    updateCityInformationView(arriveCityInfoView, weatherResponse);
                }
            });
        }
    }

}
