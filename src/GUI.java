/* Work Log
 * 2/14 [chris] - created class and implemented UML requirements and documentation
 * 2/16 [chris] - created and implemented title panel methods including createRotatedImage()
 * 2/18 [chris] - GUI gamePanel completed
 * 2/19 [chris] - infoPanel and controlPanel completed
 *              - added timer
 * 2/20 [chris] - added menu
 *              - added leaderboard
 *              - added more game files( game2, game3, game4)
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Construct a Graphical interface for a 3x4 Logic Puzzle using GameBoard data structure.
 */
public class GUI implements ActionListener{
    // __ ATTRIBUTES __
    /* Reference to finished graphical interface */
    private JFrame rootFrame;
    /* initialized blocks array for creating game panels */
    private Block[][] blocks;
    // TODO: add gameboard attribute, remove block[][] attribute
    private GameBoard gameBoard;
    /* Notes text area in info panel stores user generated text */
    private boolean wasOnNotes;
    /* Updated on 1 second intervals to display total time the player has been attempting current puzzle */
    private JLabel timeValueLabel;

    /* Reference used to pass control buttons to GUI in order to display.
     * However, logic for controls are handled in the LogicGame class */
    private JButton[] controls;
    private JPanel menuRootPane;
    private JPanel gameRootPane;
    private JPanel gameCreationRootPane;


    // __ CONSTRUCTORS __
    public GUI() {
        this.rootFrame = new JFrame(" Logic Puzzle Game ");
        // Frame settings
        this.rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.rootFrame.setMinimumSize(new Dimension(1000, 480));
        this.rootFrame.setVisible(true);
        this.gameBoard = null;
        this.wasOnNotes = false;
        this.controls = null;

    }

    public GUI(JButton[] controls) {
        this();
        this.controls = controls;
    }

    /**
     * Swaps the root panel displayed as the frame content pane to move between
     * menu windows.
     * @param name - Name of the window you want to swap to. Name is independent of
     *             any object property.
     */
    public void switchWindow(String name) {
        switch(name) {
            case "Creation":
                this.rootFrame.setContentPane(this.gameCreationRootPane);
                break;
            case "Menu":
                this.rootFrame.setContentPane(this.menuRootPane);
                break;
            case "Game":
                this.rootFrame.setContentPane(this.gameRootPane);
                break;
        }
        this.rootFrame.pack();
        this.rootFrame.revalidate();
        this.rootFrame.repaint();
        this.rootFrame.setVisible(true);
    }

    // __ FUNCTIONS __

    /**
     * Constructs the graphical interface for the opening menu root panel.
     * this panel is the first thing displayed upon launching the application.
     */
    private void createMenuInterface() {
        /* bottom layer of menu */
        menuRootPane = new JPanel();
        menuRootPane.setLayout(new BoxLayout(menuRootPane, BoxLayout.Y_AXIS));

        /* Button panel for menu options */
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 20, 0);

        /* initialize and compose menu panel components */
        JButton newGameButton = this.controls[2];
        newGameButton.setPreferredSize(new Dimension(350, 50));
        constraints.gridx = 0;
        constraints.gridy = 0;
        menuPanel.add(newGameButton, constraints);

        JButton leaderButton = this.controls[3];
        leaderButton.setPreferredSize(new Dimension(350, 50));
        constraints.gridx = 0;
        constraints.gridy = 1;
        menuPanel.add(leaderButton, constraints);

        JButton createButton = this.controls[4];
        createButton.setPreferredSize(new Dimension(350, 50));
        constraints.gridx = 0;
        constraints.gridy = 2;
        menuPanel.add(createButton, constraints);

