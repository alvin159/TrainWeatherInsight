package compse110.frontend;

import compse110.Entity.TrainInformation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrainDetailPage extends Application {

    public void start(Stage primaryStage, TrainInformation trainInformation) {

        if (trainInformation == null) {
            //
            return;
        }
        primaryStage.setTitle(trainInformation.getTrainName());

        VBox root = new VBox();

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
