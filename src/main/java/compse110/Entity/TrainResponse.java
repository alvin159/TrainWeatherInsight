package compse110.Entity;
import java.util.ArrayList;
import java.util.List;

public class TrainResponse {
    private List<TrainInfo> trains;

    public TrainResponse() {
        this.trains = new ArrayList<>();
    }

    public void addTrain(TrainInfo trainInfo) {
        this.trains.add(trainInfo);
    }

    public List<TrainInfo> getTrains() {
        return trains;
    }

    @Override
    public String toString() {
        return "TrainResponse{" +
                "trains=" + trains +
                '}';
    }
}
