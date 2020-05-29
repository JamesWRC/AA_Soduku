/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
    // TODO: Add attributes as needed.
	private int gameSize = 0;
	private int gridSize = 0;
	private int totalNumRows = 0;
	private int totalNumCols = 0;
	private int symbolAmt = 0;
	
	
	
	private static final int NUM_OF_COL_CONSTRAINTS = 4;
	private static final Integer POSSIBLE_SET = 1;
	private static final Integer EMPTY = null;

    public AlgorXSolver() {
    	
        // TODO: any initialisation you want to implement.
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	
    	//set the size of boxes.
    	gameSize = (int) Math.sqrt(grid.getSize());

    	//set grid size
    	gridSize = grid.getSize();
    	//set symbol amount
    	symbolAmt = grid.getSymbols().length;
    	//rows = (gameSize*gameSize) * gameSize
    	totalNumRows = (gridSize*gridSize) * gridSize;
    	//cols = (gameSize*gameSize) * NUM_OF_COL_CONSTRAINTS (4) <--num of col restrictions.
    	totalNumCols = (gridSize*gridSize) * NUM_OF_COL_CONSTRAINTS;
    	
    	//empty binary matrix, ready to be constructed by the buildMatrix method.
    	System.out.println("MROWS: " + totalNumRows + " MCOLS: " + totalNumCols);
    	Integer[][] matrix = new Integer[totalNumRows][totalNumCols];
    	matrix = buildMatrix(matrix, grid);		
    	
    	//Binary matrix constructed! Ready to solve!!

    	recursiveSolve(matrix, grid);
    
    	printMatrix(matrix);
//    	for(int x = 0; x < gridSize; ++x) {
//    		for(int y = 0; y < gridSize; ++y) {
////    			if(grid.getCell(x, y))
//    			System.out.println(grid.getCell(x, y));
//    		}
//    	}
    	return false;
    } // end of solve()

    
    
    private Integer[][] buildMatrix(Integer[][] matrix, SudokuGrid grid){
    	//this function builds the matrix of 1s or nulls.
    	
    	//boxNumbers is used to get the box number based off coordinates in the grid.
    	int[][] boxNumbers = new int[gameSize][gameSize];
    	
    	int groupSize = gridSize*gridSize;

    	//row constraints
    	int rowConstraintOffset = groupSize;
    	int rowConstraintEnd = rowConstraintOffset + groupSize;
    	
    	//col constraints
    	int colConstraintStart = rowConstraintEnd;
    	int colConstraintEnd = rowConstraintEnd + groupSize;
    	
    	//box constraints
    	int boxConstraintStart = colConstraintEnd;
    	
    	
    	
    	/*	build boxNumber array
    	 * 	- This is used to get the box number based off coordinated in the grid.
    	 * 	- Is used to get an offset in the grid array.
    	 * 	- Box offsets start from 0, starting left to right, top to bottom.
    	 */
    	int boxCounter = -1;
    	for(int col = 0; col < gameSize; ++col) {
    		for(int row = 0; row < gameSize; ++row) {
    			boxNumbers[col][row] = ++boxCounter;
    		}
    	}
    	//construct grid with 1s or nulls.
    	int count = 0;
    	int cellCount = 0;
		for(int row = 0; row < gridSize; ++row) {
	    	int colCount = 0;
    		for(int col = 0; col < gridSize; ++col) {
    			for(int cell = 0; cell < symbolAmt; ++cell) {
    				//only add 1's to cols if they are empty.
    				if(grid.getCell(row, col)==null) {
//    					System.out.println("value: " + grid.getCell(row, col) + " @ " + row + " , " + col );
    				
            		matrix[count][cellCount] = POSSIBLE_SET;
            		matrix[count][(row*gridSize)+rowConstraintOffset+cell] = POSSIBLE_SET;
            		matrix[count][colCount+colConstraintStart] = POSSIBLE_SET;
            		/*	Get the box number to offset.
            		 * 	- Scanning from top to bottom of grid, left to right 
            		 */
            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
            		matrix[count][boxConstraintStart+cell+((boxNumber)*symbolAmt)] = POSSIBLE_SET;
    				}
	    			++count;
	        		++colCount;
	        		
            	}
    			++cellCount;
    		}
    	}
		return matrix;
    }
    
    
    
    private void printMatrix(Integer[][] matrix) {
    	System.out.println("BEEP BOOP \n\n\n\n\n");
    	StringBuilder builder = new StringBuilder();
    	for(int i=0; i<totalNumRows; ++i) {
    		for(int j=0; j<totalNumCols; ++j) {
    			if(matrix[i][j] != null) {
        			builder.append(matrix[i][j]);
    			}else {
    				builder.append(" ");
    			}

    			//apply commas after all except last in row, 
    			//the one at the end will add a line break
    			if(j<totalNumCols-1) {
    				builder.append(",");
    			}else {
    				builder.append("\n");
    			}
    		}
    	}
    	System.out.println(builder);
    }
    
    
    private boolean recursiveSolve(Integer[][] matrix, SudokuGrid partialSolution) {
    	/*
    	 * check if grid is empty
    	 * if grid is not empty
    	 *		choose the FIRST column with the lowest number of 1s
    	 * 	NOTES: NEED TO REMOVE ROWS TOO.
    	 */
    	System.out.println("\n\n\n");
    	System.out.println("OPTIONS");
    	System.out.println("gridSize: " + gridSize + " totalNumRows: " + totalNumRows + " totalNumCols: " + totalNumCols);

    	System.out.println("\n\n\n");
		System.out.println(partialSolution.toString());
    	System.out.println("\n\n\n");
    	printMatrix(matrix);
    	boolean isValid = true;

    	for(int row = 0; row < totalNumRows; ++row) {
    		int getRealY = 0;
    		for(int col = 0; col < totalNumCols; ++col) {
    			for(int cell = 0; cell < symbolAmt; ++cell) {
	    			//set up getRealY
//    				System.out.println("Val123: " + gridSize*symbolAmt);
//    				System.out.println("symamnt: " + symbolAmt);
//    				System.out.println("ColvAL: " + col);
    				if(cell % symbolAmt == 0 && col != 0) {
    					++getRealY;
    				}
    				if(getRealY % gridSize == 0) {
        				getRealY = 0;
        			}
    				
	    			//need to copy the column and row.
	    			Integer[] tempRow = copyRow(row, matrix);
	    			Integer[] tempCol = copyCol(col, matrix);

//	    			System.out.println("row: " + row + ", col: " + col + " | cell: "+ cell);

//    				System.out.println("x: " + getRealX + " y: " + getRealY + " cell: " + cell);
	    			
	    			/*	
	    			 * 	Check if row has a 1 in it, then it is valid.
	    			 *	We only need to check if the column OR the row is valid. Checking row will be less time on larger grids.
	    			 *	If any 1s are found in the selected row it will also purge / nullify the respective column.
	    			 */
	    			if(validateRowAndPurgeCols(tempRow, matrix)) {
	    				System.out.println("FOUD VALID");
	    		    	int tempX = 0;
	    		    	int tempY = 0;
//	    		    	boolean addedToPartialSolution = false;
//		    			for(int x = 0; x < gridSize; ++x) {
//	    		    		for(int y = 0; y < gridSize; ++y) {
//	    		    			if(partialSolution.getCell(x, y) == null) {
//	    		    				partialSolution.setCell(x, y, cell);
//	    		    				tempX = x;
//	    		    				tempY = y;
//	    		    				addedToPartialSolution = true;
//	    		    				break;
//	    		    			}
//	    		    		}
//	    		    		if(addedToPartialSolution) {
//	    		    			break;
//	    		    		}
//	    		    	}
		    			int getRealX = (int) Math.floor(row/(gridSize*symbolAmt));

    		    		System.out.println("setting cell @ " + getRealY + ", " + getRealX + " --> " + cell);

	    		    	if(partialSolution.getCell(getRealY, getRealX) == null) {
		    				partialSolution.setCell(getRealY, getRealX, cell);
	    		    	}
		    			//now that we have added the possible row to the partial solution, delete the row and col
		    			System.out.println("Nullify");
		    			nullifyRow(row, matrix);
		    			nullifyCol(col, matrix);
		    			isValid = recursiveSolve(matrix, partialSolution);
//	    				go down 1 level
	    				if(!isValid) {				
			    			//Unsent the cell at the given position
	    					partialSolution.setCell(getRealX, getRealY, null);
			    			
			    			//need to repair the row and columns
			    			repairRow(col, matrix, tempRow);
			    			repairCol(row, matrix, tempCol);
	    				}
	    			}
    			}
    		}
    	}
    	return true;
    }
    
    //copy the row to a temp array and return the row
    private Integer[] copyRow(int rowPos, Integer[][] matrix) {
    	
    	Integer[] retRow = new Integer[totalNumCols];
    	//copy col
		for(int col = 0; col < totalNumCols; ++col) {
	    	retRow[col] = matrix[rowPos][col];
		}
		return retRow;
	}
    
    //copy the column to a temp array and return that column.
    private Integer[] copyCol(int colPos, Integer[][] matrix) {
    	Integer[] retCol = new Integer[totalNumRows];
    	//copy row
    	
		for(int row = 0; row < totalNumRows; ++row) {
			retCol[row] = matrix[row][colPos];
		}
		return retCol;
	}
    
    // simply check if the array has any 1s, if so then its true.
    // Nullify operation to remove all 1s every column where a 1 is in the row.

    private boolean validateRowAndPurgeCols(Integer[] array, Integer[][] matrix) {
    	boolean isValid = false;
    	for(int i = 0; i < array.length; ++i) {
    		if(array[i] == POSSIBLE_SET) {	
    			//purge columns if a 1 is found
    			nullifyCol(i, matrix);
    			isValid = true;
    		}
    	}
    	return isValid;
    }
    
    
    // # # # Nullify the row / col # # #

    //nullify the row 
    private void nullifyRow(int rowPos, Integer[][] matrix) {
		for(int col = 0; col < totalNumCols; ++col) {
	    	matrix[rowPos][col] = null;;
		}
	}
    
    //nullify the column.
    private void nullifyCol(int colPos, Integer[][] matrix) {
		for(int row = 0; row < totalNumRows; ++row) {
			matrix[row][colPos] = null;;
		}
	}
    
    // # # # replace the array back to the grid # # #
    
    //crepair the row in the matrix that was removed.
    private void repairRow(int rowPos, Integer[][] matrix, Integer[] array) {
    	//repair the matrix
		for(int col = 0; col < totalNumCols; ++col) {
			 matrix[rowPos][col] = array[col];
		}
	}
    //repair the col in the matrix that was removed.
    private void repairCol(int colPos, Integer[][] matrix, Integer[] array) {
		for(int row = 0; row < totalNumRows; ++row) {
			matrix[row][colPos] = array[row];
		}
	}

		
} // end of class AlgorXSolver
