package Trivia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Handles the logic for managing questions and verifying answers in the quiz game.
 */
public class Logic {

    // Constants
    static final String QUESTIONS_FILE = "trivia.txt";
    static final String FILE_ERROR_TITLE = "File Error";
    static final String GAME_ERROR_TITLE = "Game Error";
    static final String QUESTION_ERROR_CONTENT = "There are no 4 answers to a question";
    static final String FILE_ERROR_CONTENT = "Could not find the file ";
    static final String END_FILE_CONTENT = "No questions available to load";

    // Instance variables
    private ArrayList<Question> questionsArray; // List of questions loaded from the file
    private static int score; // Player's current score

    /**
     * Gets the list of questions loaded from the file.
     * 
     * @return The list of questions.
     */
    public ArrayList<Question> getQuestionsArray() {
        return questionsArray;
    }

    /**
     * Loads questions from a file into the questions array.
     * The file must have a specific format: each question is followed by its correct answer and three incorrect answers.
     * @throws FileNotFoundException 
     */
    public void loadQuestionsToArrayFromFile() throws FileNotFoundException {
        questionsArray = new ArrayList<>();
        try (Scanner input = new Scanner(new File(QUESTIONS_FILE))) { // Try-with-resources
        	while (input.hasNextLine()) {
                String questionText = input.nextLine();
                String correctAnswer = input.nextLine();
                String wrongAnswer1 = input.nextLine();
                String wrongAnswer2 = input.nextLine();
                String wrongAnswer3 = input.nextLine();
                
                Question question = new Question(questionText, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
                questionsArray.add(question);  
            }
        } catch (FileNotFoundException e) {
        	throw new FileNotFoundException();
        } catch (NoSuchElementException e) { //Fell inside the while loop
        	showError(FILE_ERROR_TITLE, QUESTION_ERROR_CONTENT);
        }
    }

    /**
     * Checks if the provided answer is correct. Updates the score if correct.
     * 
     * @param userAnswer The answer provided by the user.
     * @param correctAnswer The correct answer for the current question.
     * @return True if the answer is correct, otherwise false.
     */
    public boolean isCorrect(String userAnswer, String correctAnswer) {
        boolean result = userAnswer.equalsIgnoreCase(correctAnswer);
        if (result) {
            setScore(getScore() + 1);
        }
        return result;
    }

    /**
     * Gets the player's current score.
     * 
     * @return The player's score.
     */
    public static int getScore() {
        return score;
    }

    /**
     * Resets the player's score to zero.
     */
    public static void resetScore() {
        setScore(0);
    }

    /**
     * Sets the score for the game.
     * This method is static, so it updates the score for the entire game session.
     * 
     * @param score The score to set. This value will overwrite the current score.
     */
    public static void setScore(int score) {
        Logic.score = score;
    }

    /**
     * Displays an error message in an alert box with an "OK" button.
     * This method is used to show critical errors to the user, with a title and content.
     * 
     * @param title The title of the alert box.
     * @param content The content message to display in the alert box.
     */
    public static void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: No header text
        alert.showAndWait(); // Wait for the user to acknowledge the alert
    }

    /**
     * Displays a game over message in a dialog box using JOptionPane.
     * This method shows the final score of the game when the game is over.
     * 
     * @param score The final score of the game, which will be displayed in the message.
     */
    public static void endGameMessaga() {
        JOptionPane.showMessageDialog(null, "GAME OVER\nYou got " + score + " points");
    }

}
