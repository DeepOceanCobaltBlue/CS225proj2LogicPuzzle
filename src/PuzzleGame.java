/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 * 2/16 [phoenix] - refactored class name
 *                - declared fields
 *                - created method templates
 *                - wrote getters
 *
 * 2/16 [phoenix] - added conditional to loadGame
 *                - wrote field comments
 *
 * 2/17 [phoenix] - edited loadGame
 *                - wrote method comments
 */
import javax.swing.*;

public abstract class PuzzleGame {
    // __ PRIVATE FIELDS __
    // Game board on which handles the text elements of the game
    private GameBoard gameBoard;
    // Creates and/or formats all the graphical elements of the game
    private GUI gui;

    // __ CONSTRUCTORS __
    public PuzzleGame() {
        gameBoard = new GameBoard();
        gui = new GUI();

    }

    /*public PuzzleGame(params) {

    }*/

    // __ FUNCTIONS __

    /**
     * Initiates the playing of the game
     */
    public abstract void play();

    /**
     * Creates a GameBoard using the information extracted from a selected file.
     * @return The created game board.
     */
    protected GameBoard loadGame() {
        GameBoard board;
        /*if (filePath.equals("none")) {
            board = new GameBoard();
        }
        else {
            //board = new GameBoard(filePath);
        }*/

        return gameBoard;
    }

    /**
     * Creates the GUI that will assemble and format all the graphical elements.
     * @return The frame that has been created.
     */
    protected abstract JFrame createGUI();

    // __ MUTATORS __

    protected void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    protected void setGui(GUI gui) {
        this.gui = gui;
    }


    // __ ACCESSORS __

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GUI getGui() {
        return gui;
    }
}
