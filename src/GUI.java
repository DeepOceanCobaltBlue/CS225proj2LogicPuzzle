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

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

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

    // TODO: walk through if this constructor was used, specifically looping importgame and menupanel
    public GUI(JButton[] controls) {
        this();
        this.controls = controls;
    }

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
     * Initialize all graphical components and construct the GUI
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
        subjectPanel.setBorder(new LineBorder(Color.GREEN));

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
                    blockTitles[a][b].setText("Category Name");
                } else {
                    blockTitles[a][b].setText("Subset name " + a);
                }
                subjectTextPanel.add(blockTitles[a][b]);
            }
        }

        subjectPanel.add(subjectTextPanel);

        /* Answer input components */
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBorder(new LineBorder(Color.GREEN));

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
        clueInput.setPreferredSize(new Dimension(300, 200));
        cluePanel.add(clueLabel);
        cluePanel.add(clueInput);
        /* Story input components */
        JPanel storyPanel = new JPanel();
        storyPanel.setBorder(new LineBorder(Color.BLACK));
        storyPanel.setLayout(new BoxLayout(storyPanel, BoxLayout.Y_AXIS));
        JLabel storyLabel = new JLabel("Story");
        JTextArea storyInput = new JTextArea();
        storyInput.setPreferredSize(new Dimension(300, 200));
        storyPanel.add(storyLabel);
        storyPanel.add(storyInput);
        /* Detail image */
        JPanel imagePanel = new JPanel(new GridLayout(1,1));
        JLabel imageDisplayLabel = new JLabel();
        imagePanel.add(imageDisplayLabel);

        inputPanel.add(subjectPanel);
        inputPanel.add(imagePanel);
        inputPanel.add(answerPanel);
        inputPanel.add(cluePanel);
        inputPanel.add(storyPanel);

        gameCreationRootPane.add(buttonPanel, BorderLayout.NORTH);
        gameCreationRootPane.add(inputPanel, BorderLayout.CENTER);
    }

    public void updateClock(String time) {
        timeValueLabel.setText(time);
    }
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
     * Once GUI is created this method will return the finalized root frame
     * to the application.
     * @return - the frame containing the all the graphical components for the application
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

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Get infoPanel components */
        JButton clicked = (JButton) e.getSource();
        //if(clicked.getParent() == this.rootFrame.getContentPane().)
        JPanel infoBtnPanel = (JPanel) clicked.getParent();
        JPanel infoPanel = (JPanel) infoBtnPanel.getParent();
        JTextArea ta = (JTextArea) infoPanel.getComponent(1);
        /* clear 'selected' button color then set 'clicked to 'selected'*/
        for (int i = 0; i < 3; i++) {
            infoBtnPanel.getComponent(i).setBackground(null);
        }
        clicked.setBackground(new Color(150, 255, 255, 200));

        if (wasOnNotes) {
            gameBoard.setNotes(ta.getText());
        }

        /* Alter TextArea depending on button selected */
        String text = "";
        switch (clicked.getText()) {
            case "Clues":
                String[] clues = gameBoard.getClues();
                for (int a = 0; a < clues.length; a++) {
                    text = text.concat(clues[a] + "\n");
                }
                wasOnNotes = false;
                ta.setEditable(false);
                break;
            case "Story":
                text = gameBoard.getStory();
                ta.setEditable(false);
                wasOnNotes = false;
                break;
            case "Notes":
                text = gameBoard.getNotes();
                ta.setEditable(true);
                wasOnNotes = true;
                break;
            case "Create":
                // make the game file
                break;
            case "Cancel":
                switchWindow("Menu");
                break;
        }

        ta.setText(text);

    }
}
