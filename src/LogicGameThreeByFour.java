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
 * 2/20 [chris]   - refactored some methods to accommodate new code for new GUI items
 * 2/21 [Andrew] - updated class with documentation and overall cleanup
 * 2/21 [phoenix] - Finished hint system
 *                - Finished file error system
 *                - small formatting edits
 * 2/21 [Andrew]  -updated hint feedback text
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
 * - handle functions from buttons that change the state of the application
 */
public class LogicGameThreeByFour implements ActionListener {
    // __ ATTRIBUTES __
    private GUI gui;
    private GameBoard gameBoard;
    /* Tracks the total time a player spends on a puzzle */
    private int currentTime;
    private Timer timer;
    private boolean runClock;
    /* Using the Hint feature penalizes the players total time */
    private int penaltyTime;
    /* Used outside the game area to change the state of the game */
    private JButton[] functionButtons;


    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        this.currentTime = 0;
        this.penaltyTime = 0;
        this.timer = new Timer(0, null);
        this.runClock = false;
        this.functionButtons = new JButton[5];
        this.gameBoard = null;
        createButtons();
        this.gui = new GUI(functionButtons);
    }

    public LogicGameThreeByFour(GameBoard gameBoard) {
        this();
        this.gameBoard = gameBoard;
    }

    // __ FUNCTIONS __

    /**
     * Controls the start and stop of the in game timer
     */
    private void clock() {
        if(runClock) {
            timer = new Timer(1000, e -> {
                currentTime += 1;
                String time = "Time: ";
                time = time.concat(String.valueOf(currentTime));
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
     * @param inputFile - the game file text document to read
     */
    private void fileReader(File inputFile) throws FileNotFoundException, NullPointerException, RuntimeException {
        Scanner reader = null;
        try {
            reader = new Scanner(inputFile);
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
                    default:
                        throw new FileFormatException();
                }
            }

            this.setGameBoard(new GameBoard(clues, answer, story, blocks));
            createButtons();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }


    }

    /**
     * Open file selection window and retrieve game file.
     * No error check or safety for selecting a file that isn't a game file.
     */
    private File importGameBoard() {
        JFileChooser fileChooser = new JFileChooser("Game Files");
        int result = fileChooser.showOpenDialog(this.gui.getDisplay().getContentPane());
        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Check if every Square of every Block has the correct state.
     * @return If correct state, return true, else return false.
     */

    /**
     * Reveal an unrevealed or incorrect TRUE square
     */
    private void giveHint() {
        ArrayList<Integer> errBlockIndices = gameBoard.getErrorIndices(false);

        if (errBlockIndices.size() > 0) {
            gameBoard.showAllErrors(errBlockIndices);
        } else {
            errBlockIndices = gameBoard.getErrorIndices(true);
            if (errBlockIndices.size() > 0) {
                gameBoard.revealSquareInBlock(errBlockIndices);
            } else {
                gui.setFeedbackTAText("The current status of the puzzle board is 100% correct. \n\n Please click the submit button to view your results!");
            }
        }

    }


    /**
     * Functions that affect the state of the Game, including Block operations,
     * are handled in this class and passed to GUI to be displayed.
     */
    private void createButtons() {
        functionButtons[0] = new JButton("Submit Answers");
        functionButtons[0].addActionListener(this);
        functionButtons[1] = new JButton("Hint");
        functionButtons[1].addActionListener(this);
        functionButtons[2] = new JButton("Start Game");
        functionButtons[2].addActionListener(this);
        functionButtons[3] = new JButton("Leaderboard");
        functionButtons[3].addActionListener(this);
        functionButtons[4] = new JButton("Create Board");
        functionButtons[4].addActionListener(this);
    }

    /**
     * calls the GUI method to initialize gui windows and set the frame content pane
     * to the first window(Menu)
     */
    public void play() {
        // display main menu
        this.gui.start();
    }

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
        JButton clicked = (JButton) e.getSource();

        switch (clicked.getText()) {
            case "Submit":
                boolean win = gameBoard.getErrorIndices(true).size() == 0;
                runClock = false; // do not allow timer to run
                float finalTime = (currentTime + penaltyTime)/60;
                clock(); // stop timer
                gui.setFeedbackTAText("~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" + (win ? "You Won!" : "You Lost." ) + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n" +
                        (win ? "You took " + currentTime + " seconds to solve the puzzle, with " + penaltyTime + " additional seconds added for hints.\nYour total time is " +
                                (currentTime+penaltyTime) + " seconds, or " + (finalTime) + " minutes." : "The mistakes are shown on the board.") + "\n\n\n Thanks for playing!");
                gameBoard.showAllErrors();
                gameBoard.endGame();
                break;
            case "Hint":
                giveHint();
                penaltyTime += 60;
                break;
            case "Start Game":
                try {
                    if (gameBoard == null) {
                        fileReader(importGameBoard());// get game file and initialize game board
                    }
                    this.gui.setGameBoard(this.gameBoard); // creates game interface and switches display to game
                    this.gui.switchWindow("Game");
                    this.gui.getDisplay().revalidate();
                    this.gui.getDisplay().repaint();
                    currentTime = 0;
                    runClock = true; // allow timer to begin
                    clock(); // start timer
                } catch (NullPointerException exception) {
                    // User exited the dialog, no action required.
                } catch (FileNotFoundException exception) {
                    gui.displayError("There was an error finding your file. Please ensure the chosen file has not been deleted or moved.");
                } catch (RuntimeException exception) {
                    gui.displayError("There was an error loading your file. Please ensure it has been formatted properly.");
                }
                break;
            case "Leaderboard":
                break;
            case "Create Board":
                this.gui.switchWindow("Creation");
                this.gui.getDisplay().revalidate();
                this.gui.getDisplay().repaint();
                break;

        }
    }

    // __ MUTATORS __
    public void setGUI(GUI gui) { // sets the GUI to the desired object reference
        this.gui = gui;
    }
    public void setGameBoard(GameBoard gameBoard) { // sets the GameBoard to the desired object reference
        this.gameBoard = gameBoard;
    }

    // __ ACCESSORS __
    public GUI getGUI() { //    returns the current GUI object
        return gui;
    }
    public GameBoard getGameBoard() { //    returns the current GameBoard object
        return gameBoard;
    }
    public int getCurrentTime() { //   returns the time on the stopwatch
        return currentTime;
    }
    public int getPenaltyTime() { //   returns the penaltyTimer recorded by the stopwatch
        return penaltyTime;
    }
    public JButton[] getFunctionButtons() {
        return functionButtons;
    }
    public Timer getTimer() {
        return timer;
    }
}
