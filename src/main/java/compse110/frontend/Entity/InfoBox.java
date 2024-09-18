package compse110.frontend.Entity;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoBox extends VBox{
            public InfoBox(String value, String label) {
            // Create the two labels
            Label valueLabel = new Label(value);
            Label descriptionLabel = new Label(label);
            
            // Add the labels to the VBox
            this.getChildren().addAll(valueLabel, descriptionLabel);
            
            // Apply the common styling
            this.setStyle(
                "-fx-background-color: #d96ce3;" +  // dynamic color
                "-fx-background-radius: 50%;" +                     // round border
                "-fx-min-width: 100px;" +                           // min width
                "-fx-max-height: 100px;" +                          // max height
                "-fx-max-width: 100px;" +                           // max width
                "-fx-alignment: center;"                            // center alignment
            );
            
            // Align the content in the center of the VBox
            this.setAlignment(Pos.CENTER);
        }
}
