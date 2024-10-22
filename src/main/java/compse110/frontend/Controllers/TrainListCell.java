package compse110.frontend.Controllers;

import compse110.frontend.Entity.TrainInformation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TrainListCell extends ListCell<TrainInformation> {

    private final HBox itemView;
    private final Label trainName;
    private final Label departureTime;
    private final Label estimatedTime;
    private final Label duration;
    private final Label track;
    private final Label arrivalTime;
    private final ImageView forecastImage;

    //onCreateView
    public TrainListCell() {
        //create view
        itemView = new HBox();

        itemView.setSpacing(50);
        itemView.setAlignment(Pos.CENTER);

        trainName = new Label("Train Name");
        departureTime = new Label("12:00 ");
        duration = new Label("5 hrs 0 minutes");
        estimatedTime = new Label("12:00 ");
        track = new Label("Track 1");
        arrivalTime = new Label("17:00 ");
        forecastImage = new ImageView("https://openweathermap.org/img/wn/03d@2x.png");

        itemView.getChildren().addAll(trainName, departureTime, estimatedTime, track, arrivalTime, forecastImage);

    }


    @Override
    protected void updateItem(TrainInformation item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            this.trainName.setText(item.getTrainName());
            this.departureTime.setText(item.getDepartureTime().toString());
            this.duration.setText(item.getDuration() + " min");
            if (item.getEstimatedTime() != null) {
                this.estimatedTime.setText(item.getEstimatedTime().toString());
            }
            this.track.setText("Track " + item.getTrack());
            this.arrivalTime.setText(item.getArriveTime().toString());

            setGraphic(itemView);
        } else {
            setAlignment(null);
        }
    }
}
