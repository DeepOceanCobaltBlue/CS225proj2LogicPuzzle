/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 *
 * 2/16 [phoenix] - declared fields
 *                - created method templates
 *                - wrote getters
 *
 * 2/16 [phoenix] - built basic functionality of methods
 *                - wrote field comments
 *
 * 2/17 [phoenix] - continued work on basic functionality
 *                - wrote method comments
 *
 * 2/18 [chris]   = Worked on fileReader and Text file formatting
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/** This class will manage top level functionality of the game.
 * Functions:
 * - create GUI and display to user
 * - initialize game assets
 * - handle control panel contents
 *   - Buttons, new game, loading game boards, file I/O
 * - handle win condition logic
 * - handle player feedback
 *   - hints, clues, etc.
 */
public class LogicGameThreeByFour extends PuzzleGame implements ActionListener {
    // __ PRIVATE FIELDS __

    // 2D array of all blocks
    // private Block[][] blocks;
    // Holds the time in milliseconds when the game starts
    private long startTime;
    // The number of rows of blocks for a 3x4 game
    // private final int rowCount = 2;
    // The maximum number of columns of blocks for a 3x4 game
    // private final int columnCount = 2;
    // Array of buttons that perform essential game functions like submitting answers
    private JButton[] functionButtons;

    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        super();
        startTime = 0;
        //blocks = new Block[rowCount][columnCount];
        String filepath = importGameBoard();
        fileReader(filepath);
        functionButtons = new JButton[2];
        createButtons();
    }

    // __ FUNCTIONS __

    /**
     * Extract and initialize game assets from game file
     * @param filepath - the game file text document to read
     */
    private void fileReader(String filepath) {
        File inputFile = new File(filepath);
        Scanner reader;
        try {
            reader = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Block[][] blocks = new Block[2][2];
        String story = null;
        String[] clues = null;
        String[] answer = null;
        /* Read in all text from Game File to initialize block objects and gameBoard */
        while(reader.hasNext()) {

            String inputLine = reader.nextLine();
            switch(inputLine) {
                case "BLOCKS":
                    for(int i = 0; i < 3; i++) {
                        String rowTitle = reader.nextLine();
                        String[] rowTitles = reader.nextLine().split(",");
                        String columnTitle = reader.nextLine();
                        String[] colTitles = reader.nextLine().split(",");
                        String trueRows = reader.nextLine();

                        switch (i) {
                            case 0:
                                blocks[0][0] = new Block(rowTitle, rowTitles, columnTitle, colTitles, trueRows);
                                break;
                            case 1:
                                blocks[0][1] = new Block(rowTitle, rowTitles, columnTitle, colTitles, trueRows);
                                break;
                            case 2:
                                blocks[1][0] = new Block(rowTitle, rowTitles, columnTitle, colTitles, trueRows);
                                break;
                        }
                    }

                    break;
                case "CLUES":
                    String allClues = reader.nextLine();
                    while(!(reader.nextLine().equals("STORY"))) {
                        allClues = allClues.concat(",");
                        allClues = allClues.concat(reader.nextLine());
                    }
                    clues = allClues.split(",");

                    break;
                case "STORY":
                    story = reader.nextLine();
                    break;
                case "ANSWER":
                    String allAnswers = reader.nextLine();
                    while(reader.hasNext()) {
                        allAnswers = allAnswers.concat(",");
                        allAnswers = allAnswers.concat(reader.nextLine());
                    }
                    answer = allAnswers.split(",");
                    break;
            }
        }
        super.setGameBoard(new GameBoard(clues, answer, story, blocks));
    }

    /**
     * Open dialogue box to choose game file
     * @return
     */
    private String importGameBoard() {
        return "\\Game Files\\Game1";
    }

    /**
     * Check if every Square of every Block has the correct state.
     * @return If correct state, return true, else return false.
     */
    private boolean compareBoardToAnswer() {
        // TODO: super.getGameBoard().getBlocks();
        // TODO: boolean boardCorrect = true;
        for (Block[] row : blocks) {
            for (Block block : row) {
                // TODO: if(!(block.getCurrentState == block.getCorrectState())) { boardCorrect = false; }

                //if (block != null) {
                    // block.checkSquares();?

            }
        }
        return false;
    }

    /*private ArrayList<Square> findIncorrectBlocks() {
            Similar to compareBoardToAnswer except each Square with a state mismatch is recorded in a list and returned.
    }*/


    /**
     * Reveal an unrevealed or incorrect TRUE square
     */
    private void giveHint() {
        /* TODO: implement
           - cycle through Square to find a TRUE square that is EMPTY or FALSE
           - set the first Square found to TRUE
         */

        Random rand = new Random();
        int randBlock = rand.nextInt(3);
        int randSquare = rand.nextInt(16);
        Block selBlock = blocks[(randBlock/2)%2][randBlock%2];
        /*Square[][] selMatrix = selBlock.getMatrix();
        selMatrix[(randSquare/4)%4][randSquare%4];*/

    }

    /**
     * Initialize the buttons for the control functions of the game and pass it to the GameBoard
     */
    private void createButtons() {
        functionButtons[0] = new JButton("Submit Answers");
        functionButtons[0].addActionListener(this);
        functionButtons[1] = new JButton("Hint");
        functionButtons[1].addActionListener(this);
        super.getGameBoard().setControls(functionButtons);
    }

    public void play() {
        // TODO: I dont think this method is needed unless we add features later.
        setGameBoard(loadGame());
        createGUI(/*Buttons?*/);
        startTime = System.currentTimeMillis();

    }

    // __ OVERRIDES __
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        int index = 0;
        while(!(functionButtons[index].equals(pressedButton))) {
            index += 1;
        }
        switch (index) {
            case 0:
                /*if(compareBoardToAnswer()) {
                    Tell user they were correct
                }
                else {
                    Tell user they were incorrect
                }*/
                break;
            case 1:
                // giveHint();
                break;
        }
    }


    // __ ACCESSORS __
    public long getStartTime() {
        return startTime;
    }

    public JButton[] getFunctionButtons() {
        return functionButtons;
    }
}
