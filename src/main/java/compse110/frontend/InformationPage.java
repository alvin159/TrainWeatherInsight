package compse110.frontend;

import compse110.Entity.Station;
import compse110.Entity.TrainInformation;
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
    private StationInfoFetcher stationInfoFetcher;
    ListView<TrainInformation> trainListView;
    VBox trainScheduleBox;
    Forecast weatherForecast;
    private VBox departCityInfoView;
    private VBox arriveCityInfoView;
    CityInformation departingCityInfo;
    CityInformation arrivingCityInfo;

    private Stage primaryStage;

    // Main method to start with SearchInfo object
    public void start(Stage primaryStage, SearchInfo message) {
        primaryStage.setTitle("Information Page");

        broker.subscribe(EventType.TRAIN_RESPONSE, this);
        broker.subscribe(EventType.WEATHER_RESPONSE, this);

        this.primaryStage = primaryStage;

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
                                        message.setDepartingStation(station);
                                    } else {
                                        message.setArrivingStation(station);
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

        Button backToHomePageButton = new Button("Back to Home Page");
        backToHomePageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    HomePage homePage = new HomePage();
                    Stage homeStage = new Stage();
                    homePage.start(homeStage);
                    primaryStage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        trainScheduleBox = new VBox();
        trainScheduleBox.setSpacing(10);
        trainScheduleBox.setStyle("-fx-background-color: #f0f0f0;");
        
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
        trainScheduleBox.getChildren().add(addLoadingView());

        // clear children view first
        root.getChildren().clear();
        root.getChildren().add(backToHomePageButton);
        // Adding all sections to the root layout
        root.getChildren().add(addHeaderView());

        // TODO this only demo data
        if (message.getArrivingStation() != null)
        //broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getDepartingStation().getStationName().trim().split("\\s+")[0])));
        broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getDepartingStation().getLongitude(), message.getDepartingStation().getLatitude())));
        // Add static city details and initialize placeholder departing city information view
        CityDetails cityDetails = new CityDetails(200000, 15231.3, 192.3);
        departingCityInfo = new CityInformation(0, message.getDepartingStation().getStationName(), weatherForecast, cityDetails);

        departCityInfoView = addCityInformationView(departingCityInfo);

        root.getChildren().add(departCityInfoView);
        root.getChildren().add(addTrainScheduleTitleLine());
        root.getChildren().add(trainScheduleBox);

        if (message.getArrivingStation() != null) {
            // if no any arriving city will not show this part
            broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getArrivingStation().getLongitude(), message.getDepartingStation().getLatitude())));
            
            CityDetails cityDetails1 = new CityDetails(100300, 21521.3, 215.2);
            arrivingCityInfo = new CityInformation(0, message.getArrivingStation().getStationName(), weatherForecast, cityDetails1);
            arriveCityInfoView = addCityInformationView(arrivingCityInfo);
            root.getChildren().add(arriveCityInfoView);
        }
        // Your other view components like train schedules or city information can be added here..

    }

    private void updateCityWeather(CityInformation arrivalOrDepartingCityInfo, VBox arrivalOrDepartingInfoView, Forecast weatherForecast) {
        arrivalOrDepartingCityInfo.setForecast(weatherForecast);
        arrivalOrDepartingInfoView.getChildren().clear();
        arrivalOrDepartingInfoView = addCityInformationView(departingCityInfo);
    }

    private HBox addLoadingView() {
        Label loadingLabel = new Label("Loading train timetables...");
        loadingLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        loadingLabel.setAlignment(Pos.CENTER);
        HBox loadingBox = new HBox(loadingLabel);
        loadingBox.setAlignment(Pos.CENTER);
        return loadingBox;
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
                } else {
                    if (arrivalStationField.getText().isEmpty()) {
                        message.setArrivingStation(null);
                    }
                    initView();
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

            //TODO fake data
            Forecast forecast = new Forecast(
                5,
                "Sunny",
                "https://openweathermap.org/img/wn/01d.png",
                new ForecastDetails()
            );
            cityInformation.setForecast(forecast);
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

    private GridPane addTrainScheduleTitleLine() {
        GridPane titleLine = new GridPane();
        titleLine.setAlignment(Pos.CENTER);

        for (int i = 0; i < 8; i++) {
            ColumnConstraints col = new ColumnConstraints(120);
            titleLine.getColumnConstraints().add(col);
        }

        Label trainNameLabel = new Label("Train Name");
        Label departureTrackLabel = new Label("Departure Track");
        Label departureTimeLabel = new Label("Departure Time");
        Label estimatedTimeLabel = new Label("Estimated Time");
        Label durationLabel = new Label("Duration");
        Label arrivalStationLabel = new Label("Arrival Station");
        Label arrivalTimeLabel = new Label("Arrival Time");
        Label arrivalTrackLabel = new Label("Arrival Track");
        Label weatherLabel = new Label("Weather");

        titleLine.add(trainNameLabel, 0, 0);
        titleLine.add(departureTrackLabel, 1, 0);
        titleLine.add(departureTimeLabel, 2, 0);
        titleLine.add(estimatedTimeLabel, 3, 0);
        titleLine.add(durationLabel, 4, 0);
        titleLine.add(arrivalStationLabel, 5, 0);
        titleLine.add(arrivalTimeLabel, 6, 0);
        titleLine.add(arrivalTrackLabel, 7, 0);
        titleLine.add(weatherLabel, 8, 0);

        return titleLine;
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
            // Train information update

            Events.TrainResponseEvent.Payload responseData = (Events.TrainResponseEvent.Payload) payload;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(responseData.getTrainInformationList() == null) {
                        // No any train show no train view
                        trainScheduleBox.getChildren().clear();
                        Label errorLabel = new Label(responseData.getErrorMessage());
                        errorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                        errorLabel.setAlignment(Pos.CENTER);
                        HBox errorBox = new HBox(errorLabel);
                        errorBox.setAlignment(Pos.CENTER);
                        trainScheduleBox.getChildren().add(errorBox);
                    } else {
                        // Load Schedule to train list
                        trainListView.getItems().clear();
                        trainListView.getItems().addAll(responseData.getTrainInformationList());
                        trainScheduleBox.getChildren().clear();
                        trainScheduleBox.getChildren().add(trainListView);
                    }
                }
            });

        } else if (event == EventType.WEATHER_RESPONSE && payload instanceof Events.WeatherResponseEvent.Payload) {
            Events.WeatherResponseEvent.Payload weatherResponse = (Events.WeatherResponseEvent.Payload) payload;
            // These are example getters and you can replace them
            weatherResponse.getWeatherResponse().getTemperature();
            weatherResponse.getWeatherResponse().getWeatherCondition();
            weatherResponse.getWeatherResponse().getWeatherIcon();
            System.out.println("Weather response received: " + weatherResponse.getWeatherResponse().getTemperature() + " " + weatherResponse.getWeatherResponse().getWeatherCondition() + " " + weatherResponse.getWeatherResponse().getWeatherIcon());
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (weatherResponse.getWeatherResponse() == null) {
                        // Print an error message when no weather data is available
                        System.err.println("Error: No weather data received.");
                    } else {
                        // Set the weatherForecast array with the weather data received
                        WeatherResponse response = weatherResponse.getWeatherResponse();
                        Forecast forecast = new Forecast(
                            response.getTemperature(),          // assuming getTemperature() returns temperature
                            response.getWeatherCondition(),     // assuming getWeatherCondition() returns condition
                            response.getWeatherIcon(),          // assuming getWeatherIcon() returns an icon URL
                            new ForecastDetails()               // pass any additional details required here
                        );
            
                        // Assign the forecast to weatherForecast (assuming it’s an array of Forecast)
                        //if(departingCityInfo != null) {
                        //    updateCityWeather(departingCityInfo, departCityInfoView, forecast);
                        //    System.out.println("Weather data updated successfully.");
                        //}
                    }
                }
            });            
        }
    }

}
