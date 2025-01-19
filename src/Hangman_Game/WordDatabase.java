/**
 * This class handles the storage and retrieval of words for the Hangman game.
 * It loads words from a file into a list and provides functionality to retrieve random words.
 */
package Hangman_Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WordDatabase {
	
    static final String WORDS_FILE = "hangman_words.txt"; // File containing the words
    private static ArrayList<String> words; // List to store words loaded from a file

    /**
     * Loads words from a file into the words list.
     * The file should contain one word per line.
     */
    public static void loadWordsToArrayFromFile() {
        words = new ArrayList<>();
        try (Scanner input = new Scanner(new File(WORDS_FILE))) {
            while (input.hasNext()) {
                words.add(input.next());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found. Please ensure " + WORDS_FILE +" is in the correct directory.");
        }
    }

    /**
     * Retrieves a random word from the words list.
     * @return A randomly selected word as a String.
     * @throws IllegalStateException if the words list is empty or uninitialized.
     */
    public static String getRandomWord() {
        if (words == null || words.isEmpty()) {
            throw new IllegalStateException("Word list is empty or uninitialized. Call loadWordsToArrayFromFile() first.");
        }
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }

    /**
     * Checks if the word list is loaded and ready.
     * @return True if the word list is initialized and non-empty, false otherwise.
     */
    public static boolean isWordListReady() {
        return words != null && !words.isEmpty();
    }
}
