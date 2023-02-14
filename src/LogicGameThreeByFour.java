/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 */

import javax.swing.*;
import java.awt.*;

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
public class LogicGameThreeByFour {
    // __ PRIVATE FIELDS __

    // __ CONSTRUCTORS __

    // __ FUNCTIONS __
    private void createGUI() {
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
    }

    // __ ACCESSORS __

    // __ OVERRIDES __
}
