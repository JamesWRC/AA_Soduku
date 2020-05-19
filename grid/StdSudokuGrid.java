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
	private Integer[] symbols;
	/*
	 * The game board that will hold all values.  
	 * Have chosen Integer over int as it is more efficient.
	 *  
	 */
	private Integer[][] game;
    public StdSudokuGrid() {
        super();
        //set up an initial 0 sized array of allowed symbols
        symbols = new Integer[gridSize];
        //set up the game board which is initially 0*0
        game = new Integer[gridSize][gridSize];
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
    			
    			//System.out.println("DEBUG: size" + parseLine);
    			int parsedMazeSize = Integer.parseInt(parseLine);
    			//set the gridSize variable
    			gridSize = parsedMazeSize;
    			
    			//construct the game with the proper sizes.
    			symbols = new Integer[parsedMazeSize];
    			game = new Integer[parsedMazeSize][parsedMazeSize];

    		}else if(linePositionInFile == VALID_SYMBOLS_LINE_POSITION) {
    			//set valid symbols
    			//System.out.println("DEBUG: symbols" + parseLine);
    			splitString = parseLine.split(SYMBOL_DELIMITER);
    			for(int i = 0; i < symbols.length && i < splitString.length; ++i) {
    				symbols[i] = Integer.parseInt(splitString[i]);
    			}
    		}else if(linePositionInFile >= PRESET_VALUE_LINE_POSITION) {
    			//System.out.println("DEBUG: inserting preset" + parseLine);
    			/*
    			 * example = 8,8 7
    			 * below parses and splits the string up to usable values to 
    			 * then insert into the game, as preset value constraints.
    			 * 
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
    	scanner.close();
    }// end of initBoard()
    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	BufferedWriter writeToOutFile = new BufferedWriter(new FileWriter(filename));
    	writeToOutFile.write(toString());
    	writeToOutFile.close();
     
    } // end of outputBoard()


    @Override
    public String toString() {
    	
    	/*	Build the string to print out
    	/*	- String builder is much fast for string concat'ing a string.
    	 * 	
    	 */
    	StringBuilder builder = new StringBuilder();
    	for(int i=0; i<game.length; ++i) {
    		for(int j=0; j<game.length; ++j) {
    			builder.append(game[i][j]);

    			//apply commas after all except last in row, 
    			//the one at the end will add a line break
    			if(j<game.length-1) {
    				builder.append(",");
    			}else {
    				builder.append("\n");
    			}
    		}
    	}
        return String.valueOf(builder);
    } // end of toString()


    @Override
    public boolean validate() {
        int gameSize = (int) Math.sqrt(getSize());
    	int currX = 0;
    	int currY = 0;
    	int xLimit = gameSize;
    	int yLimit = gameSize;
    	boolean isValid = true;
    	boolean boxIsValid = false;
    	//ensure each box in the grid is valid 
    	while(!boxIsValid) {
			for(int index = 0; index < symbols.length; ++index) {
				int cellCount = 0;
				for(int y = currY; y < yLimit; ++y) {
	    			for(int x = currX; x < xLimit; ++x) {	
						if(symbols[index] == getCell(y,x)) {
							cellCount++;
						}						
						//stopping condition. ie reached end of entire grid.
						if(y == gridSize-1 && x == gridSize-1) {
							boxIsValid = true;
						}
					}
    			}
				if(cellCount>1) {
					System.out.println("Found a duplicate of: " + symbols[index] + "");
					return false;
				}
			}
			//set co-orodinates to move to next box in grid, left to right, top to bottom
			if((currX + gameSize)<gridSize) {
				xLimit+= gameSize;
				currX+=gameSize;
			}else if((currY + gameSize)<gridSize) {
				currX = 0;
				xLimit = gameSize;
				yLimit+=gameSize;
				currY+=gameSize;
			}
		}
    	
		
		//verify rows and columns are unique
		for(int y = 0; y < gridSize; ++y) {
			for(int x = 0; x < gridSize; ++x) {
				//check Row
				for(int i = 0; i < getSize(); ++i) {
					if(getCell(x, y) == getCell(x, i) && i != y) {
						System.out.println("Found a duplicate of: " + getCell(x, y) + " at position: " + x + "," + y);
						return false;
					}
				}
				//check Col
				for(int i = 0; i < getSize(); ++i) {
					if(getCell(x, y) == getCell(i, y) && i != x) {
						System.out.println("Found a duplicate of: " + getCell(x, y) + " at position: " + x + "," + y);
						return false;
					}
				}
			}
		}
        return isValid;
    } // end of validate()

    @Override
    public Integer[] getSymbols() {
    	//returns an array of valid symbols.
    	return symbols;
    }


	@Override
	public Cage[] getCages() {
		//Doesn't have any cages as this is not the Killer sudoku.
		return null;
	}


	@Override
	public void setCell(Integer x, Integer y, Integer value) {
		//set the cell to the value.
		game[x][y] = value;
	}


	@Override
	public Integer getCell(Integer x, Integer y) {
		//return cell value at specified location.
		return game[x][y];
	}


	@Override
	public int getSize() {
		//returns the grid size.
		return gridSize;
	}


	@Override
	public boolean verifyCell(int x, int y, int boxIDx, int boxIDy, Integer cellToPlace) {

		int boxSize = (int) Math.sqrt(gridSize);
		//verify cell placement is not already in the box.
		for(int l = 0; l < symbols.length; ++l) {
			int foundCount = 0;
			for(int i = boxIDx; i < boxSize; ++i) {
				for(int j = boxIDy; j < boxSize; ++j) {
					if(getCell(i, j) == cellToPlace) {
						return false;
					}
				}
			}
		}
		//check row for duplicate symbol
		for(int i = 0; i < getSize(); ++i) {
			if(cellToPlace == getCell(i, y) && i != x) {
				return false;
			}
		}
		//check column for duplicate symbol
		for(int i = 0; i < getSize(); ++i) {
			if(cellToPlace == getCell(x, i) && i != y) {
				return false;
			}
		}
		return true;
	}
	
	
} // end of class StdSudokuGrid
