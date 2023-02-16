/* Work Log
 * 2/14 [chris] - created class and implemented UML requirements and documentation
 */

/*
* Construct and return the GUI for a logic game (3x4)
*/

import javax.swing.*;

import java.awt.*;

import static javafx.scene.input.KeyCode.J;

public class GUI {
    // FIELDS
    /* Reference to finished graphical interface */
    private JFrame rootFrame;
    /* Reference game instance graphical requirements */
    private GameBoard gameBoard;

    // CONSTRUCTOR
    public GUI() {
        this.rootFrame = new JFrame(" Logic Puzzle Game ");
    }

    public GUI(GameBoard board) {
        this();
        this.gameBoard = board;
        createGUI();
    }

    // METHODS

    /**
     * Initialize all graphical components and construct the GUI
     *
     * base panel will hold all child panels
     * base panel has 9 square child panels
     *          Left     Mid    Right
     *  Top   | BLANK | title | title |
     *  Mid   | title | block | block |
     *  Bot   | title | block | BLANK |
     *
     *  Panels labeled as Row x Col => TopLeftPanel (is the blank panel in the top corner)
     *
     */
    private void createGUI() {
        /* Font used to display titles */
        Font baseFont = new Font("Arial", Font.PLAIN, 14);

        JPanel topLeftPanel  = new JPanel(); /* BLANK */
        JPanel topMidPanel   = new JPanel(); /* Title */
        JPanel topRightPanel = new JPanel(); /* Title */

        JPanel midLeftPanel  = new JPanel(); /* Title */
        JPanel midMidPanel   = new JPanel(); /* Block */
        JPanel midRightPanel = new JPanel(); /* Block */

        JPanel botLeftPanel  = new JPanel(); /* Title */
        JPanel botMidPanel   = new JPanel(); /* Block */
        JPanel botRightPanel = new JPanel(); /* BLANK */








    }

    // ACCESSORS
    public JFrame getDisplay() {
        return rootFrame;
    }
}
