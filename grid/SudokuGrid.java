/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();

    /**
     * gets the valid symbols we are allowed to use in the game.
     * @return Integer[] array of valid symbols to use in game
     */
    public abstract Integer[] getSymbols();
    
    /**
     * gets the cages that the game has.
     * @return Cage[] array of cages that the game uses.
     */
	public abstract Cage[] getCages();
	
	/**
	 * set the specified cell value.
	 * @param x X-Coordinate of the cell
	 * @param y Y-Coordinate of the cell
	 * @param value value to set the cell to
	 */
	public abstract void setCell(Integer x, Integer y, Integer value);
	
	/**
	 * returns the value at the specified cell in the game
	 * @param x X-Coordinate of the cell
	 * @param y Y-Coordinate of the cell
	 */
	public abstract Integer getCell(Integer x, Integer y);
	
	/**
	 * gets the the size of the grid ie if 9*9 grid will return 9
	 * since its a square.
	 * @return size of grid
	 */
	public abstract int getSize();
	/**
	 * checks the rowm cols and current box to verify the placement of the cell.
	 * @param x X-Coordinate of current cell to verify
	 * @param y Y-Coordinate of current cell to verify
	 * @param boxIDx starting X-Coordinate of box
	 * @param boxIDy starting Y-Coordinate of box
	 * @param cellToPlace the cell to be placed if valid
	 * @return
	 */
	public abstract boolean verifyCell(int x, int y, int boxIDx, int boxIDy, Integer cellToPlace);
} // end of abstract class SudokuGrid
