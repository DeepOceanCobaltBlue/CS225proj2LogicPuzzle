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
 * 2/18 [chris]   - Worked on fileReader and Text file formatting
 */

import javax.swing.*;
import java.awt.*;
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
    // __ ATTRIBUTES __
    /* Tracks the total time a player spends on a puzzle */
    private long startTime;
    private long endTime;
    /* Used outside the game area to change the state of the game */
    private JButton[] functionButtons;
    /* Used to control the display of the GUI created from GameBoard */
    private JFrame guiFrame;

    // __ CONSTRUCTORS __
    public LogicGameThreeByFour() {
        this.startTime = 0;
        this.endTime = 0;
        this.functionButtons = new JButton[2];

        String filepath = importGameBoard();    // get filepath to Game file
        fileReader(filepath);                   // read file and initialize game board
        createButtons();                        // Initialize control function buttons and pass to game board
        createGUI();                            // pass game board to GUI to initialize gui and get root Frame

    }

    // __ FUNCTIONS __

    /**
     * Initializes GUI using GameBoard object created from game file.
     * Retrieves the root frame to be displayed
     */
    private void createGUI() {
        GUI gui = new GUI(super.getGameBoard());
        super.setGui(gui);
        this.guiFrame = super.getGui().getDisplay();
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
        super.setGameBoard(new GameBoard(clues, answer, story, blocks));
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
        // TODO: This method should start the game by creating the window to display the GUI

        startTime = System.currentTimeMillis();
        guiFrame.setVisible(true);
        // constructor creates gui, now display it using this.guiFrame and display it

    }

    // TODO: method for returning formatted time once submit button is clicked

    // __ OVERRIDES __

    /**
     * Handles necessary game logic relevant to the control functions used outside the
     * primary game area.
     *      Functions include:
     *          New Game:
     *          Submit Answers:
     * @param e
     */
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
