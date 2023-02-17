/* Work Log
 * 2/14 [chris] - created class
 * 2/18 [chris] - implemented UML requirements
 */

/**
 * Generic Puzzle Game assets include a GUI and the data structure containing
 * relevant game assets and information, the GameBoard. GUI is created using
 * the GameBoard.
 */
public abstract class PuzzleGame {
    // __ ATTRIBUTES __
    private GUI gui;
    private GameBoard gameBoard;

    // __ ACCESSORS __
    public GUI getGui() {
        return gui;
    }
    public void setGui(GUI gui) {
        this.gui = gui;
    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
