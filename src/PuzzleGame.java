/* Work Log
 * 2/14 [chris] - created class
 *
 * 2/16 [phoenix] - refactored class name
 *                - declared fields
 *                - wrote getters
 *                - wrote field comments
 *
 * 2/18 [chris] - implemented UML requirements
 *
 * 2/18 [phoenix] - replaced gui with guiFrame
 *                - added play()
 */

import javax.swing.*;

/**
 * Generic Puzzle Game assets include a GUI and the data structure containing
 * relevant game assets and information, the GameBoard. GUI is created using
 * the GameBoard.
 */
public abstract class PuzzleGame {
    // __ ATTRIBUTES __
    private JFrame guiFrame;
    private GameBoard gameBoard;

    // __ FUNCTIONS __

    public abstract void play();
    // __ ACCESSORS __
    public JFrame getFrame() {
        return guiFrame;
    }
    public void setFrame(JFrame guiFrame) {
        this.guiFrame = guiFrame;
    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
