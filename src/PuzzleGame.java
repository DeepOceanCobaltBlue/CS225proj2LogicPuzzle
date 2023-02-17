/* Work Log
 * 2/14 [chris] - created class
 * 2/18 [chris] - implemented UML requirements
 */

public abstract class PuzzleGame {
    // __ ATTRIBUTES __
    private GUI gui;
    private GameBoard gameBoard;

    // __ FUNCTIONS __

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
