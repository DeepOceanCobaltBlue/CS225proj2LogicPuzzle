/* Work Log
 * 2/14 [chris] - created class
 * 2/16 [andrew] - implemented UML design for class
 */
public class GameBoard {

    //  16 sqaures in game
    //  3 blocks
    //  attributes
    private String[] clues = new String[4];
    private String[] answers = new String[3];
    private String filePath = "";

    //  methods
    public String[] getClues(){
        return this.clues;
    }
    public String[] getAnswers(){
        return answers;
    }
    public String getFilePath(){
        return filePath;
    }
    public void setFilePath(String desiredPath){
        this.filePath = desiredPath;
    }

}
