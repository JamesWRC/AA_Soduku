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

    	int boxIDy = 0-gameSize;
    	int boxIDx = 0;

    	int boxLimitx = 0;
    	int boxLimity = gameSize;

    	/*	gameSize is helping the get the bottom left cell of the cells in each box
    		which is the square root of the game length, as the whole grid is made up
    		of smaller squares (box).
    	*/	
    	Integer[] symbols = grid.getSymbols();
    	//Integer[] symbols = null;
    	int cellsInBoxLeft = 0;
    	recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, symbols, cellsInBoxLeft);
    	if(grid.validate()) {
    		return true;
    	}
        return false;
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
    	// if symbols is all null then increase boxIDs??
    	//copy available symbols
    	//generate list of available symbols, not already used in the current box
    	//this will only run the number of times there are boxes in a grid.
    	//if grid 9*9 then this will run 9 times.
    	boolean isValid = true;
    	if(cellsInBoxLeft == 0) {
        	cellsInBoxLeft = gameSize*gameSize;

    		//generate new IDs within the grid, going left to right, top to bottom.
//    		if(cellsInBoxLeft == 1) {
//    			boxIDy+=gameSize;
//    			boxLimitx+=gameSize;
//    		}
        	if(boxIDy<cellsInBoxLeft) {
    			boxIDy+=gameSize;
    			boxLimitx+=gameSize;
    			System.out.println("resizing x");
    		}else if(boxIDy>=cellsInBoxLeft) {
    			boxIDy = 0;
    			boxIDx+=gameSize;
    			boxLimity+=gameSize;
    			System.out.println("resizing y");

    		}
    		
        	
        	
    		symbols = grid.getSymbols();
    		System.out.println("======== ++++++" + cellsInBoxLeft);
	    	for(int y = boxIDx; y < boxLimitx; ++y) {
	    		for(int x = boxIDy; x<boxLimity; ++x) {
	    			//if not null add to blacklist
	    			if(grid.getCell(x, y) != null){
		    	    	for(int l = 0; l < symbols.length; ++l) {
		    	    		//null element in valid dymbols
		    	    		if(grid.getCell(x, y).equals(symbols[l])) {
		    	    			System.out.println("BZC,MN");
//		    	    			System.out.println("\t\ta: " + boxLimitx + " " + boxLimity);
//		    	    			System.out.println("removing cell; " + symbols[l]);

		    	    			symbols[l] = null;
		    	    			--cellsInBoxLeft;
//		    	    			System.out.println("sebug amt1: " + cellsInBoxLeft);
		    	    			break;
		    	    		}
		    	    	}
	    			}
	    		}
	    	}
    	}
    	Integer triedAndFailed = null;
    	Integer lastCell = null;
    	int failCounter = 0;
    	isValid = true;
