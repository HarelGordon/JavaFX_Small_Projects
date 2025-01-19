package Calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;

/**
 * Logic class that manages meeting schedules and provides dialog-based
 * interaction for displaying and editing meetings.
 */
public class Logic {

    //HashMap to store meeting details with their respective dates.
    private static HashMap<Calendar, String> meetings = new HashMap<>();

    /**
     * Displays a dialog showing the meeting schedule for a specific date.
     * If a meeting exists, the user can edit it. Otherwise, they can create a new meeting.
     *
     * @param date The date for which the meeting schedule should be displayed.
     */
    public static void showMeetingSchedule(Calendar date) {
        // Normalize the date to ensure consistency in key usage.
        Calendar normalizedDate = normalize(date);
        // Create a dialog for displaying the meeting information.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(normalizedDate.getTime()));
        String meetingText;
        ButtonType primaryButton;
        // Determine whether a meeting exists for the specified date.
        if (meetings.containsKey(normalizedDate)) {
            meetingText = meetings.get(normalizedDate);
            primaryButton = new ButtonType("Edit");
        } else {
            meetingText = "You have no meetings on this day.";
            primaryButton = new ButtonType("New");
        }
        // Set up the dialog content and buttons.
        dialog.setContentText(meetingText);
        dialog.getDialogPane().getButtonTypes().addAll(primaryButton, ButtonType.CANCEL);
        // Handle button actions using a result converter.
        dialog.setResultConverter(button -> {
            if (button == primaryButton) {
                // Show the edit dialog when the primary button is pressed.
                showEditDialog(normalizedDate);
            }
            return "";
        });
        dialog.showAndWait();
    }

    /**
     * Displays a dialog for editing or creating a meeting on a specific date.
     *
     * @param date The date for which the meeting is being edited or created.
     */
    public static void showEditDialog(Calendar date) {
        // Create a dialog for editing meeting details.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Meeting");
        dialog.setHeaderText(null);
        // Create a text area for entering meeting details.
        TextArea textArea = new TextArea();
        // Pre-fill the text area if a meeting already exists for the specified date, if not - use default massage.
        textArea.setPromptText("Enter your meeting details here...");
        String meetingText = meetings.get(date);
        if (meetingText != null) {
            textArea.setText(meetingText);
        }
        // Add the text area and buttons to the dialog.
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        // Handle button actions using a result converter.
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Save the entered meeting details to the map.
                meetings.put(date, textArea.getText());
            }
            return null;
        });
        dialog.showAndWait();
    }

    /**
     * Normalizes a Calendar date by setting the time components (hours, minutes,
     * seconds, milliseconds) to zero, ensuring consistency for date-only operations.
     *
     * @param date The original date to normalize.
     * @return A normalized Calendar instance with time components cleared.
     */
    private static Calendar normalize(Calendar date) {
        Calendar normalized = Calendar.getInstance();
        // Set the date components while clearing time components.
        normalized.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        normalized.set(Calendar.MILLISECOND, 0);
        return normalized;
    }
}
