package compse110.frontend;

import compse110.Entity.TimeTableRows;
import compse110.Entity.TrainInformation;
import javafx.application.Application;
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
import java.util.List;

public class TrainDetailPage extends Application {

    public void start(Stage primaryStage, TrainInformation trainInformation) {

        if (trainInformation == null) {
            return;
        }
        List<TimeTableRows> stops = trainInformation.getTimeTableRows();

        primaryStage.setTitle(trainInformation.getTrainName());

        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        VBox topBox = new VBox(5);
        topBox.setStyle("-fx-padding: 10;");

        String departureText = trainInformation.getTrainName() + "\nDeparture " + timeFormat.format(trainInformation.getDepartureTime());
        Label departInfo = new Label(departureText);
        departInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        int stopCount = 0;
        for (TimeTableRows stop : stops) {
            if (stop.isTrainStopping()) {
                stopCount++;
            }
        }
        String trainDetails = "Travel time: " + getFormatDurationTime(trainInformation.getDuration()) + "\n"
                + stopCount + " stops";
        Label trainInfo = new Label(trainDetails);
        trainInfo.setStyle("-fx-font-size: 14px;");

        topBox.getChildren().addAll(departInfo, trainInfo);

        VBox stopsBox = new VBox(5);
        stopsBox.setStyle("-fx-padding: 10;");

        for (TimeTableRows stop : stops) {
            if (!stop.isTrainStopping()) continue; // Filter non-stop sites

            String stopText = stop.getStationShortCode() + ", "
                    + timeFormat.format(stop.getScheduledTime());
            if (stop.getLiveEstimateTime() != null) {
                stopText += " (Est: " + timeFormat.format(stop.getLiveEstimateTime()) + ")";
            }
            TextFlow stopDetail = createStopDetail(stopText, stop.isCancelled());
            stopsBox.getChildren().add(stopDetail);
        }

        ScrollPane scrollPane = new ScrollPane(stopsBox);
        scrollPane.setFitToWidth(true); // Scroll content width to fit windows
        scrollPane.setStyle("-fx-background: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5;");

        VBox bottomBox = new VBox(5);
        bottomBox.setStyle("-fx-padding: 10;");

        String arrivalText = "Arrives " + timeFormat.format(trainInformation.getArriveTime())
                + " " + trainInformation.getArriveStationName();
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

        Scene scene = new Scene(root, 400, 800);
        primaryStage.setMinWidth(400); // Set window minimum width
        primaryStage.setMinHeight(600); // Set the minimum height of the window
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();

    }

    private TextFlow createStopDetail(String stopInfo, boolean isCancelled) {
        Text stopText = new Text(stopInfo);
        stopText.setStyle(isCancelled ? "-fx-fill: red;" : "-fx-fill: black;");

        return new TextFlow(stopText);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

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
}