//    	System.out.println(" ELL : " + grid.getCell(1, 0));
    	//when printing coords do x,y 
    	// when refrencing the grid do y,x
    	int tempX = 0;
    	int tempY = 0;
	    for(int y = boxIDy; y < boxLimity ; ++y) {
	    	for(int x = boxIDx; x < boxLimitx; ++x) {
    			if(grid.getCell(y, x) == null) {
//	    			System.out.println("trying: " + "from: " + x + "," + y);
//    				symbols = grid.getSymbols();
	    			for(int l = 0; l < symbols.length; l++) {
	    				int numNotNull = 0;
    	    			Integer[] tempSymbols = new Integer[symbols.length];

    	    			System.arraycopy(symbols, 0, tempSymbols, 0, symbols.length);
    	    			
    	    			if(symbols[l] != null) {
	    					//set cell and verify
    	    				
//	    	    			grid.setCell(y, x, symbols[l]);
//	    	    			System.out.println("\t\t\t\t\t\t\t\\t\t" + x + "," + y);
    	    				System.out.println("\n\n\t\t" + symbols[l] + "A-  vailable cells @" + x + "," + y + " cells left: " + cellsInBoxLeft);
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
	    	    	    		if(symbols[i] != null) {
	    	    	    			lastCell = symbols[i];
	    	    	    			System.out.println("LAST CELL IS: " + lastCell);

	    	    	    		}
	    	    	    	}
	    	    			Integer tempCell = symbols[l];
	    	    			
    	    				System.out.println("trying: " + tempCell + " @: " + x + "," + y);
    	    				System.out.println(grid.toString());
    	    				
	    	    			if(grid.verifyCell(y, x, boxIDx, boxIDy, symbols[l])) {
	    	    				System.out.println("SUCCESS: cells left in box: " + cellsInBoxLeft);
	    	    				grid.setCell(y, x, symbols[l]);
	    	    				System.out.println(grid.getCell(y,x) + " is valid @ " + x + "," + y + "pos: " + l);

	    	    				System.out.println(grid.toString());

	    	    				tempX = x;
	    	    				tempY = y;
	    	    				tempSymbols[l] = null;
	    	    				symbols[l] = null;
	    	    				--cellsInBoxLeft;
	    	    				isValid = recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, tempSymbols, cellsInBoxLeft);
	    	    				System.out.println("\n\n\t\t" + l + " tavailable cells @" + x + "," + y);
	    	    				System.out.println("\n\n\t\t" + symbols[l] + " vailable cells @" + x + "," + y);

		    	    	    	for(int i = 0; i < symbols.length; i++) {
		    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
		    	    	    	}
	    	    				if(!isValid){
	    	    					grid.setCell(y, x, null);

	    	    					++cellsInBoxLeft;
	    	    					tempSymbols[l] = tempCell;
	    	    					symbols[l] = tempCell;
	    	    					triedAndFailed = tempCell;
	    	    					System.out.println("A- removing " + tempCell + "from: " + x + "," + y);
	    	    					
	    	    					--numNotNull;
	    	    					if(l == symbols.length-1) {
		    	    					grid.setCell(y, x, null);
		    	    					return false;
	    	    					}
//	    	    					continue;
//		    	    				if(!isValid && lastTried == tempCell) {
//		    	    					return false;
//		    	    				}
	    	    					
	    	    				}
	    	    			}else {
	    	    				++cellsInBoxLeft;
	    	    				if(!isValid && lastCell == tempCell) {
	    	    					System.out.println("yes last one shoudl remove!!!!!!!!!! " + lastCell + " | " + tempCell);
		    	    				System.out.println("\n\n\t\t" + l + " tavailable cells @" + x + "," + y);
		    	    				System.out.println("\n\n\t\t" + symbols[l] + " vailable cells @" + x + "," + y);

			    	    	    	for(int i = 0; i < symbols.length; i++) {
			    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
			    	    	    	}
	    	    					grid.setCell(y, x, null);
	    	    					return false;

	    	    				}
    	    					System.out.println("B - removing " + grid.getCell(y,x) + "from: " + x + "," + y + " cells left: " + cellsInBoxLeft);
    	    					if(lastCell == tempCell) {
        	    					System.out.println("yes last one shoudl remove!!!!!!!!!! " + lastCell + " | " + tempCell);
    	    	    				System.out.println("\n\n\t\t" + l + " tavailable cells @" + x + "," + y);
    	    	    				System.out.println("\n\n\t\t" + symbols[l] + " vailable cells @" + x + "," + y);

    		    	    	    	for(int i = 0; i < symbols.length; i++) {
    		    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
    		    	    	    	}
        	    					grid.setCell(y, x, null);
        	    					return false;

        	    				}
	    	    				grid.setCell(y, x, null);
	    	    				if(l == symbols.length-1 ) {
	    	    					return false;
    	    					}
	    	    				if(l == symbols.length-1) {
	    	    					return false;
    	    					}
	    	    				triedAndFailed = tempCell;

	    	    			}
	    	    			System.out.println("\n\n\t\t B - available cells @" + x + "," + y);
    	    				if(!isValid && lastCell == tempCell) {
    	    					System.out.println("yes last one shoudl remove!!!!!!!!!! " + lastCell + " | " + tempCell);
	    	    				System.out.println("\n\n\t\t" + l + " tavailable cells @" + x + "," + y);
	    	    				System.out.println("\n\n\t\t" + symbols[l] + " vailable cells @" + x + "," + y);

		    	    	    	for(int i = 0; i < symbols.length; i++) {
		    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
		    	    	    	}
    	    					grid.setCell(y, x, null);
    	    					return false;

    	    				}
	    	    			boolean allNull = true;
	    	    	    	for(int i = 0; i < symbols.length; i++) {
	    	    	    		if(symbols[i] != null) {
	    	    	    			failCounter++;
	    	    	    			allNull = false;
	    	    	    		}
	    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
	    	    	    	}
	    	    	    	if(allNull || failCounter == 1) {
	    	    	    		System.out.println("ALL ARE NULL, SETING BACK TO NULL");
    	    					grid.setCell(y, x, null);
    	    					return false;
    	    					
	    	    	    	}
//	    	    	    	8 is valid @ 2,2pos: 7
//	    	    			boolean a = true;
//	    	    			for(int j = 0; j < symbols.length; ++j) {
//		    					if(symbols[j] != null) {
//		    						a = false;
////		    						break;
//		    					}
//		    				}
//	    	    			if(a && triedAndFailed == symbols[l]) {
//	    	    				grid.setCell(y, x, null);
//	    	    				return false;
//	    	    			}
	    	    			

//	    	    			if(numNotNull <1) {
//	    	    				System.out.println("FAIL VALUE TOO LOW @" + x + "," + y);
//	    	    				grid.setCell(y, x, null);
//	    	    				return false;
//	    	    			}
	    	    			
	    	    			//if element is only one in list, and it has failed then return false
	    				}
	    			}

//	    	    	System.out.println("\n\n");
//	    	    	for(int l = 0; l < symbols.length; ++l) {
//
//	    	    		if(symbols[l] != null) {
//	    	    			System.out.println("trying: " + symbols[l] + "from: " + x + "," + y);
//	    	    			System.out.println(grid.toString());
//	    	    			//set cell and verify
//	    	    			
//	    	    			grid.setCell(y, x, symbols[l]);
//	    	    			Integer tempCell = symbols[l];
//	    	    			if(grid.verifyCell(y, x, boxIDx, boxIDy)) {
//	    	    				symbols[l] = null;
//	    	    				--cellsInBoxLeft;
//	    	    				System.out.println("successfuly placed: " + tempCell + "at: " + x + "," + y);
//	    	    				isValid = recursiveBacktrack(grid, boxIDx, boxIDy, boxLimitx, boxLimity, symbols, cellsInBoxLeft);
//	    	    				if(!isValid) {
//	    	    					++cellsInBoxLeft;
//	    	    					symbols[l] = tempCell;
//	    	    					triedAndFailed = tempCell;
//	    	    					grid.setCell(y, x, null);
//	    	    				}
//	    	    				
//	    	    			}else {
//	    	    				grid.setCell(y, x, null);
//	    	    				if(symbols[l] == triedAndFailed) {
//	    	    					failCounter++;
//	    	    					if(failCounter > 1) {
//	    	    						return false;
//	    	    					}
//	    	    					
//	    	    				}
//	    	    			}
//	    	    		}
//	    	    	}
	    	    	
    			}
    		}
    	}
    	
    	
    	