        menuRootPane.add(menuPanel);
    }
    /**
     * Initialize all graphical components and construct the GUI of the game window
     * root frame will hold all child panels
     * root frame has 9 square child panels
     *          Left     Mid    Right
     *  Top   | BLANK | title | title |
     *  Mid   | title | block | block |
     *  Bot   | title | block | BLANK |
     *  Panels labeled as Row x Col => TopLeftPanel (is the blank panel in the top left corner)
     *
     */
    private void createGameInterface() {
        gameRootPane = new JPanel(new BorderLayout());

        /* Houses the control function buttons for the player to interact with */
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(200, 480));
        controlPanel.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(1,1,1,3)));

        /* initialize controlPanel components */
        JButton hintButton = this.controls[0];
        hintButton.setText("Get Hint");

        JButton submitButton = this.controls[1];
        submitButton.setText("Submit");

        JTextArea feedbackTextArea = new JTextArea();
        feedbackTextArea.setPreferredSize(new Dimension((int)controlPanel.getPreferredSize().getWidth(), 200));
        feedbackTextArea.setBorder(new LineBorder(Color.black));
        feedbackTextArea.setText("~~~~~~~~~~~~~~~~~~~~~~~~~~~\n How to play\n~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                ">> 1) Click buttons to change their state.\n\n>> 2) A red X represents a False relationship and a " +
                "green circle represents a True relationship between the row and column subjects. \n\n>> 3) Fill the " +
                "entire board and hit submit to see if you correctly answered the logic puzzle.\n\n>> 4) If you get " +
                "stuck, ask for a hint(above) and an incorrect square will be highlighted or a correct square will be " +
                "filled in for you!\n\n>> 5) You can only hit submit once so logic your way to the solution and have fun!");
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        feedbackTextArea.setEditable(false);

        /* compose controlPanel */
        controlPanel.add(hintButton, BorderLayout.NORTH);
        controlPanel.add(feedbackTextArea, BorderLayout.CENTER);
        controlPanel.add(submitButton, BorderLayout.SOUTH);

        /* Used to display information to the player */
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(250, 480));
        infoPanel.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(1,3,1,1)));

        JPanel infoButtonPanel = new JPanel();
        infoButtonPanel.setLayout(new BoxLayout(infoButtonPanel, BoxLayout.X_AXIS));

        Font buttonFont = new Font("Arial", Font.BOLD, 10);

        /* initialize infoPanel components */
        JButton clueButton = new JButton("Clues");
        clueButton.addActionListener(this);
        clueButton.setBackground(new Color(150, 255, 255, 200));
        clueButton.setPreferredSize(new Dimension(50, 35));
        clueButton.setFont(buttonFont);

        JButton storyButton = new JButton("Story");
        storyButton.addActionListener(this);
        storyButton.setPreferredSize(new Dimension(30, 35));
        storyButton.setFont(buttonFont);

        JButton notesButton = new JButton("Notes");
        notesButton.addActionListener(this);
        notesButton.setPreferredSize(new Dimension(30, 35));
        notesButton.setFont(buttonFont);

        /* TODO: reinsert if an answer field is implemented
        JButton answerButton = new JButton("Answer");
        answerButton.addActionListener(this);
        answerButton.setPreferredSize(new Dimension(30, 35));
        answerButton.setFont(buttonFont);
         */

        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setPreferredSize(new Dimension((int)infoPanel.getPreferredSize().getWidth(), 300));
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setEditable(false);

        JPanel timerPanel = new JPanel(new GridLayout(1, 1));
        timeValueLabel = new JLabel("Time: 0 Seconds");
        timeValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timeValueLabel);

        /* compose infoPanel */
        infoButtonPanel.add(clueButton, BorderLayout.NORTH);
        infoButtonPanel.add(storyButton, BorderLayout.NORTH);
        infoButtonPanel.add(notesButton, BorderLayout.NORTH);
        //infoButtonPanel.add(answerButton, BorderLayout.NORTH);

        infoPanel.add(infoButtonPanel, BorderLayout.NORTH);
        infoPanel.add(infoTextArea, BorderLayout.CENTER);
        infoPanel.add(timerPanel, BorderLayout.SOUTH);

        /* Holds all components related to game Blocks used for play */
        JPanel gamePanel = new JPanel();
        gamePanel.setBorder(new LineBorder(Color.black));
        gamePanel.setLayout(new GridLayout(3,3));

        /* Initialize gamePanel components*/
        JPanel topLeftPanel  = new JPanel();                        /* BLANK */
        JPanel topMidPanel   = createTopTitlePanel(blocks[0][0]);   /* Title */
        JPanel topRightPanel = createTopTitlePanel(blocks[0][1]);   /* Title */

        JPanel midLeftPanel  = createSideTitlePanel(blocks[0][1]);  /* Title */
        JPanel midMidPanel   = createBlockPanel(blocks[0][0]);      /* Block */
        JPanel midRightPanel = createBlockPanel(blocks[0][1]);      /* Block */

        JPanel botLeftPanel  = createSideTitlePanel(blocks[1][0]);  /* Title */
        JPanel botMidPanel   = createBlockPanel(blocks[1][0]);      /* Block */
        JPanel botRightPanel = new JPanel();                        /* BLANK */

        /* compose gamePanel */
        gamePanel.add(topLeftPanel );
        gamePanel.add(topMidPanel  );
        gamePanel.add(topRightPanel);

        gamePanel.add(midLeftPanel );
        gamePanel.add(midMidPanel  );
        gamePanel.add(midRightPanel);

        gamePanel.add(botLeftPanel );
        gamePanel.add(botMidPanel  );
        gamePanel.add(botRightPanel);

        for(Component panel : gamePanel.getComponents()) {
            panel.setPreferredSize(new Dimension(120,120));
        }

        /* Initialize infoTextArea text by simulating clicking the "Clues" button */
        clueButton.doClick();

        /* compose interface for game */
        gameRootPane.add(controlPanel, BorderLayout.WEST);
        gameRootPane.add(gamePanel, BorderLayout.CENTER);
        gameRootPane.add(infoPanel, BorderLayout.EAST);
    }

    /**
     * Constructs the graphical interface for the game file creation root
     * panel. The panel is used if the player selects to create a new game
     * file.
     */
    private void createGameCreationInterface() {
        gameCreationRootPane = new JPanel(new BorderLayout());

        /* initialize and compose button panel components */
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(new LineBorder(Color.CYAN));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.addActionListener(this);
        JButton createButton = new JButton("Create");
        createButton.setPreferredSize(new Dimension(100, 30));
        createButton.addActionListener(this);
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(createButton, BorderLayout.EAST);

        /* initialize and compose input panel components */
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,2));
        inputPanel.setBorder(new LineBorder(Color.BLACK));

        /* Subject and row input components*/
        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.setBorder(new LineBorder(Color.BLACK));

        // panel to hold title
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setBorder(new LineBorder(Color.BLACK));
        JLabel label1 = new JLabel("Title headers and sub-categories");
        titlePanel1.add(label1);
        subjectPanel.add(titlePanel1);
        // panel to hold labels
        JPanel subjectLabelPanel = new JPanel(new GridLayout(1,3));
        JLabel subjectLabel1 = new JLabel("Category 1  ");
        JLabel subjectLabel2 = new JLabel("Category 2  ");
        JLabel subjectLabel3 = new JLabel("Category 3");
        subjectLabelPanel.add(subjectLabel1);
        subjectLabelPanel.add(subjectLabel2);
        subjectLabelPanel.add(subjectLabel3);
        subjectPanel.add(subjectLabelPanel);

        // panel to hold textareas
        JPanel subjectTextPanel = new JPanel(new GridLayout(5,3));
        JTextArea[][] blockTitles = new JTextArea[5][3];
        for(int a = 0; a < blockTitles.length; a++) {
            for(int b = 0; b < blockTitles[a].length; b++) {
                blockTitles[a][b] = new JTextArea();
                blockTitles[a][b].setBorder(new LineBorder(Color.BLACK));
                blockTitles[a][b].setPreferredSize(new Dimension(100, 25));
                if(a == 0) {
                    blockTitles[a][b].setText("Title");
                } else {
                    blockTitles[a][b].setText("Row " + a);
                }
                subjectTextPanel.add(blockTitles[a][b]);
            }
        }

        subjectPanel.add(subjectTextPanel);

        /* Answer input components */
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBorder(new LineBorder(Color.BLACK));

        // panel to hold title
        JPanel titlePanel2 = new JPanel();
        titlePanel2.setBorder(new LineBorder(Color.BLACK));
        JLabel title2 = new JLabel("Answer key");
        titlePanel2.add(title2);
        answerPanel.add(titlePanel2);
        // panel to hold labels
        JPanel answerLabelPanel = new JPanel(new GridLayout(1,3));
        answerLabelPanel.setBorder(new LineBorder(Color.BLACK));
        JLabel answerLabel1 = new JLabel(" Category 1  ");
        JLabel answerLabel2 = new JLabel(" Category 2  ");
        JLabel answerLabel3 = new JLabel(" Category 3");
        answerLabelPanel.add(answerLabel1);
        answerLabelPanel.add(answerLabel2);
        answerLabelPanel.add(answerLabel3);
        answerPanel.add(answerLabelPanel);

        // panel to hold textareas
        JPanel answerTextPanel = new JPanel(new GridLayout(5,3));
        JTextArea[][] answerTitles = new JTextArea[4][3];
        for(int a = 0; a < answerTitles.length; a++) {
            for(int b = 0; b < answerTitles[a].length; b++) {
                answerTitles[a][b] = new JTextArea();
                answerTitles[a][b].setBorder(new LineBorder(Color.BLACK));
                answerTitles[a][b].setPreferredSize(new Dimension(100, 25));
                answerTextPanel.add(answerTitles[a][b]);
            }
        }

        answerPanel.add(answerTextPanel);

        /* Clue input components */
        JPanel cluePanel = new JPanel();
        cluePanel.setBorder(new LineBorder(Color.BLACK));
        cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
        JLabel clueLabel = new JLabel("Clues");
        JTextArea clueInput = new JTextArea();
        clueInput.setLineWrap(true);
        clueInput.setText("Clues must be delineated by new lines and start with numbers i.e. 1) Clue ~~~~ [new line]");
        clueInput.setPreferredSize(new Dimension(300, 200));
        cluePanel.add(clueLabel);
        cluePanel.add(clueInput);
        /* Story input components */
        JPanel storyPanel = new JPanel();
        storyPanel.setBorder(new LineBorder(Color.BLACK));
        storyPanel.setLayout(new BoxLayout(storyPanel, BoxLayout.Y_AXIS));
        JLabel storyLabel = new JLabel("Story");
        JTextArea storyInput = new JTextArea();
        storyInput.setLineWrap(true);
        storyInput.setText("Add story as a single line, no new line character except at the end.");
        storyInput.setPreferredSize(new Dimension(300, 200));
        storyPanel.add(storyLabel);
        storyPanel.add(storyInput);
        /* Detail image */
        JPanel imagePanel = new JPanel(new GridLayout(1,1));
        JLabel imageDisplayLabel = new JLabel();
        try {
            Image detailImage = ImageIO.read(new File("Square Images\\Detail.png"));
            imageDisplayLabel.setIcon(new ImageIcon(detailImage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imagePanel.add(imageDisplayLabel);

        inputPanel.add(subjectPanel);
        inputPanel.add(imagePanel);
        inputPanel.add(answerPanel);
        inputPanel.add(cluePanel);
        inputPanel.add(storyPanel);

        gameCreationRootPane.add(buttonPanel, BorderLayout.NORTH);
        gameCreationRootPane.add(inputPanel, BorderLayout.CENTER);
    }

    /**
     * sets the text parameter of the JLabel used to display the running
     * time of the game.
     * @param time
     */
    public void updateClock(String time) {
        timeValueLabel.setText(time);
    }

    /**
     * creates the non-game board dependent windows and sets the Menu
     * panel as the first window displayed.
     */
    public void start() {
        createMenuInterface();
        createGameCreationInterface();
        switchWindow("Menu");
    }

    /**
     * Creates a panel to hold all the Square objects contained by this Block.
     * Squares are laid out in a 4x4 matrix that matches the matrix[][] field
     * in the Block Class.
     * @param block - create a Black Panel to display this Block
     * @return - completed panel
     */
    private JPanel createBlockPanel(Block block) {
        JPanel blockPanel = new JPanel();
        blockPanel.setLayout(new GridLayout(4, 4));

        for(int a = 0; a < 4; a++) { // rows
            for(int b = 0; b < 4; b++) { // columns
                blockPanel.add(block.getSquare(a, b));
            }
        }

        return blockPanel;
    }

    /**
     * Creates a Panel to display the column title information for each Block
     * @param block - The Block containing the titles to display
     * @return - completed display panel for this block
     */
    private JPanel createSideTitlePanel(Block block) {
        /* root panel */
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));

        /* Subject Title */
        JPanel subPanelOne = new JPanel();
        subPanelOne.setBorder(new LineBorder(Color.BLACK));

        JLabel categoryLabel = new JLabel();
        categoryLabel.setIcon(new ImageIcon(createRotatedImage(block.getBlockRowTitle(), 0, 10)));
        subPanelOne.add(categoryLabel);
        basePanel.add(subPanelOne);

        /* row/column individual titles */
        JPanel subPanelTwo = new JPanel(new GridLayout(4, 1));
        String[] titles = block.getRowTitles();

        for(int i = 0; i < 4; i++) {
            String title = titles[i];
            JLabel label = new JLabel(title);
            label.setBorder(new LineBorder(Color.BLACK));
            label.setPreferredSize(new Dimension(80, 30));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);

            subPanelTwo.add(label);
        }

        basePanel.add(subPanelTwo, BorderLayout.CENTER);

        return basePanel;
    }

    /**
     * Creates a Panel to display the Row title information for each Block
     * @param block - The Block containing the titles to display
     * @return - completed display panel for this block
     */
    private JPanel createTopTitlePanel(Block block) {
        /* root panel */
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));

        /* Subject Title */
        JPanel subPanelOne = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanelOne.setBorder(new LineBorder(Color.BLACK));
        JLabel categoryLabel = new JLabel(block.getBlockColTitle());
        subPanelOne.add(categoryLabel);
        basePanel.add(subPanelOne);

        /* row/column individual titles */
        JPanel subPanelTwo = new JPanel(new GridLayout(1, 4));
        String[] titles = block.getColumnTitles();

        for(int i = 0; i < 4; i++) {
            String title = titles[i];
            /* create image containing title string */
            BufferedImage rotatedImage = createRotatedImage(title, 3, 18);
            /* create label to hold image */
            JLabel label = new JLabel();
            label.setBorder(new LineBorder(Color.BLACK));
            label.setPreferredSize(new Dimension(rotatedImage.getWidth(), rotatedImage.getHeight()));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setIcon(new ImageIcon(rotatedImage));

            subPanelTwo.add(label);
        }

        basePanel.add(subPanelTwo);

        return basePanel;
    }

    /** Helper method
     * Used by createSideTitlePanel(Block block) &
     *         createTopTitlePanel(Block block)
     * methods to create an image with the text rotated 90 degrees.
     * @param title - The Title that needs to be rotated
     * @return - Image containing the text rotated 90 degrees
     */
    private BufferedImage createRotatedImage(String title, int xOffset, int yOffset) {
        int fontSize = 14;
        Font font = new Font("Arial", Font.BOLD, fontSize);

        /* Temporary image to generate FontMetrics object */
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();

        /* determine pixel width of title string. Text will be rotated next
         * therefore width will become the height */
        int height = metrics.stringWidth(title);

        /* modify font size until pixel width is less than 80 or font size is reduced to 12 pts */
        while (height > 80 && fontSize > 12) {
            // reduce font size
            fontSize--;
            font = new Font("Arial", Font.BOLD, fontSize);

            // reassess pixel width of string
            metrics = g2d.getFontMetrics();
            height = metrics.getHeight();
        }

        // Create the rotated image with the correct dimensions
        BufferedImage rotatedImage = new BufferedImage(30, 80, BufferedImage.TYPE_INT_ARGB);
        g2d = rotatedImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        //g2d.fillRect(0, 0, rotatedImage.getWidth(), rotatedImage.getHeight());


        /* Rotate image 90 degrees counter clockwise */
        AffineTransform at = new AffineTransform();
        at.translate(0, 80);
        at.rotate(-Math.PI / 2); // (pi / 2) = 90 degrees, (-) indicates direction of rotation - CCW
        g2d.setTransform(at);
        g2d.drawString(title, xOffset, yOffset);

        g2d.dispose();

        return rotatedImage;
    }

    // __ ACCESSORS __
    /**
     * Once GUI is created this method will return the root frame to the application.
     * @return - the frame containing the content pane that displays the application
     */
    public JFrame getDisplay() {
        return rootFrame;
    }
    // __ MUTATORS __
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.blocks = gameBoard.getBlocks();
        createGameInterface();
    }
    public void setControls(JButton[] functionButtons) {
        this.controls = functionButtons;
    }

    private JTextArea getInfoPanel(JButton button) {
        /* Get infoPanel components */
        JPanel infoBtnPanel = (JPanel) button.getParent();
        JPanel infoPanel = (JPanel) infoBtnPanel.getParent();
        /* clear 'selected' button color then set 'clicked to 'selected'*/
        for (int i = 0; i < 3; i++) {
            infoBtnPanel.getComponent(i).setBackground(null);
        }
        button.setBackground(new Color(150, 255, 255, 200));
        return (JTextArea) infoPanel.getComponent(1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        JTextArea ta;
        String text = "";

        switch (clicked.getText()) {
            case "Clues":
                ta = getInfoPanel(clicked);
                if (wasOnNotes) {
                    gameBoard.setNotes(ta.getText());
                }

                String[] clues = gameBoard.getClues();
                for (int a = 0; a < clues.length; a++) {
                    text = text.concat(clues[a] + "\n");
                }
                wasOnNotes = false;
                ta.setEditable(false);
                ta.setText(text);
                break;
            case "Story":
                ta = getInfoPanel(clicked);
                if (wasOnNotes) {
                    gameBoard.setNotes(ta.getText());
                }

                text = gameBoard.getStory();
                ta.setEditable(false);
                wasOnNotes = false;
                ta.setText(text);
                break;
            case "Notes":
                ta = getInfoPanel(clicked);

                text = gameBoard.getNotes();
                ta.setEditable(true);
                wasOnNotes = true;
                ta.setText(text);
                break;
            case "Create": // create a new game file from data input into game creation window
                createGameFile();
                break;
            case "Cancel": // cancel creating a new game file
                switchWindow("Menu");
                break;
        }

    }

    /**
     * creates a new game file from inputs on game creation window.
     * no protection or error checking.
     */
    private void createGameFile() {
        File directory = new File("Game files");
        int count = directory.listFiles().length;
        String filepath = ("Game" + (count + 1));
        File newGameFile = new File(filepath);
        try {
            newGameFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PrintWriter pw;
        try {
            pw = new PrintWriter(newGameFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(pw != null) {
            JPanel inputComponents = (JPanel) this.gameCreationRootPane.getComponent(1); // input panel

            /* Component hierarchy
            inputComponents
                0 - subjectPanel
                    0 - titlePanel1
                    1 - subjectLabelPanel
                    2 - subjectTextPanel
                        [0,1,2] titles
                        [3,6,9,12] - cat 1
                        [4,7,10,13] - cat 2
                        [5,8,11,14] - cat 3
                1 - imagePanel
                2 - answerPanel
                     0 - titlePanel2
                     1 - answerLabelPanel
                     2 - answerTextPanel
                        [0,1,2] titles
                        [3,6,9,12] - cat 1
                        [4,7,10,13] - cat 2
                3 - cluePanel
                    0 - clueLabel
                    1 - clueInput
                4 - storyPanel
                    0 - storyLabel
                    1 - storyInput
             */
            /* Get Answer Information */
            JPanel aPanel = (JPanel) inputComponents.getComponent(2);
            JPanel taAnswerPanel = (JPanel) aPanel.getComponent(2);
            String[] answerTextAreaInputs = new String[12];
            for(int i = 0; i < answerTextAreaInputs.length; i++) {
                answerTextAreaInputs[i] = ((JTextArea)taAnswerPanel.getComponent(i)).getText();
            }
            String[] trueRows = new String[4];
            trueRows[0] = (answerTextAreaInputs[0] + "," + answerTextAreaInputs[1] + "," + answerTextAreaInputs[2]);
            trueRows[1] = (answerTextAreaInputs[3] + "," + answerTextAreaInputs[4] + "," + answerTextAreaInputs[5]);
            trueRows[2] = (answerTextAreaInputs[6] + "," + answerTextAreaInputs[7] + "," + answerTextAreaInputs[8]);
            trueRows[3] = (answerTextAreaInputs[9] + "," + answerTextAreaInputs[10] + "," + answerTextAreaInputs[11]);

            /* get Blocks information */
            JPanel sPanel = (JPanel) inputComponents.getComponent(0);
            JPanel taSubjectPanel = (JPanel) sPanel.getComponent(2);
            String[] subjectTextAreaInputs = new String[15];
            for(int i = 0; i < subjectTextAreaInputs.length; i++) {
                subjectTextAreaInputs[i] = ((JTextArea)taSubjectPanel.getComponent(i)).getText();
            }
            String blockRowTitle;
            String rowTitlesStr;
            String blockColTitle;
            String colTitlesStr;

            /* Block 1 */
            blockRowTitle = subjectTextAreaInputs[0];   // CAT 1 subject TITLE
            rowTitlesStr = ( subjectTextAreaInputs[3] + "," + // Cat 1 row titles
                                    subjectTextAreaInputs[6] + "," +
                                    subjectTextAreaInputs[9] + "," +
                                    subjectTextAreaInputs[12]);

            blockColTitle = subjectTextAreaInputs[1];   // CAT 2 subject TITLE
            colTitlesStr = ( subjectTextAreaInputs[4] + "," + // Cat 1 col titles
                                    subjectTextAreaInputs[7] + "," +
                                    subjectTextAreaInputs[10] + "," +
                                    subjectTextAreaInputs[13]);
            // BLOCK 1 TRUE ROW
            pw.write("BLOCKS");

            pw.write(blockRowTitle);
            pw.write(rowTitlesStr);
            pw.write(blockColTitle);
            pw.write(colTitlesStr);
            pw.write(trueRows[0]);

            /* Block 2 */
            blockRowTitle = subjectTextAreaInputs[0];// CAT 1 ROW TITLE
            rowTitlesStr = (    subjectTextAreaInputs[3] + "," + // Cat 1 row titles
                                subjectTextAreaInputs[6] + "," +
                                subjectTextAreaInputs[9] + "," +
                                subjectTextAreaInputs[12]);

            blockColTitle = subjectTextAreaInputs[2];// CAT 3 COL TITLE
            colTitlesStr = (    subjectTextAreaInputs[5] + "," + // Cat 3 col titles
                                subjectTextAreaInputs[8] + "," +
                                subjectTextAreaInputs[11] + "," +
                                subjectTextAreaInputs[14]);
            // BLOCK 2 TRUE ROW

            pw.write(blockRowTitle);
            pw.write(rowTitlesStr);
            pw.write(blockColTitle);
            pw.write(colTitlesStr);
            pw.write(trueRows[1]);

            /* Block 3 */
            blockRowTitle = subjectTextAreaInputs[2];// CAT 3 ROW TITLE
            rowTitlesStr = (    subjectTextAreaInputs[3] + "," + // Cat 1 row titles
                                subjectTextAreaInputs[6] + "," +
                                subjectTextAreaInputs[9] + "," +
                                subjectTextAreaInputs[12]);

            blockColTitle = subjectTextAreaInputs[1];// CAT 2 COL TITLE
            colTitlesStr = (    subjectTextAreaInputs[4] + "," + // Cat 1 col titles
                                subjectTextAreaInputs[7] + "," +
                                subjectTextAreaInputs[10] + "," +
                                subjectTextAreaInputs[13]);
            // BLOCK 3 TURE ROWS
            pw.write(blockRowTitle);
            pw.write(rowTitlesStr);
            pw.write(blockColTitle);
            pw.write(colTitlesStr);
            pw.write(trueRows[2]);

            pw.write("CLUES");
            // DELINEATE CLUES BY NEW LINES

            pw.write("STORY");
            // ONE LINE

            pw.write("ANSWER");
            // DELINEATE BY ,

            pw.write("END");


        }
    }
}
