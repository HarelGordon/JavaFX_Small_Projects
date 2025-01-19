package Trivia;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Controller class for the Trivia Game.
 * Manages the user interface and interactions for the game.
 */
public class TriviaController {

    // Game logic instance
    private Logic game = new Logic();
    private ArrayList<Question> questionsArray = new ArrayList<>();
    private static Question currentQuestion;
    private static String correctAnswer;

    @FXML
    private Label lblQuestion; // Label to display the current question

    @FXML
    private ComboBox<String> comboBox; // ComboBox to display answer options

    /**
     * Initializes the controller and loads the first question.
     */
    @FXML
    public void initialize() {
    	try {
			game.loadQuestionsToArrayFromFile();
	        loadQuestionToUI();
    	}
    	catch (FileNotFoundException e) {
        	Logic.showError(Logic.FILE_ERROR_TITLE, Logic.FILE_ERROR_CONTENT + Logic.QUESTIONS_FILE);
		}
    }

    /**
     * Handles the event when the "Guess" button is pressed.
     * 
     * @param event The action event triggered by the button press.
     */
    @FXML
    void guessPressed(ActionEvent event) {
        if (comboBox.getValue() == null) {
            JOptionPane.showMessageDialog(null, "You guessed nothing");
            return;
        } else if (comboBox.getValue().equals(correctAnswer)) {
            Logic.setScore(Logic.getScore() + 10);
            JOptionPane.showMessageDialog(null, "Correct answer!");
        } else {
            Logic.setScore(Logic.getScore() - 5);
            JOptionPane.showMessageDialog(null, "Wrong answer!");
        }

        comboBox.getItems().clear();

        if (questionsArray.isEmpty()) {
            Logic.endGameMessaga();
        } else {
            loadQuestionToUI();
        }
    }

    /**
     * Handles the event when the "End Game" button is pressed.
     * 
     * @param event The action event triggered by the button press.
     */
    @FXML
    void endPressed(ActionEvent event) {
    	Logic.endGameMessaga();
        questionsArray.clear();
    }

    /**
     * Handles the event when the "New Game" button is pressed.
     * 
     * @param event The action event triggered by the button press.
     */
    @FXML
    void newPressed(ActionEvent event) {
    	Logic.endGameMessaga();
        comboBox.getItems().clear();
        try {
        	game.loadQuestionsToArrayFromFile();
        	loadQuestionToUI();
        }
        catch (FileNotFoundException e) {
            Logic.showError(Logic.FILE_ERROR_TITLE, Logic.FILE_ERROR_CONTENT + Logic.QUESTIONS_FILE);
    	}

    }

    /**
     * Loads a new question into the user interface.
     */
    public void loadQuestionToUI() {
        questionsArray = game.getQuestionsArray();
        // Check if there are no questions available
        if (questionsArray == null || questionsArray.isEmpty()) {
            Logic.showError(Logic.GAME_ERROR_TITLE, Logic.END_FILE_CONTENT);
            return;
        }
        // Randomly select a question
        Random rand = new Random();
        currentQuestion = questionsArray.get(rand.nextInt(questionsArray.size()));
        // Display the question text
        lblQuestion.setText(currentQuestion.getQuestionText());
        // Store the correct answer and add the answers to the combo box
        correctAnswer = currentQuestion.getCorrectAnswer();
        comboBox.getItems().add(correctAnswer);
        comboBox.getItems().add(currentQuestion.getWrongAnswer1());
        comboBox.getItems().add(currentQuestion.getWrongAnswer2());
        comboBox.getItems().add(currentQuestion.getWrongAnswer3());
        // Shuffle the answer options and update the combo box
		ObservableList<String> items = comboBox.getItems();
		Collections.shuffle(items);
		comboBox.setItems(items);
	    // Remove the question from the list to avoid re-asking
        questionsArray.remove(currentQuestion);
    }
}
