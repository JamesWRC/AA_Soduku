/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{
    // TODO: Add attributes as needed.
	private int gameSize = 0;
    public BackTrackingSolver() {
        // TODO: any initialisation you want to implement.
    } // end of BackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	//	boxID is the index of the bottom left cell of the boxes in the game
    	gameSize = (int) Math.sqrt(grid.getSize());

    	int boxIDx = 0;
    	int boxIDy = 0;

    	int boxLimitx = 0;
    	int boxLimity = 0;

    	/*	gameSize is helping the get the bottom left cell of the cells in each box
    		which is the square root of the game length, as the whole grid is made up
    		of smaller squares (box).
    	*/	
    	Integer[] symbols = grid.getSymbols();
    	//Integer[] symbols = null;
    	int cellsInBoxLeft = -1;
    	
        return recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, symbols, cellsInBoxLeft);
    } // end of solve()

    
    public boolean recursiveBacktrack(SudokuGrid grid, int boxIDx, int boxIDy, 
    		int boxLimitx, int boxLimity, Integer[] symbols, int cellsInBoxLeft) {
    	//sudo code
    	/*
    	 * first scan 3*3 grid to see what numbers should be blacklisted.
    	 * 
    	 * 	once scanned stop, never scan again
    	 * 	
    	 */
    	
    	// 	if symbols is all null then increase boxIDs??
    	//	copy available symbols
    	//	generate list of available symbols, not already used in the current box
    	//	this will only run the number of times there are boxes in a grid.
    	//	if grid 9*9 then this will run 9 times.
    	//	-1 is the very starting point for the maze, will never be the same value again.
    	 
        	

    	if(cellsInBoxLeft == 0 || cellsInBoxLeft == -1) {
        	if(cellsInBoxLeft == -1) {
        		//set up entry point of grid for x
    			boxIDx = 0;
    			boxLimitx = gameSize;
    			
            	//set up entry point of grid for y
        		boxIDy = 0;
    			boxLimity = gameSize;
    			
            	//set up the number of cells to left to fill in each box (sub box of the grid)
        		cellsInBoxLeft = 0;
        	}else {
        		System.out.println(grid.toString());
        		//re sizes the boxes throughout the grid.
	        	if((boxIDx + gameSize)<grid.getSize()) {
	    			boxLimitx+= gameSize;
	    			boxIDx+=gameSize;
	    		}else if((boxIDy + gameSize)<grid.getSize()) {
	    			boxIDx = 0;
	    			boxLimitx = gameSize;
	    			boxLimity+=gameSize;
	    			boxIDy+=gameSize;	
	    		}
        	}
        	cellsInBoxLeft = gameSize*gameSize;
        	
    		symbols = grid.getSymbols();
    		Integer[] tempSymbols = new Integer[symbols.length];
			System.arraycopy(grid.getSymbols(), 0, tempSymbols, 0, symbols.length);
			
			symbols = tempSymbols;

	    	for(int y = boxIDx; y < boxLimitx; ++y) {
	    		for(int x = boxIDy; x<boxLimity; ++x) {
	    			//if not null add to blacklist
	    			if(grid.getCell(x, y) != null){
		    	    	for(int l = 0; l < symbols.length; ++l) {
		    	    		//null element in valid dymbols
		    	    		if(grid.getCell(x, y).equals(symbols[l])) {		    	    			
		    	    			symbols[l] = null;
		    	    			--cellsInBoxLeft;
		    	    			break;
		    	    		}
		    	    	}
	    			}
	    		}
	    	}
    	}
    	Integer lastCell = null;
    	int failCounter = 0;
    	boolean isValid = true;
	    for(int y = boxIDy; y < boxLimity ; ++y) {
	    	for(int x = boxIDx; x < boxLimitx; ++x) {
	    		//keep its own copy of where it is at to allow for multi-threading uses.
//	    		Integer[] tempSymbols = new Integer[symbols.length];
//    			System.arraycopy(symbols, 0, tempSymbols, 0, symbols.length);
    			if(grid.getCell(y, x) == null) {
	    			for(int l = 0; l < symbols.length; l++) {
    	    			
    	    			
    	    			if(symbols[l] != null) {
    	    					    	    		
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		if(symbols[i] != null) {
	    	    	    			lastCell = symbols[i];

	    	    	    		}
	    	    	    	}
	    	    			Integer tempCell = symbols[l];
	    	    			
	    	    			if(grid.verifyCell(y, x, boxIDx, boxIDy, symbols[l])) {
	    	    				--cellsInBoxLeft;
	    	    				grid.setCell(y, x, symbols[l]);

//	    	    				tempSymbols[l] = null;
	    	    				symbols[l] = null;
	    	    				
//	    	    				isValid = recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, tempSymbols, cellsInBoxLeft);
	    	    				isValid = recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, symbols, cellsInBoxLeft);

	    	    			
	    	    				++cellsInBoxLeft;

	    	    				if(!isValid){
	    	    					grid.setCell(y, x, null);
	    	    					
//	    	    					tempSymbols[l] = tempCell;
	    	    					symbols[l] = tempCell;	    	    					
	    	    					if(l == symbols.length-1) {
		    	    					grid.setCell(y, x, null);
		    	    					return false;
	    	    					}
	    	    				}else {
    	    						return true;
    	    					}
	    	    			}else {
	    	    				if(lastCell == tempCell) {

        	    					grid.setCell(y, x, null);
        	    					return false;

        	    				}
	    	    				grid.setCell(y, x, null);
	    	    			}
    	    				if(!isValid && lastCell == tempCell) {

    	    					grid.setCell(y, x, null);
    	    					return false;

    	    				}
	    	    			boolean allNull = true;
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		if(symbols[i] != null) {
	    	    	    			failCounter++;
	    	    	    			allNull = false;
	    	    	    		}
	    	    	    	}
	    	    	    	if(allNull || failCounter == 1) {
    	    					grid.setCell(y, x, null);
    	    					return false;
    	    					
	    	    	    	}
	    				}
	    			}
    			}
    		}
    	}
		return isValid;
    }

} // end of class BackTrackingSolver()
