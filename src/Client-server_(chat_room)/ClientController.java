package Q1;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller class for the client GUI. Handles user interactions with the client interface.
 */
public class ClientController {

    @FXML
    private TextField message; // Text field for typing messages.
    @FXML
    private TextArea messagesArea; // Text area to display messages from the server.
    @FXML
    private TextArea participantsArea; // Text area to display the list of participants.

    private ClientThread clientThread; // The thread handling communication with the server.
    private String messageAreaString = ""; // Accumulated messages to display.
    private boolean isJoin = false; // Whether the client has joined the server.

    /**
     * Handles the "Join" button press. Connects the client to the server.
     * 
     * @param event The action event triggered by the button press.
     */
    @FXML
    void joinPressed(ActionEvent event) {
        if (!isJoin) {
            String name = JOptionPane.showInputDialog("Enter your name"); // Prompt for the user's name.
            if (name != null && !name.trim().isEmpty()) {
                String computerName = JOptionPane.showInputDialog("Enter computer name"); // Prompt for the server's address.
                if (isComputerNameValid(computerName)) {
                    clientThread = new ClientThread(this, computerName, name); // Create a new client thread.
                    isJoin = true;
                    clientThread.start(); // Start the client thread.
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid computer name. Please try again.");
                }
            }
        }
    }

    /**
     * Validates the provided computer name by attempting a socket connection.
     * 
     * @param computerName The name or IP address of the server.
     * @return True if the connection is successful, false otherwise.
     */
    private boolean isComputerNameValid(String computerName) {
        try (Socket testSocket = new Socket(computerName, 3333)) {
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: Computer name is not valid.");
            return false;
        }
    }

    /**
     * Handles the "Leave" button press. Disconnects the client from the server.
     * 
     * @param event The action event triggered by the button press.
     */
    @FXML
    void leavePressed(ActionEvent event) {
        if (isJoin) {
            clientThread.leave(); // Notify the server that the client is leaving.
            isJoin = false;
        }
    }

    /**
     * Handles the "Enter" key press in the message field. Sends the message to the server.
     * 
     * @param event The key event triggered by pressing "Enter".
     */
    @FXML
    void sendText(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !message.getText().trim().isEmpty()) {
            clientThread.sendMessage(message.getText()); // Send the typed message.
            message.clear(); // Clear the text field.
        }
    }

    /**
     * Updates the participants area with the current list of participants.
     * 
     * @param participants The list of participants to display.
     */
    public void setParticipantsArea(ArrayList<String> participants) {
        String arrayToString = String.join("\n", participants);
        participantsArea.setText(arrayToString);
    }

    /**
     * Appends a message to the messages area.
     * 
     * @param message The message to append.
     */
    public void addToMessagesArea(String message) {
        messageAreaString += message;
        messagesArea.setText(messageAreaString);
    }
}
