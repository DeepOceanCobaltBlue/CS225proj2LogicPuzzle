/* Work Log
 * 2/4 [chris]    - created class
 *                - wrote class comment
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
 * 2/18 [chris]   - Worked on fileReader and Text file formatting
 *
 * 2/18 [phoenix] - created findIncorrectBlocks()
 *                - changed giveHint behavior utilizing findIncorrectBlocks()
 *
 * 2/19 [phoenix] - small tweaks and moved most of findIncorrectBlocks() functionality to Block
 *                - moved functionality from abstract class
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** This class will manage top level functionality of the game.
 * - Handle game file I/O
 * - initialize game board
 * - launch game window
 * - handle control panel functions
 *   - hints, submit answers [new game, loading game boards]
 */
public class LogicGameThreeByFour implements ActionListener {
    // __ ATTRIBUTES __
    private GUI gui;
    private GameBoard gameBoard;
    /* Tracks the total time a player spends on a puzzle */
    private long startTime;
    private long endTime;
    /* Tracks to total time the player has spent on the current game */
    private Timer timer;
    private boolean runClock;
    /* Using the Hint feature penalizes the players total time */
    private long penaltyTime;
    /* Used outside the game area to change the state of the game */
    private JButton[] functionButtons;


    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        this.startTime = 0;
        this.endTime = 0;
        this.penaltyTime = 0;
        this.timer = new Timer(0, null);
        this.runClock = false;
        this.functionButtons = new JButton[2];

        String filepath = importGameBoard();    // get filepath to Game file
        fileReader(filepath);                   // read file and initialize game board
        createButtons();                        // Initialize control function buttons and pass to game board
        createGUI();                            // pass game board to GUI to initialize gui
    }

    public LogicGameThreeByFour(String filepath) {
        this.startTime = 0;
        this.endTime = 0;
        this.penaltyTime = 0;
        this.timer = new Timer(0, null);
        this.runClock = false;
        this.functionButtons = new JButton[2];

        fileReader(filepath);                   // read file and initialize game board
        createButtons();                        // Initialize control function buttons and pass to game board
        createGUI();                            // pass game board to GUI to initialize gui
    }

    // __ FUNCTIONS __
    /**
     * Initializes GUI using GameBoard object created from game file.
     */
    private void createGUI() {
        this.setGUI(new GUI(this.getGameBoard()));
    }

    private void clock() {
        if(runClock) {
            timer = new Timer(1000, e -> {
                long timeSeconds = (int)((System.currentTimeMillis() - getStartTime())/1000);
                String time = "Time: ";
                time = time.concat(String.valueOf(timeSeconds));
                time = time.concat(" Seconds");

                gui.updateClock(time);
            });

            timer.start();
        } else {
            timer.stop();
        }
    }


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
                    String allClues = "";
                    String nextClue = reader.nextLine();
                    while(!(nextClue.equals("STORY"))) {
                        allClues = allClues.concat(nextClue);
                        allClues = allClues.concat(",");
                        nextClue = reader.nextLine();
                    }
                    clues = allClues.split(",");
                    /* STORY title was just read */
                    story = reader.nextLine();
                    break;
                case "ANSWER":
                    String allAnswers = "";
                    String nextAnswer = reader.nextLine();
                    while(!(nextAnswer.equals("END"))) {
                        allAnswers = allAnswers.concat(nextAnswer);
                        allAnswers = allAnswers.concat(",");
                        nextAnswer = reader.nextLine();
                    }
                    answer = allAnswers.split(",");
                    break;
            }
        }
        this.setGameBoard(new GameBoard(clues, answer, story, blocks));
    }

    /**
     * Open dialogue box to choose game file
     * @return - path to game file
     */
    private String importGameBoard() {
        // TODO: No dialogue box yet, just hard code to the only game file
        return "Game Files\\Game1";
    }

    /**
     * Check if every Square of every Block has the correct state.
     * @return If correct state, return true, else return false.
     */
    private ArrayList<Block> findIncorrectBlocks(boolean includeEmpty) {
        ArrayList<Block> incBlocks = new ArrayList<Block>();
        Block currentBlock;

        for (int i = 0; i < 3; i++) {
            currentBlock = this.getGameBoard().getBlocks()[(i / 2) % 2][i % 2];
            if (currentBlock.anyErrors(includeEmpty)) {
                incBlocks.add(currentBlock);
            }
        }
        return incBlocks;
    }



    /**
     * TEMP: Similar to compareBoardToAnswer.
     * @param includeEmpty Determines whether empty Squares are included in the returned ArrayList
     * @return Each Square with a State mismatch,  optionally includes Squares with State.Empty.
     */
    /*private ArrayList<Square> findIncorrectBlocks(boolean includeEmpty) {
        ArrayList<Square> incSquares = new ArrayList<Square>();
        for (Block[] row : this.getGameBoard().getBlocks()) {
            for (Block block : row) {
                if (block != null) {
                    for (Square[] sqRow : block.getSquares()) {
                        for (Square square : sqRow) {
                            if (!square.isStateCorrect() && (!(square.getCurrentState() == Square.State.EMPTY) || includeEmpty)) {
                                incSquares.add(square);
                            }
                        }
                    }
                }
            }
        }
        return incSquares;
    }*/


    /**
     * Reveal an unrevealed or incorrect TRUE square
     */
    private void giveHint() {
        ArrayList<Block> incBlock;
        /* TODO: implement
           - cycle through Square to find a TRUE square that is EMPTY or FALSE
           - set the first Square found to TRUE
        */
        incBlock = findIncorrectBlocks(false);
        if (incBlock.size() > 0) {
            // TODO: currentBlock.displayError();
            this.penaltyTime += 60;
        } else {
            incBlock = findIncorrectBlocks(true);
            if (incBlock.size() > 0) {
                // TODO: currentBlock.setEmptyCorrect();
            } else {
                // User Has perfect board
            }
        }
    }


    /**
     * Initialize the buttons for the control functions of the game and pass it to the GameBoard
     */
    private void createButtons() {
        functionButtons[0] = new JButton("Submit Answers");
        functionButtons[0].addActionListener(this);
        functionButtons[1] = new JButton("Hint");
        functionButtons[1].addActionListener(this);
        this.getGameBoard().setControls(functionButtons);
    }

    public void play() {
        startTime = System.currentTimeMillis();
        // TODO: We can add a start button and have the gameboard initially disabled and clues hidden
        runClock = true; // allow timer to begin
        clock(); // start timer
        this.gui.getDisplay().setVisible(true);
    }

    // TODO: method for returning formatted time once submit button is clicked

    // __ OVERRIDES __

    /**
     * Handles necessary game logic relevant to the control functions used outside the
     * primary game area.
     *      Functions include:
     *          Give Hint:
     *          Submit Answers:
     * @param e - control button clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressedButton = (JButton) e.getSource();
        switch (pressedButton.getText()) {
            case "Submit":
                endTime = System.currentTimeMillis();
                runClock = false; // do not allow timer to run
                clock(); // stop timer
                if(findIncorrectBlocks(true).size() > 0) {
                    // Tell user they were correct
                }
                else {
                    // Tell user they were incorrect
                }
                break;
            case "Hint":
                giveHint();
                break;
        }
    }

    // __ MUTATORS __
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    // __ ACCESSORS __
    public long getStartTime() {
        return startTime;
    }
    public GUI getGUI() {
        return gui;
    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public long getEndTime() {
        return endTime;
    }
    public long getPenaltyTime() {
        return penaltyTime;
    }
}
