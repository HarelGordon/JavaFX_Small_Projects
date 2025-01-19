/**
 * This class acts as the controller for the Hangman game, managing user interactions,
 * updating the UI, and interfacing with the game logic.
 */
package Hangman_Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.Optional;

public class HangmanController {

    private static final String NO_GUESSES_MESSAGE = "No guesses yet";

    @FXML
    private Circle hang_1;
    @FXML
    private Line hang_2;
    @FXML
    private Line hang_6;
    @FXML
    private Line hang_5;
    @FXML
    private Line hang_3;
    @FXML
    private Line hang_4;
    @FXML
    private ComboBox<Character> comBox;
    @FXML
    private Label textWord;
    @FXML
    private Label guesses;

    private Logic game;

    /**
     * Initializes the controller and starts a new game.
     */
    @FXML
    public void initialize() {
        try {
            game = new Logic();
            resetUI();
        } catch (IllegalStateException e) {
            showError("Initialization Error", e.getMessage());
        }
    }

    /**
     * Resets the UI components to reflect a new game state.
     */
    private void resetUI() {
        textWord.setText(game.getDisplayWord());
        guesses.setText(NO_GUESSES_MESSAGE);
        comBox.getItems().clear();

        for (char c = 'a'; c <= 'z'; c++) {
            comBox.getItems().add(c);
        }

        hideHangmanParts();
    }

    /**
     * Hides all parts of the hangman figure.
     */
    private void hideHangmanParts() {
        hang_1.setVisible(false);
        hang_2.setVisible(false);
        hang_3.setVisible(false);
        hang_4.setVisible(false);
        hang_5.setVisible(false);
        hang_6.setVisible(false);
    }

    /**
     * Displays a part of the hangman figure based on the number of wrong guesses.
     * @param part The part number to display.
     */
    private void showHangmanPart(int part) {
    	switch (part) {
        case 1:
            hang_1.setVisible(true);
            break;
        case 2:
            hang_2.setVisible(true);
            break;
        case 3:
            hang_3.setVisible(true);
            break;
        case 4:
            hang_4.setVisible(true);
            break;
        case 5:
            hang_5.setVisible(true);
            break;
        case 6:
            hang_6.setVisible(true);
            break;
        default:
        	break;
    	}
    }

    /**
     * Handles the action of pressing the "Guess" button.
     * @param event The ActionEvent triggered by the button press.
     */
    @FXML
    void guessPressed(ActionEvent event) {
        Character selectedLetter = comBox.getValue();
        if (selectedLetter == null) return;

        boolean correct = game.guessLetter(selectedLetter); //Checks if the letter is in the word. If so, put it in the displayWord variable. If not, increments wrongGuessCount by one.
        textWord.setText(game.getDisplayWord());
        guesses.setText("Guesses: " + game.getGuessesList());
        comBox.getItems().remove(selectedLetter);

        if (!correct) {
            showHangmanPart(game.getWrongGuessCount());
        }

        if (game.isWin()) {
            showEndGameAlert("YOU WIN!", "Do you want to play again?");
        } else if (game.isLose()) {
            showEndGameAlert("YOU LOSE!", "The word was: " + game.getTheWord() + "\nDo you want to play again?");
        }
    }

    /**
     * Handles the action of pressing the "New Game" button.
     * @param event The ActionEvent triggered by the button press.
     */
    @FXML
    void newPressed(ActionEvent event) {
        game.resetGame();
        resetUI();
    }

    /**
     * Displays an alert for end-game scenarios and handles restarting the game if requested.
     * @param title The title of the alert.
     * @param content The content of the alert.
     */
    private void showEndGameAlert(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.YES) {
            game.resetGame();
            resetUI();
        }
    }

    /**
     * Displays an error alert for exceptions or critical issues.
     * @param title The title of the error.
     * @param content The content of the error message.
     */
    private void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
