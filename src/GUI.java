/* Work Log
 * 2/14 [chris] - created class and implemented UML requirements and documentation
 * 2/16 [chris] - created and implemented title panel methods including createRotatedImage()
 * 2/18 [chris] - GUI gamePanel completed
 * 2/19 [chris] - infoPanel and controlPanel completed
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


    // __ CONSTRUCTORS __
    public GUI() {
        this.rootFrame = new JFrame(" Logic Puzzle Game ");
        this.wasOnNotes = false;
    }

    public GUI(GameBoard gb) {
        this();
        this.gameBoard = gb;
        this.blocks = gb.getBlocks();
        createGUI();
    }

    // __ FUNCTIONS __
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
    private void createGUI() {
        /* Houses the control function buttons for the player to interact with */
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(1,1,1,3)));

        /* initialize controlPanel components */
        JButton hintButton = gameBoard.getControls()[0];
        hintButton.setText("Get Hint");

        JButton submitButton = gameBoard.getControls()[1];
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
        infoPanel.setPreferredSize(new Dimension(250, 600));
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

        /* compose infoPanel */
        infoButtonPanel.add(clueButton, BorderLayout.NORTH);
        infoButtonPanel.add(storyButton, BorderLayout.NORTH);
        infoButtonPanel.add(notesButton, BorderLayout.NORTH);
        //infoButtonPanel.add(answerButton, BorderLayout.NORTH);

        infoPanel.add(infoButtonPanel, BorderLayout.NORTH);
        infoPanel.add(infoTextArea, BorderLayout.CENTER);

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

        // Frame settings
        this.rootFrame.setLayout(new BorderLayout());
        this.rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.rootFrame.setMinimumSize(new Dimension(900, 480));

        /* compose frame */
        this.rootFrame.add(controlPanel, BorderLayout.WEST);
        this.rootFrame.add(gamePanel, BorderLayout.CENTER);
        this.rootFrame.add(infoPanel, BorderLayout.EAST);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Get infoPanel components */
        JButton clicked = (JButton)e.getSource();
        JPanel infoBtnPanel = (JPanel) clicked.getParent();
        JPanel infoPanel = (JPanel) infoBtnPanel.getParent();
        JTextArea ta = (JTextArea) infoPanel.getComponent(1);
        /* clear 'selected' button color then set 'clicked to 'selected'*/
        for(int i = 0; i < 3; i++ ) {
            infoBtnPanel.getComponent(i).setBackground(null);
        }
        clicked.setBackground(new Color(150, 255, 255, 200));

        if(wasOnNotes) {
            gameBoard.setNotes(ta.getText());
        }

        /* Alter TextArea depending on button selected */
        String text = "";
        switch(clicked.getText()) {
            case "Clues":
                String[] clues = gameBoard.getClues();
                for(int a = 0; a < clues.length; a++) {
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
        }

        ta.setText(text);

    }
}
