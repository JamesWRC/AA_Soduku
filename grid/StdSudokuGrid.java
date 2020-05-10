/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{
	//hold the size of grid, ie if grid is 4x4 then gridSize = 4
	private int gridSize = 0;
	//hold the valid symbols allowed to be used in the maze
	private int[] symbols;
	//the gameboard that will hold all values
	private int[][] game;
    public StdSudokuGrid() {
        super();
        //set up an initial 0 sized array of allowed symbols
        symbols = new int[gridSize];
        //set up the game board which is initially 0*0
        game = new int[gridSize][gridSize];
    } // end of StdSudokuGrid()


    /* ********************************************************* */


    @Override
    
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	File fileToParse = new File(filename);
    	Scanner scanner = new Scanner(fileToParse);
    	int linePositionInFile = 0;
    	final int MAZE_SIZE_LINE_POSITION = 0;
    	final int VALID_SYMBOLS_LINE_POSITION = 1;
    	final int PRESET_VALUE_LINE_POSITION = 2;
		final String SYMBOL_DELIMITER = " ";

    	String[] splitString = null;
    	while(scanner.hasNextLine()){
    		//current line to be parsed
			String parseLine = scanner.nextLine();
    		if(linePositionInFile == MAZE_SIZE_LINE_POSITION) {
    			//construct the game sizes.
    			
    			System.out.println("DEBUG: size" + parseLine);
    			int parsedMazeSize = Integer.parseInt(parseLine);
    			//set the gridSize variable
    			gridSize = parsedMazeSize;
    			
    			//construct the game with the proper sizes.
    			symbols = new int[parsedMazeSize];
    			game = new int[parsedMazeSize][parsedMazeSize];

    		}else if(linePositionInFile == VALID_SYMBOLS_LINE_POSITION) {
    			//set valid symbols
    			System.out.println("DEBUG: symbols" + parseLine);
    			splitString = parseLine.split(SYMBOL_DELIMITER);
    			for(int i = 0; i < symbols.length && i < splitString.length; ++i) {
    				symbols[i] = Integer.parseInt(splitString[i]);
    			}
    		}else if(linePositionInFile >= PRESET_VALUE_LINE_POSITION) {
    			System.out.println("DEBUG: inserting preset" + parseLine);
    			/*
    			 * example = 8,8 7
    			 * below parses and splits the string up to usable values to 
    			 * then insert into the game, as preset value constraints.
    			 */
    			splitString = parseLine.split(SYMBOL_DELIMITER);
    			String[] coordinates = splitString[0].split(",");
    			int xCoordinate = Integer.parseInt(coordinates[0]);
    			int yCoordinate = Integer.parseInt(coordinates[1]);
    			int presetValueToInsert = Integer.parseInt(splitString[1]);
    			game[xCoordinate][yCoordinate] = presetValueToInsert;

    		}
    		++linePositionInFile;
    	}
    }// end of initBoard()
    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
        // TODO
    } // end of outputBoard()


    @Override
    public String toString() {
        // TODO

        // placeholder
        return String.valueOf("");
    } // end of toString()


    @Override
    public boolean validate() {
        // TODO

        // placeholder
        return false;
    } // end of validate()

} // end of class StdSudokuGrid
