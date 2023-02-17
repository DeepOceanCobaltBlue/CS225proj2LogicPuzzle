/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 * 2/16 [phoenix] - refactored class name
 *                - declared fields
 *                - created method templates
 *                - wrote getters
 *
 */
import javax.swing.*;

public abstract class PuzzleGame {
    // __ PRIVATE FIELDS __
    private GameBoard gameBoard;
    private GUI gui;

    // __ CONSTRUCTORS __
    public PuzzleGame() {
        gameBoard = new GameBoard();
        gui = new GUI();

    }

    /*public PuzzleGame(params) {

    }*/

    // __ FUNCTIONS __


    protected void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    protected void setGui(GUI gui) {
        this.gui = gui;
    }

    public abstract void play();

    protected GameBoard loadGame(String filePath) {
        GameBoard board;
        if (filePath.equals("none")) {
            board = new GameBoard();
        }
        else {
            //board = new GameBoard(filePath);
        }

        return gameBoard;
    }

    protected abstract JFrame createGUI();

    // __ ACCESSORS __

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GUI getGui() {
        return gui;
    }
}
