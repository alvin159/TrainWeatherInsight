package compse110.frontend;

import compse110.Entity.*;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    GridPane gridpane;

    private Stage primaryStage;

    // Main method to start with SearchInfo object
    public void start(Stage primaryStage, SearchInfo message) {
        primaryStage.setTitle("Information Page");

        broker.subscribe(EventType.TRAIN_RESPONSE, this);
        broker.subscribe(EventType.WEATHER_RESPONSE, this);
        broker.subscribe(EventType.DEMOGRAPHIC_RESPONSE, this);

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
        broker.publish(
            Events.TrainRequestEvent.TOPIC, 
            new Events.TrainRequestEvent.Payload(
                Date.from(message.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                message.getDepartingStation().getStationShortCode(),
                message.getArrivingStation() != null ? message.getArrivingStation().getStationShortCode() : null
            )
        );

        trainScheduleBox.getChildren().add(addLoadingView());

        // clear children view first
        root.getChildren().clear();
        root.getChildren().add(backToHomePageButton);
        // Adding all sections to the root layout
        root.getChildren().add(addHeaderView());

        broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getDepartingStation().getLongitude(), message.getDepartingStation().getLatitude(), message.getDepartingStation().getStationName())));
        broker.publish(EventType.DEMOGRAPHIC_REQUEST, new Events.DemographicRequestEvent.Payload(new DemographicRequest(message.getDepartingStation().getCityName(), message.getDepartingStation().getStationName())));
        // Add static city details and initialize placeholder departing city information view
        CityDetails cityDetails = new CityDetails(200000, 15231.3, 192.3);
        departingCityInfo = new CityInformation(0, message.getDepartingStation().getStationName(), weatherForecast, cityDetails);

        gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        
        departCityInfoView = addCityInformationView(departingCityInfo);

        gridpane.add(departCityInfoView, 0, 0);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        gridpane.getColumnConstraints().add(column);
        GridPane.setHgrow(departCityInfoView, Priority.ALWAYS);
        gridpane.add(addTrainScheduleTitleLine(),0,1);
        gridpane.add(trainScheduleBox, 0, 2);

        if (message.getArrivingStation() != null) {
            // if no any arriving city will not show this part
            broker.publish(EventType.WEATHER_REQUEST, new WeatherRequestEvent.Payload(new WeatherRequest(message.getDate() ,message.getArrivingStation().getLongitude(), message.getArrivingStation().getLatitude(), message.getArrivingStation().getStationName())));
            broker.publish(EventType.DEMOGRAPHIC_REQUEST, new Events.DemographicRequestEvent.Payload(new DemographicRequest(message.getArrivingStation().getCityName(), message.getArrivingStation().getStationName())));
            CityDetails cityDetails1 = new CityDetails(100300, 21521.3, 215.2);
            arrivingCityInfo = new CityInformation(0, message.getArrivingStation().getStationName(), weatherForecast, cityDetails1);
            arriveCityInfoView = addCityInformationView(arrivingCityInfo);
            gridpane.add(arriveCityInfoView, 0, 3);
        }
        root.getChildren().add(gridpane);
    }

    // Function to fetch Icons
    private ImageView createWeatherIcon(String iconName) {
        String iconPath = "/icon/" + iconName + ".png";
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        return new ImageView(icon);
    }

    private void updateCityWeather(CityInformation arrivalOrDepartingCityInfo, VBox arrivalOrDepartingInfoView, Forecast weatherForecast) {
        arrivalOrDepartingCityInfo.setForecast(weatherForecast);
        Integer rowIndex = GridPane.getRowIndex(arrivalOrDepartingInfoView);
        Integer columnIndex = GridPane.getColumnIndex(arrivalOrDepartingInfoView);
        gridpane.getChildren().remove(arrivalOrDepartingInfoView);
        arrivalOrDepartingInfoView = addCityInformationView(arrivalOrDepartingCityInfo);
        gridpane.add(arrivalOrDepartingInfoView, columnIndex != null ? columnIndex : 0, rowIndex != null ? rowIndex : 0);
    }

    private void updateCityDetails(CityInformation arrivalOrDepartingCityInfo, VBox arrivalOrDepartingInfoView, CityDetails cityDetails) {
        arrivalOrDepartingCityInfo.setCityDetails(cityDetails);
        Integer rowIndex = GridPane.getRowIndex(arrivalOrDepartingInfoView);
        Integer columnIndex = GridPane.getColumnIndex(arrivalOrDepartingInfoView);
        gridpane.getChildren().remove(arrivalOrDepartingInfoView);
        arrivalOrDepartingInfoView = addCityInformationView(arrivalOrDepartingCityInfo);
        gridpane.add(arrivalOrDepartingInfoView, columnIndex != null ? columnIndex : 0, rowIndex != null ? rowIndex : 0);
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
        DatePicker departureDatePicker = UIComponentFactory.departureDatePickerCreator(message.getDate());

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
                    message.setDate(departureDatePicker.getValue());
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
                createWeatherIcon("partly-cloudy-night"),
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
        VBox weatherBox = new VBox(cityInformation.getForecast().getWeatherImage(),
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

    private void showTemperatureGraph(JsonArray response, String name) {
        Stage stage = new Stage();
        stage.setTitle("Temperature Graph in " + name );

        // Set up the X (index) and Y (temperature in Celsius) axes
        final NumberAxis xAxis = new NumberAxis(1, 23, 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hours");
        yAxis.setLabel("Temperature (°C)");

        // Create the LineChart and data series
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Temperature over time");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Temperature");

        // Spinner for interval input (values from 1 to 6)
        Spinner<Integer> intervalSpinner = new Spinner<>();
        intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6, 1));

        intervalSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.equals(6) && oldValue.equals(6)) intervalSpinner.getValueFactory().setValue(1);
            if (newValue.equals(1) && oldValue.equals(1)) intervalSpinner.getValueFactory().setValue(6);
        });

        // Button to update the graph
        Button updateButton = new Button("Update Graph");

        updateButton.setOnAction(e -> {
            // Clear previous data points
            series.getData().clear();

            // Get user-selected interval from the Spinner
            int interval = intervalSpinner.getValue();

            // Limit to a maximum of 6 data points displayed
            for (int i = 0; i < response.size(); i += interval) {
                JsonObject entry = response.get(i).getAsJsonObject();
                double tempFahrenheit = entry.get("temperature").getAsDouble();
                double tempCelsius = (tempFahrenheit - 32) * 5 / 9;
                series.getData().add(new XYChart.Data<>(i, tempCelsius));
            }
        });

        // Add initial data with default interval (1)
        updateButton.fire();

        // Layout
        VBox inputBox = new VBox(5, intervalSpinner, updateButton);
        BorderPane root = new BorderPane();
        root.setTop(inputBox);
        root.setCenter(lineChart);

        lineChart.getData().add(series);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
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
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (weatherResponse.getWeatherResponse() == null) {
                    } else {
                        WeatherResponse response = weatherResponse.getWeatherResponse();
                        System.out.println("Received information for " + weatherResponse.getStationName() + ": " + response.toString());
                        Forecast forecast = new Forecast(
                            (response.getTemperature() - 32) * 5 / 9,
                            response.getWeatherCondition(),
                            createWeatherIcon(response.getWeatherIcon()),
                            new ForecastDetails()
                        );
            
                        if(weatherResponse.getStationName().equals(message.getDepartingStation().getStationName())) {
                            System.out.println(response.getHours());
                            showTemperatureGraph(response.getHours(), message.getDepartingStation().getStationName());
                            departingCityInfo.setForecast(forecast);
                            updateCityWeather(departingCityInfo, departCityInfoView, forecast);
                        } else {
                            arrivingCityInfo.setForecast(forecast);
                            showTemperatureGraph(response.getHours(), message.getArrivingStation().getStationName());
                            updateCityWeather(arrivingCityInfo, arriveCityInfoView, forecast);
                        }

                    }
                }
            });
        }else if (event == EventType.DEMOGRAPHIC_RESPONSE && payload instanceof Events.DemographicResponseEvent.Payload) {
            Events.DemographicResponseEvent.Payload demographicResponse = (Events.DemographicResponseEvent.Payload) payload;
            System.out.println(demographicResponse);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (demographicResponse.getDemographicResponse() == null) {
                    } else {
                        DemographicResponse response = demographicResponse.getDemographicResponse();
                        System.out.println("Received information: " + response.toString());
                        CityDetails cityDetails = new CityDetails(response.getPopulation(), response.getLandArea(), response.getPopulationDensity());

                        if(demographicResponse.getStationName().equals(message.getDepartingStation().getStationName())) {
                            departingCityInfo.setCityDetails(cityDetails);
                            updateCityDetails(departingCityInfo,departCityInfoView,cityDetails);

                        } else {
                            arrivingCityInfo.setCityDetails(cityDetails);
                            updateCityDetails(arrivingCityInfo,arriveCityInfoView,cityDetails);
                        }
                    }
                }
            });
        }
    }

}
