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
    				builder.append(", ");
    			}else {
    				builder.append("\n");
    			}
    		}
    	}
        return String.valueOf(builder);
    } // end of toString()


    @Override
    public boolean validate() {
        // TODO
    	
        // placeholder
        return false;
    } // end of validate()

    @Override
    public Integer[] getSymbols() {
    	return symbols;
    }


	@Override
	public Cage[] getCages() {
		//Doesn't have any cages.
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
		return gridSize;
	}


	@Override
	public boolean verifyCell(int x, int y, int boxIDx, int boxIDy, Integer cellToPlace) {

		int boxSize = (int) Math.sqrt(gridSize);
		
		for(int l = 0; l < symbols.length; ++l) {
			System.out.println(symbols[l]);
		}
		for(int l = 0; l < symbols.length; ++l) {
			System.arraycopy(symbols, 0, symbols, 0, symbols.length);
			int foundCount = 0;
			for(int i = boxIDx; i < boxSize; ++i) {
				for(int j = boxIDy; j < boxSize; ++j) {

					if(getCell(i, j) == cellToPlace) {
	    				System.out.println("\t\t\t BOX IS INVALID: " + getCell(i, j) + " @ " + x + "," + y);

						return false;
//						if(getCell(i, j) != null && symbols[l].equals(cellToPlace)) {
//							System.out.println("found: " + symbols[l] + " at: " + j + "," + y);
//
//		    				++foundCount;
//		    			}
//						System.out.println("comparing: " + symbols[l] + " with " + cellToPlace + " count: " + foundCount );
//
//		    			if(foundCount>1) {
//		    				System.out.println("\t\t\t BOX IS INVALID: " + getCell(i, j) + " @ " + x + "," + y);
//		    				return false;
//		    			}
					}
				}
			}
		}
		
		//check box
//		System.out.println("CHECKING BOX");
//		for(int index = 0; index < symbols.length; ++index) {
//			int foundCount = 0;
//			for(int i = boxIDx; i < boxSize; ++i) {
//	    		for(int j = boxIDy; j < boxSize; ++j) {
//	    			if(getCell(i, j) != null && symbols[index] != null && symbols[index].equals(cellToPlace)) {
//	    				++foundCount;
//	    			}
//	    			if(foundCount>1) {
//	    				System.out.println("\t\t\t BOX IS INVALID: " + getCell(i, j) + " @ " + x + "," + y);
//	    				return false;
//	    			}
//	    		}
//	    	}
//		}
//		System.out.println("CURRENT CELL: " + getCell(x,y));

		System.out.println("CHECKING COL");
		//check row
		for(int i = 0; i < getSize(); ++i) {
//			System.out.println("\t\t\t\t" + getCell(i, y));
			System.out.println("checking cell: " + y + ","+ i + " --> " + getCell(i, y));
			if(cellToPlace == getCell(i, y) && i != x) {
				System.out.println("FOUND DUPE: " + getCell(x,y) + "at " + y + "," + i);
				return false;
			}
		}
		System.out.println("CHECKING ROW");

		for(int i = 0; i < getSize(); ++i) {
//			System.out.println("\t\t\t\t" + getCell(x, i));
			System.out.println("checking cell: " + i + ","+ x + " --> " + getCell(x,i));
			if(cellToPlace == getCell(x, i) && i != y) {
				System.out.println("FOUND DUPE: " + getCell(x,i) + "at " + i + "," + x);
				return false;
			}
		}
//		for(int i = 0; i < getSize(); ++i) {
//			System.out.println("\t\t\t\t" + getCell(i, y));
//			System.out.println("checking cell: " + i + ","+ y);
//
//			if(getCell(y, i) != null && getCell(y, i) == getCell(x, y) && i != x) {
//				System.out.println("\t\t\t COL IS INVALID: " + getCell(y, i));
//
//				return false;
//			}
//			
//		}
//		System.out.println("CHECKING ROW");
//
//		for(int i = 0; i < getSize(); ++i) {
//			System.out.println("\t\t\t\t" + getCell(x, i));
//			System.out.println("checking cell: " + x + ","+ i);
//
//			if(getCell(x, i) != null && getCell(x, i) == getCell(x, y) && i != y) {
//				System.out.println("\t\t\t ROW IS INVALID: " + getCell(x, i));
//
//				return false;
//			}
//			
//		}
		return true;
	}
	
	
} // end of class StdSudokuGrid
