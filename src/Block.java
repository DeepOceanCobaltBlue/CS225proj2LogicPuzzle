/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** This class represents a 4x4 matrix of 'Squares' and handles interactions
 * between the user and any square within the matrix.
 *
 */
public class Block implements ActionListener {
    // __ PRIVATE FIELDS __
    private Square[][] matrix;

    // __ CONSTRUCTORS __
    public Block() {
        matrix = new Square[2][2];
    }

    // __ FUNCTIONS __
    private void changeSquareToTrue() {}
    private void changeSquareToFalse() {}
    private void changeSquareToEmpty() {}

    // __ ACCESSORS __

    // __ OVERRIDES __

    /** This method handles interactions between the user and any square within the
     * matrix. This is done by calling specific methods based on the state of the square
     * clicked by the user.
     * Only squares within the matrix have ActionEvents linked to them.
     *
     * @param e - 'Square' clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Square clicked = (Square)e.getSource();
    }
}
