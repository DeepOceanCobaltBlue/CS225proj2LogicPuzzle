/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 * 2/16 [phoenix] - declared fields
 *                - created method templates
 *                - wrote getters
 *
 * 2/16 [phoenix] - built basic functionality of methods
 *                - wrote field comments
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/** This class will manage top level functionality of the game.
 * Functions:
 * - create GUI and display to user
 * - initialize game assets
 * - handle control panel contents
 *   - Buttons, new game, loading game boards, file I/O
 * - handle win condition logic
 * - handle player feedback
 *   - hints, clues, etc.
 */
public class LogicGameThreeByFour extends PuzzleGame implements ActionListener {
    // __ PRIVATE FIELDS __

    // 2D array of all blocks
    private Block[][] blocks;
    // Holds the time in milliseconds when the game starts
    private long startTime;
    // The number of rows of blocks for a 3x4 game
    private final int rows = 2;
    // The maximum number of columns of blocks for a 3x4 game
    private final int maxColumns = 2;
    // The buttons that perform essential game functions like submitting answers
    private JButton[] functionButtons;

    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        super();
        startTime = 0;
        blocks = new Block[rows][maxColumns];
        functionButtons = new JButton[2];
        createButtons();
    }

    /*public LogicGameThreeByFour(params) {

    }*/

    // __ FUNCTIONS __


    /*private String importGameBoard() {


    }*/

    /*private boolean compareBoardToAnswer() {
            Check if every Square of every Block has the correct state. If yes, return true, else return false.
    }*/

    /*private ArrayList<Square> findIncorrectBlocks() {
            Similar to compareBoardToAnswer except each Square with a state mismatch is recorded in a list and returned.
    }*/



    /*private void giveHint() {
            Swaps the state of a single random Square with an incorrect state to match the correct state.
    }*/

    private void createButtons() {
        functionButtons[0] = new JButton("Submit Answers");
        functionButtons[0].addActionListener(this);
        functionButtons[1] = new JButton("Hint");
        functionButtons[1].addActionListener(this);
    }


    // __ ACCESSORS __
    public Block[][] getBlocks() {
        return blocks;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getRows() {
        return rows;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public JButton[] getFunctionButtons() {
        return functionButtons;
    }

    // __ OVERRIDES __
    @Override
    protected JFrame createGUI() {
        setGui(new GUI(getGameBoard()));
        return getGui().getDisplay();
        /*JFrame frame = new JFrame("Logic Game");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(750, 300));

        /* Font used by all text in game *
        Font baseFont = new Font("Arial", Font.BOLD, 16);

        /*
         * > frame
         *   > rootPane
         *     > gamePane - game board
         *     > controlPane - buttons
         *     > displayPane - display text to user
         *
        return frame;*/
    }

    @Override
    public void play() {
        setGameBoard(loadGame("none"));
        createGUI();
        startTime = System.currentTimeMillis();

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
