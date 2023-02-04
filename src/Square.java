/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 */

import javax.swing.*;

/** This class represents an individual junction between two categories within the game.
 * every square has a position within a 4x4 matrix which is managed by the parent 'Block'.
 * The categories depend on the game board loaded and could be anything. The square represents
 * a relationship between the two categories.
 * This relationship can be 1 of 3 possible states and have a corresponding character that will
 * be displayed on the button.
 *      EMPTY   - (   )
 *      TRUE    - ( O )
 *      FALSE   - ( X )
 */
public class Square extends JButton {

}
