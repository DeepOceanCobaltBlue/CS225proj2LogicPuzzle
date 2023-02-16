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

    }

    /*public PuzzleGame(params) {

    }*/

    // __ FUNCTIONS __

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GUI getGui() {
        return gui;
    }

    public abstract void play();

    private GameBoard loadGame(String filePath) {
        GameBoard board = new GameBoard();
        return gameBoard;
    }

    protected abstract JFrame createGUI();
}
