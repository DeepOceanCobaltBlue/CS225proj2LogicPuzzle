/* Work Log
 * 2/16 [Andrew]- created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 * 2/18 [chris] - removed filepath from game board and added a parameterized constructor
 *              - updated class comment to reflect change, updated comments
 * 2/20 [phoenix] - added dialog and text area for error feedback
 * 2/20 [Andrew] -Updated class organization
 *               -added more documentation (comments)
 * 2/21 [phoenix] - created hint related methods
 *                - added comments for hint related methods
 *                - small formatting edits
 */

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/** Data structure for Logic Game assets
 * Contains all the textual information necessary to present to the player. Also
 * holds the Block objects that make up the game interface and the control
 * function buttons handled in the LogicGame class.
 */
public class GameBoard {
    // __ ATTRIBUTES __
    /* Clues are the initial information given to the player */
    private String[] clues;
    /* The correct relationships between the 3 categories */
    private String[] answers;
    /* Initial prompt to the player which will give context to the puzzle */
    private String story;
    /* User saved notes used in game */
    private String notes;
    /* Interactive game assets */
    private Block[][] blocks;
    /* Reference used to pass control buttons to GUI in order to display.
     * However, logic for controls are handled in the LogicGame class */
    private JButton[] controls;

    // __ CONSTRUCTORS __
    public GameBoard() {
        this.clues = null;
        this.answers = null;
        this.story = null;
        this.blocks = null;
        this.notes = null;
    }

    public GameBoard(String[] clue, String[] answers, String story, Block[][] blocks) {
        this.clues = clue;
        this.answers = answers;
        this.story = story;
        this.blocks = blocks;
        this.notes = null;
    }

    /**
     * Gets the indices for each Block in the 2D array 'blocks' with an error.
     * @param includeEmpty Indicates whether empty blocks should be included as an error.
     * @return An ArrayList of Integers. The indices for each block with an error.
     */
    public ArrayList<Integer> getErrorIndices(boolean includeEmpty) {
        ArrayList<Integer> errorIndices = new ArrayList<Integer>();

        for (int i = 0; i < 3; i++) {
            if (blocks[(i/2)%2][i%2].anyErrors(includeEmpty)) {
                errorIndices.add(i);
            }
        }
        return errorIndices;
    }

    /**
     * Calls display errors on every Block. Puts a red background on each Square with an error.
     */
    public void showAllErrors() {
        for (int index = 0; index < 3; index++) {
            blocks[(index/2)%2][index%2].displayErrors(true);
        }
    }

    /**
     * Calls displayErrors() on every Block with the indicated indices. Puts a red background on each Square with an error.
     * @param errorIndices Contains the indices for each Block in the 2D array 'blocks' with an error.
     */
    public void showAllErrors(ArrayList<Integer> errorIndices) {
        for (Integer index : errorIndices) {
            blocks[(index/2)%2][index%2].displayErrors(false);
        }
    }

    /**
     * Calls setEmptyCorrect on a random block with the indicated indices
     * @param errorIndices Contains the indices for each Block in the 2D array 'blocks' with an error.
     */
    public void revealSquareInBlock(ArrayList<Integer> errorIndices) {
        int randIndex = (int)(Math.random() * errorIndices.size());
        blocks[(errorIndices.get(randIndex)/2)%2][errorIndices.get(randIndex)%2].setEmptyCorrect();
    }

    /**
     * Called at the end of the game to disable components and display the users errors.
     */
    public void endGame() {
        for (Block[] blRow : blocks) {
            for (Block block : blRow) {
                if (block != null) {
                    block.displayErrors(true);
                    block.setClickable(false);
                }
            }
        }
    }
    // __ OVERRIDE __
    @Override
    public String toString() {
        return "Clues: " + Arrays.toString(clues) + ",\nstory: " + story + ",\nblocks" + Arrays.deepToString(blocks) +
                ",\nanswers: " + Arrays.toString(answers) + ",\ncontrols: " + Arrays.toString(controls) + ",\nnotes: " + notes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() == this.getClass()) {
            GameBoard gameBoard = (GameBoard) obj;
            return Arrays.equals(clues, gameBoard.getClues()) && story.equals(gameBoard.story) && Arrays.deepEquals(blocks, gameBoard.getBlocks()) &&
                    Arrays.equals(answers, getAnswers()) && Arrays.equals(controls, gameBoard.controls) && notes.equals(gameBoard.getNotes());
        }
        return false;
    }

    // __ ACCESSORS __
    public String[] getClues(){
        return this.clues;
    } //    returns the Clues in the given Array
    public String[] getAnswers() {
        return this.answers;
    } //    returns the Answers in the given Array
    public String getStory() {
        return this.story;
    } //    returns the Story String that is set
    public Block[][] getBlocks() {
        return blocks;
    } //    returns the 2D Array of Blocks on the GameBoard

    public JButton[] getControls() {
        return controls;
    } //    returns the controls for the JButtons

    public String getNotes() {
        return notes;
    } //    returns the notes set by the user on the GameBoard

    // __ MUTATORS __
    public void setNotes(String notes) {
        this.notes = notes;
    } //    sets the notes on the Gameboard
    public void setControls(JButton[] controls) {
        this.controls = controls;
    } // sets the JButton controls to match the controls set here


}
