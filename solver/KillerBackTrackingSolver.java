/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.Cage;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
    // TODO: Add attributes as needed.
	private int gameSize = 0;
	int[][] boxNumbers;
    public KillerBackTrackingSolver() {
        // TODO: any initialisation you want to implement.
    } // end of KillerBackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        // TODO: your implementation of a backtracking solver for Killer Sudoku.
    	Cage[] cages = new Cage[grid.getCages().length];
		System.arraycopy(grid.getCages(), 0, cages, 0, cages.length);		
		
    	//	boxID is the index of the bottom left cell of the boxes in the game
    	gameSize = (int) Math.sqrt(grid.getSize());

    	//boxNumbers is used to get the box number based off coordinates in the grid.
    	boxNumbers = new int[gameSize][gameSize];
    	
    	/*	build boxNumber array
    	 * 	- This is used to get the box number based off coordinated in the grid.
    	 * 	- Is used to get an offset in the grid array.
    	 * 	- Box offsets start from 0, starting left to right, top to bottom.
    	 */
    	int boxCounter = -1;
    	for(int col = 0; col < gameSize; ++col) {
    		for(int row = 0; row < gameSize; ++row) {
    			boxNumbers[col][row] = ++boxCounter;
    			System.out.println(boxNumbers[col][row]);
    		}
    	}
    	
    	
    	int boxIDx = 0;
    	int boxIDy = 0;

    	int boxLimitx = 0;
    	int boxLimity = 0;

    	int count = 0;
    	/*	gameSize is helping the get the bottom left cell of the cells in each box
    		which is the square root of the game length, as the whole grid is made up
    		of smaller squares (box).
    	*/	
    	Integer[] symbols = grid.getSymbols();
		Integer[] tempSymbols = new Integer[grid.getSymbols().length];

    	System.arraycopy(grid.getSymbols(), 0, tempSymbols, 0, tempSymbols.length);
		for(int i = 0; i < grid.getSymbols().length; i++) {
			System.out.println(" ----> " + tempSymbols[i]);
		}

    	
    	//Integer[] symbols = null;
    	int cellsInBoxLeft = -1;
    	for(int i = 0; i < cages.length; i++) {
    		Cage a = cages[i];
			System.out.println(" Value: " + a.getCageSum() + " cells: ");
			Integer[][] b = a.getCells();
    		for(int j = 0; j < b.length; j++) {
    			System.out.println(" -> " + b[j][0] + "," + b[j][1]);
    		}
    	}
    	recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, tempSymbols, cellsInBoxLeft, cages, count);
    	

    	
