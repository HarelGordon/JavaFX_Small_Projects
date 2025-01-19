/**
 * This class manages the game logic for the Hangman game, including word handling,
 * guess validation, and win/lose conditions.
 */
package Hangman_Game;

import java.util.ArrayList;

public class Logic {

    private static final int MAX_WRONG_GUESSES = 6;
    private static final String HIDDEN_CHARS = "_ ";

    private String theWord;
    private StringBuilder displayWord;
    private ArrayList<Character> guessesList;
    private int wrongGuessCount;

    /**
     * Constructs a new Logic instance and initializes the game state.
     */
    public Logic() {
        WordDatabase.loadWordsToArrayFromFile();
        if (!WordDatabase.isWordListReady()) {
            throw new IllegalStateException("Word list could not be loaded. Ensure " + WordDatabase.WORDS_FILE + " is available.");
        }
        resetGame();
    }
    
    public String getTheWord() {
    	return this.theWord;
    }

    /**
     * Resets the game state for a new game.
     */
    public void resetGame() {
        this.theWord = WordDatabase.getRandomWord();
        this.displayWord = new StringBuilder();
        this.guessesList = new ArrayList<Character>();
        this.wrongGuessCount = 0;

        for (int i = 0; i < theWord.length(); i++) {
            displayWord.append(HIDDEN_CHARS);
        }
    }

    /**
     * Gets the current display word with guessed letters revealed.
     * @return The display word as a String.
     */
    public String getDisplayWord() {
        return displayWord.toString();
    }

    /**
     * Gets the number of incorrect guesses made so far.
     * @return The count of incorrect guesses.
     */
    public int getWrongGuessCount() {
        return wrongGuessCount;
    }

    /**
     * Gets the list of guessed letters.
     * @return An ArrayList of guessed characters.
     */
    public ArrayList<Character> getGuessesList() {
        return guessesList;
    }

    /**
     * Processes a guessed letter and updates the game state.
     * @param letter The guessed letter.
     * @return True if the letter is correct, false otherwise.
     */
    public boolean guessLetter(char letter) {
        if (guessesList.contains(letter)) {
            return false; // Letter already guessed
        }

        guessesList.add(letter);
        boolean correct = false;

        for (int i = 0; i < theWord.length(); i++) {
            if (theWord.charAt(i) == letter) {
                displayWord.setCharAt(i * 2, letter);
                correct = true;
            }
        }

        if (!correct) {
            wrongGuessCount++;
        }

        return correct;
    }

    /**
     * Checks if the game is won.
     * @return True if all letters are guessed, false otherwise.
     */
    public boolean isWin() {
        for (int i = 0; i < displayWord.length(); i++) {
            if (displayWord.charAt(i) == '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the game is lost.
     * @return True if the maximum wrong guesses are reached, false otherwise.
     */
    public boolean isLose() {
        return wrongGuessCount >= MAX_WRONG_GUESSES;
    }
}
