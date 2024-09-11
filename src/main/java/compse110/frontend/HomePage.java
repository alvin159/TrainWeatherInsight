package compse110.frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class HomePage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TrainFinder");

        // Create UI elements
        Label titleLabel = new Label("TrainFinder");
        titleLabel.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(100));

        Label welcomeLabel = new Label("Welcome to search any train connections and information about your destination");
        Label departingStationLabel = new Label("Departing station:");
        TextField departingStationField = new TextField("Oulu");
        Label arrivalStationLabel = new Label("Arrival station (optional):");
        TextField arrivalStationField = new TextField("Helsinki");
        Label departureDateLabel = new Label("Departure date:");
        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setValue(LocalDate.now()); // Set default value to today

        // Disable past dates in the DatePicker
        departureDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        CheckBox showCoolFactsCheckBox = new CheckBox("Show cool facts about cities");
        Button searchButton = new Button("Search");

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(titleLabel, 0, 0, 2, 1); // Add the title and span across two columns
        gridPane.add(welcomeLabel, 0, 1, 2, 1);
        gridPane.add(departingStationLabel, 0, 2);
        gridPane.add(departingStationField, 1, 2);
        gridPane.add(arrivalStationLabel, 0, 3);
        gridPane.add(arrivalStationField, 1, 3);
        gridPane.add(departureDateLabel, 0, 4);
        gridPane.add(departureDatePicker, 1, 4);
        gridPane.add(showCoolFactsCheckBox, 0, 5);
        gridPane.add(searchButton, 1, 5);

        // Create scene and stage with full screen and centered content
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.setMaximized(true); // Maximize the stage for full screen
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
