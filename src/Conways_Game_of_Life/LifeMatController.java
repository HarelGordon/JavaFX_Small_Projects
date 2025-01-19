package Conways_Game_of_Life;

import java.util.LinkedList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The LifeMatController class implements Conway's Game of Life on a grid.
 * It uses a Canvas to display the cells and controls the simulation of the game.
 */
public class LifeMatController {

    @FXML
    private Canvas canv; // Canvas for rendering the game grid
    private GraphicsContext gc; // GraphicsContext for drawing on the canvas
    private final int SIZE = 10; // Size of the grid (SIZE x SIZE)
    private LinkedList<Rectangle> cells; // Current state of the cells
    private LinkedList<Rectangle> newCells; // Buffer to store the next state of the cells
    private final Random random = new Random(); // Random generator for initial cell states

    /**
     * Initializes the game controller, setting up the canvas and cell grid.
     * This method is called automatically by JavaFX when the FXML file is loaded.
     */
    public void initialize() {
        gc = canv.getGraphicsContext2D();
        cells = new LinkedList<>();
        newCells = new LinkedList<>();
        generateCells();
    }

    /**
     * Generates the initial grid of cells with random states (alive or dead).
     * Each cell is represented as a Rectangle with a specific color indicating its state.
     */
    private void generateCells() {
        int cellWidth = (int) (canv.getWidth() / SIZE);
        int cellHeight = (int) (canv.getHeight() / SIZE);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int x = j * cellWidth;
                int y = i * cellHeight;
                Rectangle rect = new Rectangle(x, y, cellWidth, cellHeight);
                Color color = getRandomColor();
                rect.setUserData(color);
                cells.add(rect);
                Rectangle newRect = new Rectangle(x, y, cellWidth, cellHeight);
                newRect.setUserData(color);
                newCells.add(newRect);
            }
        }
    }

    /**
     * Returns a random color to initialize a cell's state.
     * Gray represents a live cell, and White represents a dead cell.
     *
     * @return A Color representing the cell's state.
     */
    private Color getRandomColor() {
        int choice = random.nextInt(2);
        switch (choice) {
            case 0: return Color.GRAY;
            case 1: return Color.WHITE;
        }
        return null;
    }

    /**
     * Draws the current state of the cells on the canvas.
     * Each cell is filled with its corresponding color and outlined with a black border.
     */
    private void drowCells() {
        gc.clearRect(0, 0, canv.getWidth(), canv.getHeight()); // Clear the canvas

        for (Rectangle rect : cells) {
            Color color = (Color) rect.getUserData(); // Retrieve stored color
            if (color != null) {
                gc.setFill(color);
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.setStroke(Color.BLACK);
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
        }
    }

    /**
     * Calculates the next state of the cells based on Conway's Game of Life rules.
     * Updates the buffer (newCells) with the computed states for each cell.
     */
    private void conwayCalc() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int index = i * SIZE + j;
                int liveNeighbors = liveNeighborsCalc(i, j);
                Color currentColor = (Color) cells.get(index).getUserData();
                if (currentColor == Color.WHITE) { // Dead cell
                    if (liveNeighbors == 3)
                        newCells.get(index).setUserData(Color.GRAY); // Become alive
                    else
                        newCells.get(index).setUserData(Color.WHITE); // Remain dead
                }
                else if (currentColor == Color.GRAY) { // Living cell
                    if (liveNeighbors == 2 || liveNeighbors == 3)
                        newCells.get(index).setUserData(Color.GRAY); // Stay alive
                    else
                        newCells.get(index).setUserData(Color.WHITE); // Die
                }
            }
        }
    }

    /**
     * Counts the number of live neighbors for a cell at the given row and column indices.
     *
     * @param indexRow The row index of the cell.
     * @param indexCol The column index of the cell.
     * @return The number of live neighbors.
     */
    private int liveNeighborsCalc(int indexRow, int indexCol) {
        int count = 0;
        if (((indexRow > 0) && (indexCol > 0)) && 
            (cells.get(((indexRow - 1) * SIZE) + indexCol - 1).getUserData() == Color.GRAY)) // Top-left
            count++;
        if ((indexRow > 0) && 
            (cells.get(((indexRow - 1) * SIZE) + indexCol).getUserData() == Color.GRAY)) // Top
            count++;
        if (((indexRow > 0) && (indexCol < SIZE - 1)) && 
            (cells.get(((indexRow - 1) * SIZE) + indexCol + 1).getUserData() == Color.GRAY)) // Top-right
            count++;
        if ((indexCol > 0) &&
            (cells.get((indexRow * SIZE) + indexCol - 1).getUserData() == Color.GRAY)) // Left
            count++;
        if ((indexCol < SIZE - 1) && 
            (cells.get((indexRow * SIZE) + indexCol + 1).getUserData() == Color.GRAY)) // Right
            count++;
        if (((indexRow < SIZE - 1) && (indexCol > 0)) && 
            (cells.get(((indexRow + 1) * SIZE) + indexCol - 1).getUserData() == Color.GRAY)) // Bottom-left
            count++;
        if ((indexRow < SIZE - 1) && 
            (cells.get(((indexRow + 1) * SIZE) + indexCol).getUserData() == Color.GRAY)) // Bottom
            count++;
        if (((indexRow < SIZE - 1) && (indexCol < SIZE - 1)) && 
            (cells.get(((indexRow + 1) * SIZE) + indexCol + 1).getUserData() == Color.GRAY)) // Bottom-right
            count++;
        return count;
    }

    /**
     * Handles the "Next" button press to progress the game to the next generation.
     * Draws the current cells, calculates the next generation, and updates the cell states.
     *
     * @param event The ActionEvent triggered by the button press.
     */
    @FXML
    private void nextPressed(ActionEvent event) {
        drowCells();
        conwayCalc();
        for (int i = 0; i < cells.size(); i++) {
            cells.get(i).setUserData(newCells.get(i).getUserData());
        }
    }
}
