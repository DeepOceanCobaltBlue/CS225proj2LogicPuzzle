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
    /* Reference game instance graphical requirements */
    private GameBoard gameBoard;

    // CONSTRUCTOR
    public GUI() {
        this.rootFrame = new JFrame(" Logic Puzzle Game ");
    }

    public GUI(GameBoard board) {
        this();
        this.gameBoard = board;
        createGUI();
    }

    // METHODS

    /**
     * Initialize all graphical components and construct the GUI
     * base panel will hold all child panels
     * base panel has 9 square child panels
     *          Left     Mid    Right
     *  Top   | BLANK | title | title |
     *  Mid   | title | block | block |
     *  Bot   | title | block | BLANK |
     *  Panels labeled as Row x Col => TopLeftPanel (is the blank panel in the top left corner)
     *
     */
    private void createGUI() {
        /* Font used to display titles */
        Font baseFont = new Font("Arial", Font.PLAIN, 14);

        JPanel topLeftPanel  = new JPanel(); /* BLANK */
        JPanel topMidPanel   = createTopTitlePanel(); /* Title */
        JPanel topRightPanel = createTopTitlePanel(); /* Title */

        JPanel midLeftPanel  = createSideTitlePanel(); /* Title */
        JPanel midMidPanel   = new JPanel(); /* Block */
        JPanel midRightPanel = new JPanel(); /* Block */

        JPanel botLeftPanel  = createSideTitlePanel(); /* Title */
        JPanel botMidPanel   = new JPanel(); /* Block */
        JPanel botRightPanel = new JPanel(); /* BLANK */

    }

    private static JPanel createSideTitlePanel(Block block) {
        /* root panel */
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));

        /* Subject Title */
        // TODO: fix top title to pull column titles
        JPanel subPanelOne = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanelOne.setBorder(new LineBorder(Color.BLACK));
        JLabel categoryLabel = new JLabel();
        categoryLabel.setIcon(new ImageIcon(createRotatedImage(block.getBlockRowTitle())));
        subPanelOne.add(categoryLabel);
        basePanel.add(subPanelOne);

        /* row/column individual titles */
        JPanel subPanelTwo = new JPanel(new GridLayout(4, 1));
        String[] titles = block.getRowTitles();// TODO: fix top title to pull column titles

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
    private static JPanel createTopTitlePanel(Block block) {
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
    private static BufferedImage createRotatedImage(String title) {
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
    public JFrame getDisplay() {
        return rootFrame;
    }
}
