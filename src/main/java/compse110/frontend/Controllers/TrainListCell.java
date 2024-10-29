package compse110.frontend.Controllers;

import compse110.frontend.Entity.TrainInformation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainListCell extends ListCell<TrainInformation> {
    private final GridPane gridPane;
    private final Label trainName;
    private final Label departureTime;
    private final Label estimatedTime;
    private final Label duration;
    private final Label track;
    private final Label arrivalTime;
    private final ImageView forecastImage;

    public TrainListCell() {
        // Create view
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        trainName = new Label();
        departureTime = new Label();
        duration = new Label();
        estimatedTime = new Label();
        track = new Label();
        arrivalTime = new Label();
        forecastImage = new ImageView();

        forecastImage.setFitWidth(40);
        forecastImage.setFitHeight(40);

        // Set fixed widths for each column
        ColumnConstraints col1 = new ColumnConstraints(100);
        ColumnConstraints col2 = new ColumnConstraints(100);
        ColumnConstraints col3 = new ColumnConstraints(100);
        ColumnConstraints col4 = new ColumnConstraints(100);
        ColumnConstraints col5 = new ColumnConstraints(100);
        ColumnConstraints col6 = new ColumnConstraints(100);
        ColumnConstraints col7 = new ColumnConstraints(100);

        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6, col7);

        // Add components to the grid pane
        gridPane.add(trainName, 0, 0);
        gridPane.add(departureTime, 1, 0);
        gridPane.add(estimatedTime, 2, 0);
        gridPane.add(duration, 3, 0);
        gridPane.add(track, 4, 0);
        gridPane.add(arrivalTime, 5, 0);
        gridPane.add(forecastImage, 6, 0);

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
            duration.setText(item.getDuration() + " min");
            track.setText("Track " + item.getTrack());
            arrivalTime.setText(getFormatTime(item.getArriveTime()));
            forecastImage.setImage(new Image("https://openweathermap.org/img/wn/02d@2x.png"));

            setGraphic(gridPane);
        } else {
            setGraphic(null);
        }
    }

    private String getFormatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
}