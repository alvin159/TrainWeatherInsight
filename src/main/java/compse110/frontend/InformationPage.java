package compse110.frontend;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InformationPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Information Page");

        // Create a simple UI for the InformationPage
        Label infoLabel = new Label("Welcome to the Information Page!");
        infoLabel.setStyle("-fx-font-size: 18px; -fx-padding: 20px;");

        VBox layout = new VBox(infoLabel);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300); // Set a fixed size for the new window
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Maximize the stage for full screen
        primaryStage.show();        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
