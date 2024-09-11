package compse110.frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage extends Application {
    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) throws Exception {

        //Creating a new BorderPane.
        BorderPane root = new BorderPane();

        VBox vBox = new VBox(5);
        vBox.getChildren().add(new Text("TrainWeatherInsight"));
        root.setCenter(vBox);

        root.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(root, 700, 900);
        stage.setScene(scene);
        stage.setTitle("TrainWeatherInsight");
        stage.show();

    }
}
