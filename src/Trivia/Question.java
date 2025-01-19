package Trivia;

/**
 * Represents a single question in a quiz game. Each question contains the question text, 
 * the correct answer, and three incorrect answers.
 */
public class Question {

    // Instance variables
    private String questionText; // The text of the question
    private String correctAnswer; // The correct answer to the question
    private String wrongAnswer1; // First incorrect answer
    private String wrongAnswer2; // Second incorrect answer
    private String wrongAnswer3; // Third incorrect answer

    /**
     * Default constructor initializing an empty question.
     */
    public Question() {
        this.questionText = "";
        this.correctAnswer = "";
        this.wrongAnswer1 = "";
        this.wrongAnswer2 = "";
        this.wrongAnswer3 = "";
    }

    /**
     * Parameterized constructor to initialize a question with specified values.
     * 
     * @param questionText The text of the question.
     * @param correctAnswer The correct answer.
     * @param wrongAnswer1 The first incorrect answer.
     * @param wrongAnswer2 The second incorrect answer.
     * @param wrongAnswer3 The third incorrect answer.
     */
    public Question(String questionText, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
    }

    /**
     * Gets the text of the question.
     * 
     * @return The question text.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the text of the question.
     * 
     * @param questionText The question text to set.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Gets the correct answer of the question.
     * 
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer of the question.
     * 
     * @param correctAnswer The correct answer to set.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the first incorrect answer.
     * 
     * @return The first incorrect answer.
     */
    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    /**
     * Sets the first incorrect answer.
     * 
     * @param wrongAnswer1 The first incorrect answer to set.
     */
    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    /**
     * Gets the second incorrect answer.
     * 
     * @return The second incorrect answer.
     */
    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    /**
     * Sets the second incorrect answer.
     * 
     * @param wrongAnswer2 The second incorrect answer to set.
     */
    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    /**
     * Gets the third incorrect answer.
     * 
     * @return The third incorrect answer.
     */
    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    /**
     * Sets the third incorrect answer.
     * 
     * @param wrongAnswer3 The third incorrect answer to set.
     */
    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }
}
