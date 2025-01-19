package Calendar;

import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * Controller class for a calendar application. Handles user interaction
 * with the calendar view and manages the selection of dates.
 */
public class CalendarController {

    @FXML
    private ComboBox<Integer> comboMonth;

    @FXML
    private ComboBox<Integer> comboYear;

    @FXML
    private GridPane grid;

    private final int DAYS_IN_WEEK = 7;
    private final int WEEKS_IN_MONTH = 6;
    private final int START_YEAR = 2020;
    private final int END_YEAR = 2025;
    private final int START_MONTH = 1;
    private final int END_MONTH = 12;

    private Button[][] days;

    /**
     * Initializes the controller. Sets up the combo boxes and the calendar grid.
     */
    @FXML
    public void initialize() {
        comboBoxInitialize();
        emptyGridInitialize();
    }

    /**
     * Initializes the combo boxes for selecting the year and month.
     * Populates them with valid values.
     */
    public void comboBoxInitialize() {
        // Populate year combo box with a range of years
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            comboYear.getItems().add(year);
        }
        // Populate month combo box with all months of the year
        for (int month = START_MONTH; month <= END_MONTH; month++) {
            comboMonth.getItems().add(month);
        }
    }

    /**
     * Initializes the calendar grid with empty buttons.
     * Sets up button actions to handle user interaction.
     */
    public void emptyGridInitialize() {
        // Initialize a 2D array to hold Button references for the calendar grid
        days = new Button[DAYS_IN_WEEK][WEEKS_IN_MONTH];
        for (int col = 0; col < DAYS_IN_WEEK; col++) {
            for (int row = 0; row < WEEKS_IN_MONTH; row++) {
                // Create a new Button for the current cell
                Button dayButton = new Button();
                // Set the button's size dynamically based on the GridPane dimensions
                dayButton.setPrefSize(
                    grid.getPrefWidth() / DAYS_IN_WEEK,  // Width per day
                    grid.getPrefHeight() / WEEKS_IN_MONTH // Height per week
                );
                // Add the button to the GridPane at the appropriate position
                grid.add(dayButton, col, row);
                // Assign an action to handle button clicks
                dayButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Handle button click events by invoking the handleButton method
                        handleButton(event);
                    }
                });
                // Store the button reference in the 2D array for future use
                days[col][row] = dayButton;
            }
        }
    }

    /**
     * Handles the action when a day button is clicked.
     * Retrieves the date from the button and triggers meeting schedule display.
     *
     * @param event The action event triggered by the button click.
     */
    public void handleButton(ActionEvent event) {
        // Retrieve the text of the clicked button
        String dayText = ((Button) event.getSource()).getText();
        // Ignore empty buttons
        if (dayText.isEmpty()) {
            return;
        }
        // Create a Calendar instance representing the selected date
        Calendar date = Calendar.getInstance();
        date.set(comboYear.getValue(), comboMonth.getValue() - 1, Integer.parseInt(dayText));
        // Show the meeting schedule for the selected date
        Logic.showMeetingSchedule(date);
    }

    /**
     * Handles the selection of a month from the combo box.
     * Updates the grid to reflect the selected month and year.
     *
     * @param event The action event triggered by the month combo box.
     */
    @FXML
    public void comboMonthChoose(ActionEvent event) {
        if (comboYear.getValue() != null) {
            clearGrid();
            updateGrid();
        }
    }

    /**
     * Handles the selection of a year from the combo box.
     * Updates the grid to reflect the selected year and month.
     *
     * @param event The action event triggered by the year combo box.
     */
    @FXML
    public void comboYearChoose(ActionEvent event) {
        if (comboMonth.getValue() != null) {
            clearGrid();
            updateGrid();
        }
    }

    /**
     * Clears the text of all buttons in the calendar grid.
     */
    public void clearGrid() {
        for (int col = 0; col < DAYS_IN_WEEK; col++) {
            for (int row = 0; row < WEEKS_IN_MONTH; row++) {
                days[col][row].setText("");
            }
        }
    }

    /**
     * Updates the calendar grid to reflect the days of the selected month and year.
     * Uses the first day of the month and the total days to populate the grid.
     */
    public void updateGrid() {
        // Create a Calendar instance for the first day of the selected month and year
        Calendar calendar = Calendar.getInstance();
        calendar.set(comboYear.getValue(), comboMonth.getValue() - 1, 1);
        // Determine the first day of the week and the number of days in the month
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // Populate the grid with day numbers
        for (int i = 0; i < lastDay; i++) {
            days[(firstDay - 1 + i) % DAYS_IN_WEEK][(firstDay + i - 1) / DAYS_IN_WEEK]
                .setText(String.valueOf(i + 1));
        }
    }
}
