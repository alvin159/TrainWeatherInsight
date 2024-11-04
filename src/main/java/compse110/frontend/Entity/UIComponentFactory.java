package compse110.frontend.Entity;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

public class UIComponentFactory {
    public static DatePicker departureDatePickerCreator(LocalDate date) {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(date);
        datePicker.setDayCellFactory(lambdaDatePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusDays(14); // Calculate the maximum date (14 days from today)

                // Disable dates that are empty, before today, or after maxDate
                setDisable(empty || date.isBefore(today) || date.isAfter(maxDate));
            }
        });
        return datePicker;
    }
}
