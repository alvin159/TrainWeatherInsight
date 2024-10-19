package backend;


import compse110.Entity.TrainData;
import compse110.backend.TrainComponent;

import java.util.Date;
import java.util.List;

public class TrainComponentTest {

    public static void main(String[] args) {
        TrainComponent trainComponent = new TrainComponent();
        List<TrainData> trainData = trainComponent.getTrainData(new Date(), "HKI");
        List<TrainData> trainData2 = trainComponent.getTrainData(new Date(),"HKI", "TPE");
        System.out.println(trainData);
        System.out.println("\n\n");
        System.out.println("HKI size: " + trainData.size());
        System.out.println("HKI to TPE size: " + trainData2.size());
    }
}
