package compse110.frontend.Controllers;

import compse110.frontend.Entity.TrainInformation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class TrainListCell extends ListCell<TrainInformation> {

    private final HBox itemView;
    private final Label departureTime;

    private final Label duration;

    private final Label arrivalTime;

    //onCreateView
    public TrainListCell() {
        //create view
        itemView = new HBox();
        itemView.setSpacing(50);
        itemView.setAlignment(Pos.CENTER);

        departureTime = new Label("12:00 ");
        duration = new Label("5 hrs 0 minutes");
        arrivalTime = new Label("17:00 ");

        itemView.getChildren().addAll(departureTime, duration, arrivalTime);

    }


    @Override
    protected void updateItem(TrainInformation item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            this.departureTime.setText(item.getDepartureTime().toString());
            this.duration.setText(item.getDuration() + " min");
            this.arrivalTime.setText(item.getArriveTime().toString());
        }
    }
}
