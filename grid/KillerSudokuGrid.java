/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Scanner;
import grid.Cage;

/**
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid
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
	
	//default number of cages in a game.
	int numOfCells = 0;
	//array holding all the data about each cage in the game.
	private Cage[] cages;
	
 	private int[][] boxNumbers;

    public KillerSudokuGrid() {
    	 super();
         //set up an initial 0 sized array of allowed symbols
         symbols = new Integer[gridSize];
         //set up the game board which is initially 0*0
         game = new Integer[gridSize][gridSize];

         
    } // end of KillerSudokuGrid()


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
    	final int NUM_OF_CAGES_LINE_POSITION = 2;
    	final int CAGE_LINE_POSITION = 3;
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
    		}else if(linePositionInFile == NUM_OF_CAGES_LINE_POSITION) {
    			//get the number of cages in game.
    			int numOfCages = Integer.parseInt(parseLine);
    			cages = new Cage[numOfCages];
    			
    		}else if(linePositionInFile >= CAGE_LINE_POSITION) {
    			/*
    			 * example = 7 8,8 4,2 5,2 5,8
    			 * below parses and splits the string up to usable values to 
    			 * then insert into the cage array, as a cage object.
    			 * 
    			 * Complexity: O(n*m)
    			 */
    			
    			splitString = parseLine.split(SYMBOL_DELIMITER);
    			//get the cage sum from the first int in the file.
    			int cageSum = Integer.parseInt(splitString[0]);
    			//loop and get the cells that make up the cage
    			int numOfCellsInCage = splitString.length-1;
				Cage cage = new Cage(cageSum, numOfCellsInCage);
				Integer[][] cells = new Integer[numOfCellsInCage][2];
    			for(int i = 1; i < splitString.length; ++i) {
    				String[] cell = splitString[i].split(",");
    				
    				//gets coordinates of a cell to be added to the list of cells in a cage from the line
    				int cellXCoordinate = Integer.parseInt(cell[0]);
    				int cellYCoordinate = Integer.parseInt(cell[1]);
    				cells[i-1][0] = cellXCoordinate;
    				cells[i-1][1] = cellYCoordinate;
    			}
				cage.addCellsToCage(cells);
				for(int i = 0; i < cages.length; ++i) {
					if(cages[i] == null) {
						cages[i] = cage;
						//cage has been inserted, stop inserting, go to next line in file.	
						break;
					}
				}
    		}
    		++linePositionInFile;
    	}
    	
    	scanner.close();
    } // end of initBoard()


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
					if(getCell(x, y) == getCell(x, i) && getCell(x, i) == null && i != y && getCell(x, y) != null) {
						return false;
					}
				}
				//check Col
				for(int i = 0; i < getSize(); ++i) {
					if(getCell(x, y) == getCell(i, y) && getCell(i, y) == null && i != x && getCell(x, y) != null) {
						return false;
					}
				}
			}
		}
        return isValid;
    } // end of validate()


	@Override
	public Integer[] getSymbols() {
		//returns the symbols in the game.
		return symbols;
	}
	
	@Override
	public Cage[] getCages() {
		//returns the cages in the game.
		return cages;
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
		return gridSize;
	}


	@Override
	public boolean verifyCell(int x, int y, int boxIDx, int boxIDy, Integer cellToPlace) {

		//verify cell placement is not already in the box.
		for(int l = 0; l < symbols.length; ++l) {
			for(int i = boxIDx; i < boxIDx; ++i) {
				for(int j = boxIDy; j < boxIDy; ++j) {
					if(getCell(i, j) == cellToPlace && x != boxIDx && y != boxIDy) {
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
	
	
	public boolean validateCage(Cage cage) {
		boolean isValid = false;
		
		Integer[][] cageCoordinates = cage.getCells();
		int cageX = 0;
		int cageY = 0;

		
		int cageSum = 0;

		
		//	Once cells have been checked. We need to see if the cells in the Cage are valid.
		for(int i = 0; i < cageCoordinates.length; ++i) {

			cageX = cageCoordinates[i][0];
			cageY = cageCoordinates[i][1];

			if(getCell(cageX, cageY) != null) {
				// Return true for now as the other cells in the grid are yet to be set.
				cageSum += getCell(cageX, cageY);
			}
		}
		
		if(cageSum == cage.getCageSum()) {
			isValid = true;
		}else {
			//	The sum of the cells in cage do not match up to the required amount
			return false;
		}
		
		//	Check if there are duplicate cells in the cage
		int cageCheckX = 0;
		int cageCheckY = 0;
		for(int i = 0; i < cageCoordinates.length; ++i) {
			cageCheckX = cageCoordinates[i][0];
			cageCheckY = cageCoordinates[i][1];
			//	can improve efficiency, fix later.
			for(int j = 0; j < cageCoordinates.length; ++j) {
				cageX = cageCoordinates[j][0];
				cageY = cageCoordinates[j][1];
				if(i!=j && getCell(cageCheckX, cageCheckY) == getCell(cageX, cageY)) {

					//	Found a duplicate, return false.
					isValid = false;
				}
			}
		}
		
		return isValid;
	}




} // end of class KillerSudokuGrid
