/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 * 2/14 [chris] - worked on implementation of UML requirements and documentation
 * 2/18 [chris] - updated initialization constructor for title information and squares
 * 2/19 [chris] - implemented cycling images for buttons
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/** This class represents a 4x4 matrix of 'Squares' and handles interactions
 * between the user and any square within the matrix.
 *
 */
public class Block implements ActionListener {
    // __ PRIVATE FIELDS __
    /* matrix of Square objects controlled by this block */
    private Square[][] matrix;
    /* Subject title for row titles */
    private String blockRowTitle;
    /* Subject title for column titles */
    private String blockColTitle;
    /* Subject title for each row */
    private String[] rowTitles;
    /* Subject title for each column */
    private String[] columnTitles;

    private static Image[] images;
    private static Image imgFalse;
    private static Image imgTrue;

    // __ CONSTRUCTORS __
    public Block() {
        matrix = new Square[4][4];
        blockRowTitle = null;
        blockColTitle  = null;
        rowTitles = new String[4];
        columnTitles = new String[4];
    }

    public Block(String rowTitle, String[] rowTitles, String columnTitle, String[] columnTitles, String order) {
        this();
        this.blockRowTitle = rowTitle;
        this.blockColTitle = columnTitle;
        this.rowTitles = rowTitles;
        this.columnTitles = columnTitles;

        /* Index is the column, value is the row, containing TRUE square */
        String[] tempTrueRows = order.split(",");
        int[] TrueRows = new int[4];
        for(int i = 0; i < 4; i++) {
            TrueRows[i] = (Integer.parseInt(tempTrueRows[i]) - 1);
        }
        initSquares(TrueRows);
    }

    // __ FUNCTIONS __


    private void loadImages() {
        try {
            String imageFilePath = "Square Images";

            imgFalse = ImageIO.read(new File("Square Images\\X.png"));
            imgTrue = ImageIO.read(new File( "Square Images\\Circle.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        images = new Image[] { imgFalse, imgTrue };
    }

    /**
     * Initializes all Square objects in Block matrix[][] to their CorrectStates and index positions
     * @param TrueRows
     */
    private void initSquares(int[] TrueRows) {
        loadImages();
        /* Index is the column, value is the row, containing TRUE square */
        /* Iterate through all Squares in Block */
        for(int row = 0; row < matrix.length; row++) {

            for(int col = 0; col < matrix[row].length; col++) {
                /* Initialize TRUE squares */
                if(row == (TrueRows[col])) {
                    matrix[row][col] = new Square(row, col, Square.State.TRUE);
                } else {
                    /* Initialize FALSE squares */
                    matrix[row][col] = new Square(row, col, Square.State.FALSE);
                }
                matrix[row][col].addActionListener(this);
                matrix[row][col].setImages(images);
            }
        }
    }

    private void changeSquareToOrFromTrue(Square clickedSquare, Square.State neighborState, Square.State clickedState) {
        int row = clickedSquare.getRowIndex();
        int col = clickedSquare.getColIndex();
        /* set each square in clickedSquares row to FALSE */
        for(int a = 0; a < matrix.length; a++) {
            matrix[ row ][a].setCurrentState(neighborState);
        }

        /* set each square in clickedSquares column to FALSE */
        for(int b = 0; b < matrix[col].length; b++) {
            matrix[b][ col ].setCurrentState(neighborState);
        }

        clickedSquare.setCurrentState(clickedState);

        /* set clickedSquare to TRUE */
        if(clickedState == Square.State.TRUE) {
            clickedSquare.setCurrentState(Square.State.TRUE);
        } else {
            clickedSquare.setCurrentState(Square.State.EMPTY);
        }
    }

    // __ ACCESSORS __
    public Square[][] getSquares() { return this.matrix;}
    public String getBlockRowTitle() { return this.blockRowTitle;}
    public String getBlockColTitle() { return this.blockColTitle;}
    public String[] getColumnTitles() { return this.columnTitles;}
    public String[] getRowTitles() { return this.rowTitles;}

    // __ OVERRIDES __

    /** This method handles interactions between the user and any square within the
     * matrix. This is done by calling specific methods based on the state of the square
     * clicked by the user.
     * Only squares within the matrix have ActionEvents linked to them.
     *
     * @param e - 'Square' clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Square clicked = (Square)e.getSource();
        switch(clicked.getCurrentState()) {
            case EMPTY:
                clicked.setCurrentState(Square.State.FALSE);
                break;
            case FALSE:/* change neighbor Squares to FALSE, current Square to TRUE */
                changeSquareToOrFromTrue(clicked, Square.State.FALSE, Square.State.TRUE);
                break;
            case TRUE: /* change neighbor Squares to EMPTY, current Square to Empty */
                changeSquareToOrFromTrue(clicked, Square.State.EMPTY, Square.State.EMPTY);
                break;
        }
    }

    public Component getSquare(int a, int b) {
        return matrix[a][b];
    }
}
