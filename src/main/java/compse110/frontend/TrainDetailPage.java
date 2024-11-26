package compse110.frontend;

import compse110.Entity.TimeTableRows;
import compse110.Entity.TrainInformation;
import compse110.Utils.EventPayload;
import compse110.Utils.Events;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The TrainDetailPage class is responsible for displaying detailed information about a train's journey.
 * It initializes the window with train details such as departure and arrival times, stops, and travel duration.
 * The window is opened from InformationPage and has a close button to return to it.
 */
public class TrainDetailPage extends Application {

    public void start(Stage primaryStage, TrainInformation trainInformation) {

        if (trainInformation == null) {
            return;
        }
        MessageBroker broker = MessageBroker.getInstance();
        List<TimeTableRows> stops = trainInformation.getTimeTableRows();
        List<TimeTableRows> mergedStops = mergeStops(stops);

        primaryStage.setTitle(trainInformation.getTrainName());

        broker.subscribe(Events.EventType.ADD_STATION_NAME_RESPONSE, new MessageCallback() {
            @Override
            public void onMessageReceived(Events.EventType event, EventPayload payload) {
                Events.AddStationNameResponse.Payload stationNamePayload = (Events.AddStationNameResponse.Payload) payload;
                Platform.runLater(() -> initView(trainInformation, stationNamePayload.getTimeTableRows(), primaryStage));
                broker.unsubscribe(Events.EventType.ADD_STATION_NAME_RESPONSE, this);
            }
        });

        broker.publish(Events.EventType.ADD_STATION_NAME_REQUEST, new Events.AddStationNameRequest.Payload(mergedStops));

    }

    /**
     * Initialize the window with train details such as departure and arrival times, stops, and travel duration.
     * @param trainInformation train information.
     * @param mergedStops List<TimeTableRows> merged stops, raw data is every station has two records, need merge first.
     * @param primaryStage the stage of the window.
     */
    private void initView(TrainInformation trainInformation, List<TimeTableRows> mergedStops, Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        VBox topBox = new VBox(5);
        topBox.setStyle("-fx-padding: 10;");

        String departureText = trainInformation.getTrainName() + "\nDeparture " + trainInformation.getDepartureStationName() + "  " + timeFormat.format(trainInformation.getDepartureTime());
        Label departInfo = new Label(departureText);
        departInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");


        String trainDetails = "Travel time: " + getFormatDurationTime(trainInformation.getDuration()) + "\n"
                + (mergedStops.size() - 1) + " stops";
        Label trainInfo = new Label(trainDetails);
        trainInfo.setStyle("-fx-font-size: 14px;");

        topBox.getChildren().addAll(departInfo, trainInfo);

        VBox stopsBox = new VBox(5);
        stopsBox.setStyle("-fx-padding: 10;");

        for (TimeTableRows stop : mergedStops) {
            String stopText = "";
            if (stop.getStationName() != null) {
                stopText += stop.getStationName();
            } else {
                stopText += stop.getStationShortCode();
            }
            stopText += ", " + timeFormat.format(stop.getScheduledTime());

            if (stop.getLiveEstimateTime() != null) {
                stopText += " -> " + timeFormat.format(stop.getLiveEstimateTime());
                stopText += " (stop " + stop.getDifferenceInMinutes() + " min)";
            }

            TextFlow stopDetail = createStopDetail(stopText, stop.isCancelled());
            stopsBox.getChildren().add(stopDetail);
        }

        ScrollPane scrollPane = new ScrollPane(stopsBox);
        scrollPane.setFitToWidth(true); // Scroll content width to fit windows
        scrollPane.setStyle("-fx-background: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5;");

        VBox bottomBox = new VBox(5);
        bottomBox.setStyle("-fx-padding: 10;");

        String arrivalText = "Arrives " + trainInformation.getArriveStationName() + "  " +timeFormat.format(trainInformation.getArriveTime());
        Label arrivalInfo = new Label(arrivalText);
        arrivalInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        // Add close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());

        bottomBox.getChildren().add(arrivalInfo);
        bottomBox.getChildren().add(closeButton);

        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setMinWidth(400); // Set window minimum width
        primaryStage.setMinHeight(600); // Set the minimum height of the window
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    /**
     * Create a TextFlow object for each stop.
     * @param stopInfo stop information.
     * @param isCancelled whether the stop is canceled.
     * @return TextFlow object.
     */
    private TextFlow createStopDetail(String stopInfo, boolean isCancelled) {
        Text stopText = new Text(stopInfo);
        stopText.setStyle(isCancelled ? "-fx-fill: red;" : "-fx-fill: black;");

        return new TextFlow(stopText);
    }

    /**
     * Start the window. No any use but need to implement.
     * @param primaryStage the stage of the window.
     * @throws Exception exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    /**
     * Format the duration time.
     * @param duration duration time.
     * @return formatted duration time.
     */
    private String getFormatDurationTime(long duration) {
        long minutes = duration / (60 * 1000);
        if (minutes > 60) {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }

    }

    /**
     * Merge the stops. The raw data is every station has two records, need to merge first.
     * @param stops List<TimeTableRows> stops.
     * @return List<TimeTableRows> merged stops.
     */
    private List<TimeTableRows> mergeStops(List<TimeTableRows> stops) {

        Map<String, List<TimeTableRows>> groupedStops = stops.stream()
                .filter(TimeTableRows::isTrainStopping) // Filter out non-parking sites
                .collect(Collectors.groupingBy(TimeTableRows::getStationShortCode));

        List<TimeTableRows> mergedStops = new ArrayList<>();

        for (Map.Entry<String, List<TimeTableRows>> entry : groupedStops.entrySet()) {
            String stationShortCode = entry.getKey();
            List<TimeTableRows> rows = entry.getValue();

            // Make sure to sort by time
            rows.sort(Comparator.comparing(TimeTableRows::getScheduledTime));

            // create new TimeTableRows
            TimeTableRows mergedRow = new TimeTableRows();
            mergedRow.setStationShortCode(stationShortCode);
            mergedRow.setScheduledTime(rows.get(0).getScheduledTime()); // Arrival time
            if (rows.size() > 1) {
                mergedRow.setLiveEstimateTime(rows.get(rows.size() - 1).getScheduledTime()); // Departure time
            }
            mergedRow.setTrainStopping(true); // Guaranteed to be a parking site
            mergedRow.setCancelled(rows.stream().anyMatch(TimeTableRows::isCancelled));

            // Check train is late
            if (mergedRow.getLiveEstimateTime() != null &&
                    mergedRow.getLiveEstimateTime().after(mergedRow.getScheduledTime())) {
                mergedRow.setDifferenceInMinutes((int) ((mergedRow.getLiveEstimateTime().getTime() - mergedRow.getScheduledTime().getTime()) / 60000));
            } else {
                mergedRow.setDifferenceInMinutes(0);
            }

            mergedStops.add(mergedRow);
        }

        mergedStops.sort(Comparator.comparing(TimeTableRows::getScheduledTime));
        return mergedStops;
    }

}
