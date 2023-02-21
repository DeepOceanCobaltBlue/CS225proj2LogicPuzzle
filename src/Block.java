/* Work Log
 * 2/4 [chris] - created class
 *             - wrote class comment
 * 2/14 [chris] - worked on implementation of UML requirements and documentation
 * 2/18 [chris] - updated initialization constructor for title information and squares
 * 2/19 [chris] - implemented cycling images for buttons
 *              - updated documentation to reflect changes
 * 2/19 [phoenix] - moved code from findIncorrectBlocks() from LogicGame to here
 * 2/20 [Andrew] - Updated class with comments for each method provided
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * This class represents a 4x4 matrix of 'Squares' and handles interactions
 * between the user and any square within the matrix.
 */
public class Block implements ActionListener {
    // __ ATTRIBUTES __
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

    /* images displayed on individual Squares to display currentState */
    /* [0] = FALSE, [1] = TRUE */
    private static Image[] images;

    // __ CONSTRUCTORS __
    public Block() { // default constructor
        matrix = new Square[4][4];
        blockRowTitle = null;
        blockColTitle  = null;
        rowTitles = new String[4];
        columnTitles = new String[4];
    }

    /**
     * Constructor requirements
     * @param rowTitle
     * @param rowTitles
     * @param columnTitle
     * @param columnTitles
     * @param order
     */
    public Block(String rowTitle, String[] rowTitles, String columnTitle, String[] columnTitles, String order) { // constructor with arguments
        this();
        this.blockRowTitle = rowTitle;
        this.blockColTitle = columnTitle;
        this.rowTitles = rowTitles;
        this.columnTitles = columnTitles;

        /* translate text array into int array */
        String[] tempTrueRows = order.split(",");
        int[] TrueRows = new int[4];
        for(int i = 0; i < 4; i++) {
            // (-1) translates to indexing value. Row 4 is index value [3]
            TrueRows[i] = (Integer.parseInt(tempTrueRows[i]) - 1);
        }
        initSquares(TrueRows);
    }

    // __ FUNCTIONS __
    /**
     * reads Image files from directory to be used by Squares to indicate their
     * currentState as visual feedback to the player.
     */
    private void loadImages() {
        Image imgFalse;
        Image imgTrue;
        try {
            imgFalse = ImageIO.read(new File("Square Images\\X.png"));
            imgTrue = ImageIO.read(new File( "Square Images\\Circle.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        images = new Image[] {imgFalse, imgTrue};
    }

    /**
     * Initializes all Square objects in Block matrix[][] to their CorrectStates and index positions.
     * @param TrueRows - contains which squares in Block matrix are to be set to true. The Index of
     *                 the array is the column index of the TRUE Square and the value stored at that
     *                 index is the row index of the TRUE Square.
     */
    private void initSquares(int[] TrueRows) {
        loadImages();

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

    /**
     * Handles alterations to neighboring squares (full row and full column surrounding 'clickedSquare').
     * @param clickedSquare - which Square is the focus
     * @param neighborState - what state to change neighboring Squares to
     * @param clickedState - what state to set clickedSquare to
     */
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
    }

    /**
     * // TODO: fill this out
     * @param includeEmpty sets t/f depending on if the Square object is empty
     * @return
     */
    public boolean anyErrors(boolean includeEmpty) {
        boolean error = false;

        for (Square[] sqRow : this.matrix) {
            for (Square square : sqRow) {
                // If the state of any Square is incorrect => error will be set to true and stay that way.
                error = error || (!square.isStateCorrect() && (!(square.getCurrentState() == Square.State.EMPTY) || includeEmpty));
                // if(error) { // do this to escape the loop
                //     return error;
                // }
            }
        }
        return error;
    }


    public void setEmptyCorrect(){ // Finds the first empty Square on the GameBoard and sets it to its correct state.
        for (Square[] sqRow : this.matrix){
            for (Square square : sqRow){
                if (!square.isStateCorrect()){
                    square.setCurrentState(Square.State.TRUE);
                }
            }
        }
    }


    // TODO: displayErrors() Finds all incorrect squares and displays their error background(to be implemented).
    public void displayErrors(){ // Finds all incorrect squares and displays their error background (Red)
        for (Square[] sqRow : this.matrix){
            for (Square square : sqRow){
                if(!square.isStateCorrect()){
                    square.setBackground(Color.RED);
                }
            }
        }

    }

    // __ ACCESSORS __
    public Square[][] getSquares() { return this.matrix;} //    returns the given matrix of Square objects
    public String getBlockRowTitle() { return this.blockRowTitle;} //   returns the name of the row the Square is in
    public String getBlockColTitle() { return this.blockColTitle;} //   returns the name of the column the Square is in
    public String[] getRowTitles() { return this.rowTitles;} //   returns the names of the rows that the Squares are in
    public String[] getColumnTitles() { return this.columnTitles;} //   returns the names of the columns that the Squares are in
    public Component getSquare(int a, int b) {
        return matrix[a][b];
    } //    returns the matrix placement of a requested Square

    // __ OVERRIDES __

    /** This method handles interactions between the user and any square within the
     * matrix. This is done by calling specific methods based on the state of the square
     * clicked by the user.
     *
     * @param e - 'Square' clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Square clicked = (Square)e.getSource();

        /* Set Square to next State in cycle and handle necessary changes to neighbor Squares */
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

}
