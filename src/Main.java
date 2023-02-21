/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 * 2/21 [Andrew] -updated class with comments in the main method
 */

/**
 * Main class will house the main method and launch a new instance of the logic game
 */
public class Main {
    public static void main(String[] args) {
        LogicGameThreeByFour game = new LogicGameThreeByFour(); //  creating a new instance of the game
        game.play(); // executing the game for user to play
    }
}
