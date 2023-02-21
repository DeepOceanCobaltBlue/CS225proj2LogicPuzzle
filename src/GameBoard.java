/* Work Log
 * 2/16 [Andrew]- created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 * 2/18 [chris] - removed filepath from game board and added a parameterized constructor
 *              - updated class comment to reflect change, updated comments
 * 2/20 [Andrew] -Updated class organization
 *               -added more documentation (comments)
 */

import javax.swing.*;
import java.util.ArrayList;

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

    private ArrayList<Block> findIncorrectBlocks(boolean includeEmpty) {
        ArrayList<Block> incBlocks = new ArrayList<Block>();

        for (Block[] blRow : blocks) {
            for (Block block : blRow)
                if (block != null) {
                    if (block.anyErrors(includeEmpty)) {
                        incBlocks.add(block);
                    }
                }
        }

        return incBlocks;
    }

    // GameBoard is our Block handler and should have to be used whenever LogicGame needs Block access.
    // TODO: Allow GiveHint to detect if there are any incorrect/empty squares in blocks.
    // TODO: Allow GiveHint to give the appropriate hint if squares are incorrect/empty.

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