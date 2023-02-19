/* Work Log
 * 2/16 [Andrew]- created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 * 2/18 [chris] - removed filepath from gameboard and added a parameterized constructor
 *              - updated class comment to reflect change, updated comments
 */

import javax.swing.*;

/** Data structure of Game data and assets
 * This class will be used by the PuzzleGame in order to properly build our Puzzle for the
 * user to play. This class contains arrays of clues and answers,
 * This class will store all of this information so the PuzzleGame class may instantiate a new,
 * fully furnished GameBoard for the user to use.
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
     *  However, logic for controls are handled in the LogicGame class */
    private JButton[] controls;

    // __ CONSTRUCTORS __
    public GameBoard() {
        this.clues = new String[4];
        this.answers = new String[3];
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

    // __ ACCESSORS __
    public String[] getClues(){
        return this.clues;
    }
    public String[] getAnswers(){
        return this.answers;
    }
    public String getStory() {
        return this.story;
    }
    public Block[][] getBlocks() {
        return blocks;
    }
    public void setControls(JButton[] controls) {
        this.controls = controls;
    }
    public JButton[] getControls() {
        return controls;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
