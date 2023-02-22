/* Work Log
 * 2/4 [chris]  - created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 * 2/18 [chris] - minor alterations to properly initialize Squares
 * 2/19 [chris] - Updated documentation to reflect changes
 * 2/20 [Andrew] - Updated class with comments for each method provided
 *                -updated methods to match
 * 2/21 [Phoenix] -added toString and equals
 * 2/21 [Andrew] - adjusted documentation
 */

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/** This class represents an individual junction between two categories within the game.
 * every square has a position within a 4x4 matrix which is managed by the parent 'Block'.
 * The categories depend on the game board loaded and could be anything. The square represents
 * a relationship between the two categories.
 * This relationship can be 1 of 3 possible states and have a corresponding image that will
 * be displayed on the button.
 *      EMPTY   - a blank button (no image)
 *      TRUE    - a green circle
 *      FALSE   - a red 'X'
 */
public class Square extends JButton {
    /* State is used to track the logical condition of the square */
    enum State {EMPTY, TRUE, FALSE}

    // __ ATTRIBUTES __
    /* Row index value of square within parent 'Block' */
    private int rowIndex;
    /* Column index value of square within parent 'Block' */
    private int columnIndex;
    /* What state has the user set this square to */
    private State currentState;
    /* The intended state in accordance with the imported game board */
    private State correctState;
    /* currentState images */
    private Image[] images;

    // __ CONSTRUCTORS __
    public Square() { //    default constructor
        rowIndex = 0;
        columnIndex = 0;
        currentState = State.EMPTY;
        correctState = State.EMPTY;
        this.setPreferredSize(new Dimension(30, 30));
        setDisplay(State.EMPTY);
    }

    /**
     * @param rowIndex - index for the row housing this square
     * @param columnIndex - index for the column housing this square
     * @param correctState - EMPTY, TRUE, FALSE: retrieved from game file
     */
    public Square(int rowIndex, int columnIndex, State correctState) { //  constructor with parameters
        this();
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.correctState = correctState;
    }


    // __ FUNCTIONS __
    /** Change graphic displayed on this 'Square' to represent the
     * desired state.
     * @param state - determines character to display
     */
    private void setDisplay(State state) {
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

    /**
     * Used by parent 'Block' to assess if player has this 'Square' correctly set.
     * @return is Square correctly set by player
     */
    public boolean isStateCorrect() {
        return(this.currentState == this.correctState);
    }

    // __ ACCESSORS __
    public State getCurrentState() { return this.currentState; } // retrieves the current state of the selected Square
    public State getCorrectState() {
        return correctState;
    } //    retrieves the correct state of the selected Square
    public int getRowIndex() {
        return rowIndex;
    } //    retrieves the row the selected Square is in
    public int getColumnIndex() {
        return columnIndex;
    } //    retrieves the column the selected Square is in

    public Image[] getImages() { return images;} //     retrieves the images this square can display

    // __ MUTATORS __
    public void setCurrentState(State newState) { //    sets the state of the selected Square
        this.currentState = newState;
        setDisplay(this.currentState);
    }
    public void setImages(Image[] images) {
        this.images = images;
    } //    sets the image of the selected Square

    @Override
    public String toString() { //   toString method to print out each attribute of this object
        return "Column Index: " + columnIndex + ", row index:" + rowIndex + ", current state: " + currentState +
                ", correct state " + correctState;

    }

    @Override
    public boolean equals(Object obj) { //  equals method to check if two objects of the same type are identical
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() == this.getClass()) {
            Square square = (Square) obj;
            // Images not included as that will be checked in Square.
            return rowIndex == square.getRowIndex() && columnIndex == square.getColumnIndex() &&
                    currentState.equals(square.currentState) && correctState.equals(square.correctState) &&
                    Arrays.equals(images, square.getImages());
        }
        return false;
    }
}
