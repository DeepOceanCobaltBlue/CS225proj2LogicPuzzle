/* Work Log
 * 2/14 [chris] - created class and implemented UML requirements and documentation
 * 2/16 [chris] - created and implemented title panel methods including createRotatedImage()
 */

/*
* Construct and return the GUI for a logic game (3x4)
*/

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GUI {
    // FIELDS
    /* Reference to finished graphical interface */
    private JFrame rootFrame;
    /* initialized blocks array for creating game panels */
    private Block[][] blocks;

    // CONSTRUCTOR
    public GUI() {
        this.rootFrame = new JFrame(" Logic Puzzle Game ");
    }

    public GUI(Block[][] blocks) {
        this();
        this.blocks = blocks;
        createGUI();
    }

    // METHODS
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
        this.rootFrame.setLayout(new GridLayout(3, 3));
        /* blank panel attributes */
        JPanel blankPanel = new JPanel();
        // ~~~
        // ~~~

        JPanel topLeftPanel  = blankPanel;                          /* BLANK */
        JPanel topMidPanel   = createTopTitlePanel(blocks[0][0]);   /* Title */
        JPanel topRightPanel = createTopTitlePanel(blocks[0][1]);   /* Title */

        JPanel midLeftPanel  = createSideTitlePanel(blocks[1][0]);  /* Title */
        JPanel midMidPanel   = createBlockPanel(blocks[0][0]);      /* Block */
        JPanel midRightPanel = createBlockPanel(blocks[0][1]);      /* Block */

        JPanel botLeftPanel  = createSideTitlePanel(blocks[1][1]);  /* Title */
        JPanel botMidPanel   = createBlockPanel(blocks[1][0]);      /* Block */
        JPanel botRightPanel = blankPanel;                          /* BLANK */

        /* compose root frame */
        this.rootFrame.add(topLeftPanel );
        this.rootFrame.add(topMidPanel  );
        this.rootFrame.add(topRightPanel);

        this.rootFrame.add(midLeftPanel );
        this.rootFrame.add(midMidPanel  );
        this.rootFrame.add(midRightPanel);

        this.rootFrame.add(botLeftPanel );
        this.rootFrame.add(botMidPanel  );
        this.rootFrame.add(botRightPanel);
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
        JPanel subPanelOne = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanelOne.setBorder(new LineBorder(Color.BLACK));
        JLabel categoryLabel = new JLabel();
        categoryLabel.setIcon(new ImageIcon(createRotatedImage(block.getBlockRowTitle())));
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

            subPanelTwo.add(label);
        }

        basePanel.add(subPanelTwo);

        return basePanel;
    }

    /**
     * Creates a Panel to display the Row title information for each Block
     * @param block - The Block containing the titles to display
     * @return - completed display panel for this block
     */
    private JPanel createTopTitlePanel(Block block) {
        /*
        String[] temp = new String[] {"audi", "ford", "masarati12345678901234567890", "porsche"};
        block.setBlockRowTitle("Cars");
        block.setRowTitles(temp);
        END REMOVE
        */

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
            BufferedImage rotatedImage = createRotatedImage(title);
            /* create label to hold image */
            JLabel label = new JLabel();
            label.setBorder(new LineBorder(Color.BLACK));
            label.setPreferredSize(new Dimension(rotatedImage.getWidth(), rotatedImage.getHeight()));
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
    private BufferedImage createRotatedImage(String title) {
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

        /* Rotate image 90 degrees counter clockwise */
        AffineTransform at = new AffineTransform();
        at.translate(0, 80);
        at.rotate(-Math.PI / 2); // (pi / 2) = 90 degrees, (-) indicates direction of rotation - CCW
        g2d.setTransform(at);
        g2d.drawString(title, 3, 18);

        g2d.dispose();

        return rotatedImage;
    }

    // ACCESSORS
    /**
     * Once GUI is created this method will return the finalized root frame
     * to the application.
     * @return
     */
    public JFrame getDisplay() {
        return rootFrame;
    }
}
