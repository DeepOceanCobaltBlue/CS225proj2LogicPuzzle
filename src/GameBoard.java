/* Work Log
 * 2/16 [Andrew]  - created class
 *              - wrote class comment
 *              - wrote version 1.0 of the class and added documentation
 */


/* Gameboard.java Comment
This class will be used by the PuzzleGame in order to properly build our Puzzle for the
user to play. This class contains arrays of clues and answers, as well as the filepath
that will be read in by the application in order to obtain said clues & answers.
This class will store all of this information so the PuzzleGame class may instantiate a new,
fully furnished Gameboard for the user to use.
 */
public class GameBoard {

    //  16 sqaures in game
    //  3 blocks

    //  attributes
    private String[] clues = new String[4]; //  Array to hold clues
    private String[] answers = new String[3]; // Array to hold answers
    private String filePath = ""; //    String to contain the filePath

    //  methods
    public String[] getClues(){ //  getter for the clues array
        return this.clues;
    }
    public String[] getAnswers(){ //    getter for the answers array
        return answers;
    }
    public String getFilePath(){ // getter for the filepath directory
        return filePath;
    }
    public void setFilePath(String desiredPath){ // setter for the filepath directory
        this.filePath = desiredPath;
    }

}
