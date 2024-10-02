package backend;


import compse110.backend.TrainComponent;

public class TrainComponentTest {

    public static void main(String[] args) {
        TrainComponent trainComponent = new TrainComponent();
        System.out.println(trainComponent.getTrainData("HKI"));
        System.out.println("\n\n");
        System.out.println(trainComponent.getTrainData("HKI", "TPE"));
    }
}
