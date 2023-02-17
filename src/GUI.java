/*
* Construct and return the GUI for
* */

import javax.swing.*;
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
    private void createGUI() {
        /* Holds a row of blocks */
        JPanel rowOneOfBlocks = new JPanel();
        /*  */
        JPanel rowOneFull = new JPanel();
        /*  */
        JPanel rowTwoOfBlocks = new JPanel();
        /*  */
        JPanel rowTwoFull = new JPanel();
        /*  */
        JPanel blocksPanel = new JPanel();

    }

    // ACCESSORS
    public JFrame getDisplay() {
        return rootFrame;
    }
}
