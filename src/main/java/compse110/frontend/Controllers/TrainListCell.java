package compse110.frontend.Controllers;

import compse110.Entity.TrainInformation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TrainListCell extends ListCell<TrainInformation> {
    private final GridPane gridPane;
    private final Label trainName;
    private final Label departureTime;
    private final Label estimatedTime;
    private final Label duration;
    private final Label departureTrack;
    private final Label arrivalStation;
    private final Label arrivalTrack;
    private final Label arrivalTime;
//    private final ImageView forecastImage;

    public TrainListCell() {
        // Create view
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        trainName = new Label();
        departureTime = new Label();
        duration = new Label();
        estimatedTime = new Label();
        arrivalStation = new Label();
        departureTrack = new Label();
        arrivalTrack = new Label();
        arrivalTime = new Label();
//        forecastImage = new ImageView();

//        forecastImage.setFitWidth(50);
//        forecastImage.setFitHeight(30);

        // Set fixed widths for each column
        for (int i = 0; i < 8; i++) {
            ColumnConstraints col = new ColumnConstraints(100);
            gridPane.getColumnConstraints().add(col);
        }

        // Add components to the grid pane
        gridPane.add(trainName, 0, 0);
        gridPane.add(departureTrack, 1, 0);
        gridPane.add(departureTime, 2, 0);
        gridPane.add(estimatedTime, 3, 0);
        gridPane.add(duration, 4, 0);
        gridPane.add(arrivalStation, 5, 0);
        gridPane.add(arrivalTime, 6, 0);
        gridPane.add(arrivalTrack, 7, 0);
//        gridPane.add(forecastImage, 8, 0);

        // Set alignment
        gridPane.setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(TrainInformation item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            trainName.setText(item.getTrainName());
            departureTime.setText(getFormatTime(item.getDepartureTime()));
            estimatedTime.setText(item.getEstimatedTime() != null ? getFormatTime(item.getEstimatedTime()) : "");
            duration.setText(getFormatDurationTime(item.getDuration()));
            departureTrack.setText("Track " + item.getDepartureTrack());
            arrivalTrack.setText("Track " + item.getArriveTrack());
            arrivalStation.setText(item.getArriveStationName());
            arrivalTime.setText(getFormatTime(item.getArriveTime()));
//            forecastImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/partly-cloudy-day.png"))));

            setGraphic(gridPane);
        } else {
            setGraphic(null);
        }
    }

    private String getFormatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
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