/* Work Log
 * 2/4 [chris]  - created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/** This class represents an individual junction between two categories within the game.
 * every square has a position within a 4x4 matrix which is managed by the parent 'Block'.
 * The categories depend on the game board loaded and could be anything. The square represents
 * a relationship between the two categories.
 * This relationship can be 1 of 3 possible states and have a corresponding character that will
 * be displayed on the button. The corresponding int value is listed next to each State for use
 * when creating a Square using the (int rowIndex, int colIndex, __int state__) constructor.
 *      EMPTY   - (   ) == 0
 *      TRUE    - ( O ) == 1
 *      FALSE   - ( X ) == 2
 */
public class Square extends JButton {
    /* State is used to track the logical condition of the square */
    enum State {EMPTY, TRUE, FALSE}

    // __ PRIVATE FIELDS __
    /* Row index value of square within parent 'Block' */
    private int rowIndex;
    /* Column index value of square within parent 'Block' */
    private int colIndex;
    /* What state has the user set this square to */
    private State currentState;
    /* The intended state in accordance with the imported game board */
    private State correctState;
    private Image[] images;

    // __ CONSTRUCTORS __
    public Square() {
        rowIndex = 0;
        colIndex = 0;
        currentState = State.EMPTY;
        correctState = State.EMPTY;
        this.setPreferredSize(new Dimension(30, 30));
        setDisplay(State.EMPTY);
    }

    /**
     *
     * @param rowIndex - index for the row housing this square
     * @param colIndex - index for the column housing this square
     * @param correctState - 0 = EMPTY, 1 = TRUE, 2 = FALSE
     */
    public Square(int rowIndex, int colIndex, State correctState) {
        this();
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.correctState = correctState;
    }


    // __ FUNCTIONS __

    /** Change graphic displayed on this 'Square' to represent the
     * desired state.
     * @param state - determines character to display
     */
    private void setDisplay(State state) {
        /*
        // retrieve rendered image
        Image displayImage = new BufferedImage(10, 10, 2); // 2 = ARGB
        Graphics2D g2d = (Graphics2D)displayImage.getGraphics();
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));

        // update image based on desired state
        switch(newState) {
            case EMPTY:
                g2d.drawString("-", 0, 0); // TODO: change to " " after testing cycling displayImage
                break;
            case FALSE:
                g2d.setColor(Color.red);
                g2d.drawString("X", 0, 0);
            case TRUE:
                g2d.setColor(Color.green);
                g2d.drawString("O", 0, 0);
        }

        // update
        this.setIcon(new ImageIcon(displayImage));
        this.repaint(); // TODO: test?? is this necessary

         */
        switch(state) {
            case EMPTY:
                this.setIcon(new ImageIcon());
                break;
            case FALSE:
                this.setIcon(new ImageIcon(images[0]));
                break;
            case TRUE:
                this.setIcon(new ImageIcon(images[1]));
                break;
        }
    }

    /** Change current state to the next state in the cycle.
     * Cycle: EMPTY > FALSE > TRUE > EMPTY(repeat)
     */
    public void nextState() {
        switch(this.currentState) {
            case EMPTY:
                this.currentState = State.FALSE;
                break;
            case FALSE:
                this.currentState = State.TRUE;
                break;
            case TRUE:
                this.currentState = State.EMPTY;
                break;
        }
        setDisplay(this.currentState);
    }

    /** Used by parent 'Block' to assess if player has this 'Square' correctly set.
     *
     * @return if current state = correct state.
     */
    public boolean isStateCorrect() {
        return(this.currentState == this.correctState);
    }

    // __ ACCESSORS __

    public State getCurrentState() { return this.currentState; }

    public void setCurrentState(State newState) {
        this.currentState = newState;
        setDisplay(this.currentState);
    }
    public State getCorrectState() {
        return correctState;
    }
    public int getRowIndex() {
        return rowIndex;
    }
    public int getColIndex() {
        return colIndex;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    // __ OVERRIDES __

}