//    	
//    	
//    	for(int y = boxIDx; y < boxLimitx; ++y) {
//    		for(int x = boxIDy; x < boxLimity ; ++x) {
//    			if(grid.getCell(y, x) == null) {
//	    	    	for(int l = 0; l < symbols.length; ++l) {
//	    	    		System.out.println("\t\t\t\t\t\t\t COUNT:" +l + " on: " +  y + "," + x);
//	    	    		if(symbols[l] != null) {
//	    	    			System.out.println("trying: " + symbols[l] + "from: " + y + "," + x);
//	    	    			System.out.println(grid.toString());
//	    	    	    	System.out.println("\n\n");
//	    	    			System.out.println("\n\n\t\tavailable cells:");
//	    	    	    	for( int i = 0; i < symbols.length; i++) {
//	    	    	    		System.out.println("\t\t\tcells: " + symbols[i]);
//	    	    	    	}
//	    	    	    	if(symbols[l] == triedAndFailed) {
//	    	    	    		System.out.println("TRIED AND FAILED: " + triedAndFailed);
//	    	    	    		return false;
//	    	    	    	}
//	    	    	    	System.out.println("\n\n");
//		    	    		grid.setCell(y, x, symbols[l]);
//		    	    		Integer tempCell = symbols[l];
////			    	    	System.out.println(grid.toString());
//
//		    	    		if(grid.verifyCell(x, y, boxIDx, boxIDy)) {
//			    	    		symbols[l] = null;
//		    	    			--cellsInBoxLeft;
//		    	    			int a = id;
//		    	    			isValid = recursiveBacktrack(a+=1, grid, boxIDx, boxIDy, boxLimitx, boxLimity, symbols, cellsInBoxLeft);
//		    	    	    	System.out.println("\n\n\n\n\n\t\t\t ID: " + id);
//
//		    	    			if(!isValid) {
//			    	    			System.out.println("GOING BACK OUT OF LOOP2");
//			    	    			symbols[l] = tempCell;
////			    	    			if(tempCell == 2) {
////			    	    				System.exit(0);
////			    	    			}
//			    	    			triedAndFailed = tempCell;
//			    	    			grid.setCell(y, x, null);
//			    	    			++cellsInBoxLeft;
////			    	    			l;
//		    	    			continue;
//		    	    			}
//		    	    		}else {
////		    	    			++cellsInBoxLeft;
////		    	    			symbols[l] = tempCell;
//		    	    			System.out.println("tempcell:" + tempCell) ;
//		    	    			triedAndFailed = tempCell;
//		    	    			grid.setCell(y, x, null);
//		    	    			
//		    	    		}
//		    	    		Integer notNullCount = 0;
//		    	    		for( int i = 0; i < symbols.length; i++) {
//		    	    			if(symbols[i]!=null) {
//		    	    				notNullCount = symbols[i];
//		    	    			}
//		    	    		}
//		    	    		if(l == (symbols.length -1)){
//	    	    				System.out.println("ABCDEF2: reached last index: " + l );
//		    	    			return false;
//	    	    			}
//		    	    		
//	    	    		}
//	    	    	}
//    			}
//    		}
//    	}    	
    	
	    System.out.println("\n\n\n\t\t\t\t CELLS LEFT  IN BOX: " + cellsInBoxLeft);
		return isValid;
    }

} // end of class BackTrackingSolver()