//    	System.exit(0);
        return grid.validate();
       
    } // end of solve()

    
   
    
    public boolean recursiveBacktrack(SudokuGrid grid, int boxIDx, int boxIDy, 
    		int boxLimitx, int boxLimity, Integer[] symbols, int cellsInBoxLeft, Cage[] cages, int count) {
    	System.out.println("COUNT: " + count);
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
        	

//    	if(cellsInBoxLeft == 0 || cellsInBoxLeft == -1) {
//    		System.out.println("CELLS IN BOX IS RESET TO 0");
//        	if(cellsInBoxLeft == -1) {
//        		//set up entry point of grid for x
//    			boxIDx = 0;
//    			boxLimitx = gameSize;
//    			
//            	//set up entry point of grid for y
//        		boxIDy = 0;
//    			boxLimity = gameSize;
//    			
//            	//set up the number of cells to left to fill in each box (sub box of the grid)
//        		cellsInBoxLeft = 0;
//        	}else {
//        		//re sizes the boxes throughout the grid.
//	        	if((boxIDx + gameSize)<grid.getSize()) {
//	    			boxLimitx+= gameSize;
//	    			boxIDx+=gameSize;
//	    		}else if((boxIDy + gameSize)<grid.getSize()) {
//	    			boxIDx = 0;
//	    			boxLimitx = gameSize;
//	    			boxLimity+=gameSize;
//	    			boxIDy+=gameSize;	
//	    		}
//	        	
//        	}
//        	cellsInBoxLeft = gameSize*gameSize;
//        	
//    		symbols = grid.getSymbols();
//    		Integer[] tempSymbols = new Integer[symbols.length];
//			System.arraycopy(grid.getSymbols(), 0, tempSymbols, 0, symbols.length);
//			
//			symbols = tempSymbols;
//
//	    	for(int y = boxIDx; y < boxLimitx; ++y) {
//	    		for(int x = boxIDy; x<boxLimity; ++x) {
//	    			//if not null add to blacklist
//	    			if(grid.getCell(x, y) != null){
//		    	    	for(int l = 0; l < symbols.length; ++l) {
//		    	    		//null element in valid dymbols
//		    	    		if(grid.getCell(x, y).equals(symbols[l])) {		    	    			
//		    	    			symbols[l] = null;
//		    	    			System.out.println("/");
//		    	    			--cellsInBoxLeft;
//		    	    			break;
//		    	    		}
//		    	    	}
//	    			}
//	    		}
//	    	}
//    	}
    	System.out.println("symbols recived: ");
		for(int i = 0; i < symbols.length; i++) {
			System.out.println(" ----> " + symbols[i]);
		}
    	
   	 System.out.println("==================================== RECEIVED X: " + boxIDx + " RECEIVED Y: " + boxIDy 
   			 + " cells remaining: " + cellsInBoxLeft + " count: " + count);

    	Integer lastCell = null;
    	int failCounter = 0;
    	boolean isValid = true;
//    	boxIDx = 
//    	boxIDy = 
    			System.out.println("games size: " + gameSize);	
	    		int x=count % grid.getSize();
	    		int y= (int) Math.floor(count / grid.getSize());
	    		if(y == grid.getSize()) {
	    			y = 0;
	    		}
	    		boxIDx = x;
	    		boxIDy = y;
	    		//keep its own copy of where it is at to allow for multi-threading uses.
	    		Integer[] tempSymbols;

	    		if(count % grid.getSize() == 0) {
	    			System.out.println("here 0");

//	    			tempSymbols = new Integer[grid.getSymbols().length];
//	    			System.arraycopy(symbols, 0, tempSymbols, 0, symbols.length);
//	    			System.arraycopy(grid.getSymbols(), 0, symbols, 0, grid.getSymbols().length);
	    			symbols = grid.getSymbols();
	        		tempSymbols = new Integer[symbols.length];
	    			System.arraycopy(grid.getSymbols(), 0, tempSymbols, 0, symbols.length);
	    			
	    			symbols = tempSymbols;


	    		}else {
//	    			tempSymbols = new Integer[symbols.length];
//	    			System.out.println("here 1");
//	    			System.arraycopy(symbols, 0, tempSymbols, 0, symbols.length);
	    			tempSymbols = new Integer[symbols.length];
	    			System.arraycopy(symbols, 0, tempSymbols, 0, symbols.length);
	    		}
//	    		symbols = tempSymbols;
	    		System.out.println("\t\t\t\t\t\t\t\t\t\t CURRENTLY AT: x: " +x+ " y: " + y );

    			int boxNumber = boxNumbers[(int) Math.floor(y/gameSize)][(int) Math.floor(x/gameSize)];
	    		System.out.println("\t\t\t\t\t\t\t\t\t\t CURRENTLY AT: x: " +x+ " y: " + y + " in box: " + boxNumber);

    			if(grid.getCell(y, x) == null) {
    				System.out.println("IS null at x: " +x+ " y: " + y  );
	    			for(int l = 0; l < symbols.length; l++) {
    	    			System.out.println("\t\t\t "+ l+ " choosing cell: -> " +  symbols[l] +  " / " + symbols.length + " POSSIBLE CELLS");
    	    			
    	    			if(symbols[l] != null) {
    	    					    	    		
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		if(symbols[i] != null) {
	    	    	    			lastCell = symbols[i];
	    	    	    		}
	    	    	    	}
	    	    			Integer tempCell = symbols[l];
	    	    			System.out.println("chosen cell: " + tempCell);
	    	    			
	    		    		//	Begin killer cage solutions
	    	    			
	    	    			//	Pick a cage to satisfy
	    		    		Cage currentCage = null;
	    		    		int currentCellIndex = 0;
	    		    		
	    		    		for(int i = 0; i < cages.length; ++i) {
	    		    			System.out.println(".");
	    		    			if(cages[i] != null) {
	    		    				Integer[][] coordinates = cages[i].getCells();
	    		    				
 	    		    				for(int j = 0; j < coordinates.length; ++j) {
 	    		    					int cageX = coordinates[j][0];
 	    		    					int cageY = coordinates[j][1];
 	    		    					if(cageX == x && cageY == y) {
	 	   	    		    				System.out.println("ittee through cages: " + cages[i].getCageSum());
	 		    		    				currentCage = cages[i];
	 		    		    				currentCellIndex = i;
	 		    		    				
	 		    		    				break;
 	    		    					}
	    		    				}
//	    		    				System.out.println("ittee through cages: " + cages[i].getCageSum());
//	    		    				currentCage = cages[i];
//	    		    				currentCellIndex = i;
//	    		    				break;
//	    		    				

	    		    			}
//		    		    		Integer[][] cageCoordinates = currentCage.getCells();
//		    		    		int cageX = 0;
//		    		    		int cageY = 0;
//		    		    		for(int j = 0; j < cageCoordinates.length; ++j) {
//		    		    			cageX = cageCoordinates[j][0];
//		    		    			cageY = cageCoordinates[j][1];
//		    		    			if(grid.getCell(cageX, cageY) == null) {
//		    		    				//	Have an empty position in grid and cage to satisfy.
//		    		    				
//		    		    			}
//		    		    			System.out.println(cageCoordinates[i][0]+ "," + cageCoordinates[i][1]);
//		    		    			
//		    		    		}
	    		    		}
	    		    		System.out.println(" CHOSEN CAGE: " + currentCage.getCageSum());
//	    		    		currentCage.print();
//	    		    		Integer[][] cageCoordinates = currentCage.getCells();
//	    		    		int cageX = 0;
//	    		    		int cageY = 0;
//	    		    		for(int i = 0; i < cageCoordinates.length; ++i) {
//	    		    			cageX = cageCoordinates[i][0];
//	    		    			cageY = cageCoordinates[i][1];
//	    		    			if(grid.getCell(cageX, cageY) == null) {
//	    		    				//	Have an empty position in grid and cage to satisfy.
//	    		    				
//	    		    			}
//	    		    			System.out.println(cageCoordinates[i][0]+ "," + cageCoordinates[i][1]);
//	    		    			
//	    		    		}
//	    		    		System.exit(0);
	    		    				
	    		    		//	End killer cage solution
	    		    				
	    	    			
    	    				System.out.println("\t\t\t\t\t trying cell: " + symbols[l] +  " @ " + y + "," + x);

	    	    			if(grid.verifyCell(y, x, boxIDx, boxIDy, symbols[l])) {
	    	    				System.out.println("\t\t\t\t\t setting cell: " + symbols[l] +  " @ " + y + "," + x);
	    	    				System.out.println("minus one cell");
	    	    				--cellsInBoxLeft;
	    	    				
	    	    				//	Set cell in grid
	    	    				grid.setCell(y, x, symbols[l]);
	    	    				System.out.println("grida");
	    	    				System.out.println("\n\n\n");
	    	    				System.out.println(grid.toString());
	    	    				System.out.println("\n\n\n");

	    	    				tempSymbols[l] = null;
	    	    				symbols[l] = null;
	    	    				//	Check if the cell placement is valid within the cage
	    	    				if(isCageFull(currentCage, grid)) {
	    	    					System.out.println("CAGE IS FULL!");
	    	    					if(!grid.validateCage(currentCage)){
		    	    					System.out.println("yes bad cell");

		    	    					//	if this is false then the current cell in the grid is not valid, return/
	    	    						grid.setCell(y, x, null);
		    	    					
		    	    					tempSymbols[l] = tempCell;
		    	    					symbols[l] = tempCell;
		    	    					if(l == symbols.length-1) {
		    	    						++cellsInBoxLeft;
			    	    					System.out.println("FAILED - AB ");

		    	    						return false;
		    	    					} 
	    	    						++cellsInBoxLeft;
	    	    						boolean restIsNull = true;
	    	    						System.out.println("checking rest of the symbols for null value");
	    	    						for(int i = l; i < symbols.length; i++) {
	    	    							if(symbols[i] != null && i!=l) {
	    	    								System.out.println("------------> " + symbols[i]);
	    	    								restIsNull = false;
	    	    							}
	    	    						}
	    	    						System.out.println("REST IS NULL: " + restIsNull);
	    	    						if(restIsNull) {
			    	    					System.out.println("FAILED - AC ");

	    	    							return false;
	    	    						}else {
	    	    							System.out.println("continuing with rest of possible symbols...");
	    	    							continue;
	    	    						}
	    	    						
		    	    				}else {
//		    	    					cages[currentCellIndex] = null;
		    	    				}
	    	    				}
	    	    				
	    	    				System.out.println("going one level deeper");

	    	    				isValid = recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, tempSymbols, 
	    	    						cellsInBoxLeft, cages, count+1);
	    	    				System.out.println("going back 1");
	    	    			
	    	    				++cellsInBoxLeft;

	    	    				if(!isValid){
	    	    					grid.setCell(y, x, null);
	    	    					
	    	    					tempSymbols[l] = tempCell;
	    	    					symbols[l] = tempCell;	  
	    	    					
	    	    					//	Set cage back
	    	    					cages[currentCellIndex] = currentCage;
	    	    					if(l == symbols.length-1) {
		    	    					grid.setCell(y, x, null);
		    	    					System.out.println("FAILED - A ");

		    	    					return false;
	    	    					}
	    	    				}else {
	    	    					System.out.println(" PASSED - A");
    	    						return true;
    	    					}
	    	    			}else {
	    	    				if(lastCell == tempCell) {

        	    					grid.setCell(y, x, null);
        	    					System.out.println("FAILED - B ");

        	    					return false;

        	    				}
	    	    				grid.setCell(y, x, null);
	    	    			}
    	    				if(!isValid && lastCell == tempCell) {

    	    					grid.setCell(y, x, null);
    	    					System.out.println("FAILED - C ");

    	    					return false;

    	    				}
	    	    			boolean allNull = true;
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		if(symbols[i] != null) {
	    	    	    			failCounter++;
	    	    					System.out.println(" FAILED - D");


	    	    	    			allNull = false;
	    	    	    		}
	    	    	    	}
	    	    	    	if(allNull || failCounter == 1) {
    	    					grid.setCell(y, x, null);
    	    					System.out.println("FAILED - E ");
    	    					return false;
    	    					
	    	    	    	}
	    				}
	    			}
    			}
				System.out.println(" PASSED - B");

		return isValid;
    }

    private boolean isCageFull(Cage cage, SudokuGrid grid) {
    	boolean isFull = true;
		Integer[][] cageCoordinates = cage.getCells();
		int cageX = 0;
		int cageY = 0;
		//	Check if all cells in cage has been filled in yet.
		for(int i = 0; i < cageCoordinates.length; ++i) {
			cageX = cageCoordinates[i][0];
			cageY = cageCoordinates[i][1];
			if(grid.getCell(cageX, cageY) == null) {
				// Return true for now as the other cells in the grid are yet to be set.
				isFull = false;
				System.out.println(cageCoordinates[i][0]+ "," + cageCoordinates[i][1]);	
				System.out.println(" Cage is not full yet.... cont..");
			}			
		}
		return isFull;
    }
    
    
} // end of class KillerBackTrackingSolver()
