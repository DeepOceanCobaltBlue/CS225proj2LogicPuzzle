/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 * 2/16 [phoenix] - declared fields
 *                - created method templates
 *                - wrote getters
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
    private Block[][] blocks;
    private long startTime;
    private final int rows = 2;
    private final int maxColumns = 2;
    private final int numButtons = 4;
    private JButton[] functionButtons;

    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        super();
        blocks = new Block[rows][maxColumns];
        startTime = System.currentTimeMillis();
        functionButtons = new JButton[numButtons];
    }

    /*public LogicGameThreeByFour(params) {

    }*/

    // __ FUNCTIONS __
    protected JFrame createGUI() {
        JFrame frame = new JFrame("Logic Game");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(750, 300));

        /* Font used by all text in game */
        Font baseFont = new Font("Arial", Font.BOLD, 16);

        /*
        * > frame
        *   > rootPane
        *     > gamePane - game board
        *     > controlPane - buttons
        *     > displayPane - display text to user
        */
        return frame;
    }

    /*private String importGameBoard() {

            Get file path to game board file from user.

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

    public void play() {
        //  Starts the game.
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
    public void actionPerformed(ActionEvent e) {

    }
}
