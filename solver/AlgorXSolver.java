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
	private int gameSize = 0;
	private int gridSize = 0;
	private int totalNumRows = 0;
	private int totalNumCols = 0;
	private int symbolAmt = 0;
	
	
	
	private static final int NUM_OF_COL_CONSTRAINTS = 4;
	private static final Integer POSSIBLE_SET = 1;
	private Integer[] possibleCells = null;
    public AlgorXSolver() {
    	
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	possibleCells = new Integer[grid.getSymbols().length];
    	System.arraycopy(grid.getSymbols(), 0, possibleCells, 0, grid.getSymbols().length); 
    	
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
    	Integer[][] matrix = new Integer[totalNumRows][totalNumCols];
    	matrix = buildMatrix(matrix, grid);		
    	
    	//Binary matrix constructed! Ready to solve!!
    	int count = 0;
    	int colCount = 0;
    	int row = 0;
    	int col = 0;
    	
    	recursiveSolve(row, col, count, colCount, matrix, grid);
   
    	return grid.validate();
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
    	//construct grid / matrix with 1s or nulls.
    	int count = 0;
    	int cellCount = 0;
		for(int row = 0; row < gridSize; ++row) {
	    	int colCount = 0;
    		for(int col = 0; col < gridSize; ++col) {
    			for(int cell = 0; cell < symbolAmt; ++cell) {
            		matrix[count][cellCount] = POSSIBLE_SET;								//	Set cell constraint
            		matrix[count][(row*gridSize)+rowConstraintOffset+cell] = POSSIBLE_SET;	//	Set row constraint
            		matrix[count][colCount+colConstraintStart] = POSSIBLE_SET;				//	Set col constraint
            		/*	Get the box number to offset.
            		 * 	- Scanning from top to bottom of grid, left to right 
            		 */
            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
            		matrix[count][boxConstraintStart+cell+((boxNumber)*symbolAmt)] = POSSIBLE_SET;	//	Set the box constraint
					if(grid.getCell(row, col) == grid.getSymbols()[cell]) {
						//nullifies the row.
						for(int i = 0; i < totalNumCols; ++i) {
		    				if(matrix[count][i] == POSSIBLE_SET) {
	    						matrix[count][i] = -2;
	    						
		    				}else {
    		    				matrix[count][i] = -1;
		    				}
		        		}
					}
    			++count;
        		++colCount;
            	}
    		++cellCount;
    		}
    	}
		for(int col = 0; col < totalNumCols; ++col) {
			boolean isValid = false;
			for(int row = 0; row < totalNumRows; ++row) {
    			if(matrix[row][col] != null && matrix[row][col] == -2) {
    				isValid = true;
    			}
    		}
    		if(isValid) {
    			for(int row = 0; row < totalNumRows; ++row) {
    				matrix[row][col] = -1;
        		}
    		}
    	}
		return matrix;
    }
    
    
    private boolean recursiveSolve(int currRow, int currCol, int currCount, int currColCount, Integer[][] matrix, SudokuGrid partialSolution) {
    	/*
    	 * 
    	 * 	NOTES:	- Everything works fine. Could improve by NOT making a deep copy of the matrix every iteration.
    	 *			- We use a nested for loop just to keep a structure of the grid. 
    	 *			- NEED to improve on efficiency, but will comeback once others work
    	 */

    	Integer[][] usableMatrix = null;
    	boolean isValid = true;
    	int count = currCount;
    	int colCount = currColCount;
    	for(int row = currRow; row < gridSize; ++row) {
    		for(int col = currCol; col < gridSize; ++col) {
    			if(count < totalNumRows) {
	    			boolean colIsValid = false;
	    			for(int cell = 0; cell < symbolAmt; ++cell) {
	
	    				
	    				usableMatrix = copyMatrix(matrix);
	    				
	    				//get the x coordinates
		    			int getRealX = (int) Math.floor(count/(gridSize*symbolAmt));
		    			//need to copy the column and row.
		    			Integer[] tempRow = copyRow(count, usableMatrix); // <-- this is correct
		    			Integer[] tempCol = copyCol(colCount, usableMatrix);
		    			
		    			// we check if the column constraint is already met. 
		    			// this could be improved by picking the column with the least amount of 1s
		    			if(!colIsValid) {
			    			int index = 0;
			    			for(int i = 0; i < tempCol.length; ++i) {
			    				if(tempCol[i] == POSSIBLE_SET) {
			    					colIsValid = true;
	
			    					if(index == 0) {
			    						index = i;
			    						count = i;
			    					}
			    				}
			    			}
			    			tempRow = copyRow(count, usableMatrix); // <-- this is correct
			    			
			    			if(!colIsValid) {
				    			count += (symbolAmt - cell);
				    			colCount++;
				    			
			    				break;
			    			}
		    			}
		    			
		    			int possibleY = (int) Math.floor(count/symbolAmt);
		    			if(possibleY > (symbolAmt-1)) {
		    				col = 0;
		    			}
		    			possibleY = (possibleY % symbolAmt);

		    			/*	
		    			 * 	Check if row has a 1 in it, then it is valid.
		    			 *	We only need to check if the column OR the row is valid. Checking row will be less time on larger grids.
		    			 *	If any 1s are found in the selected row it will also purge / nullify the respective column.
		    			 */
	
			    		boolean isPlacementValid = cover(tempRow, tempCol, usableMatrix, cell);

		    			if(isPlacementValid) {	
		    				//set the cell in the grid which is the partial solution.
	    		    		Integer cellToSet = null;
		    		    	cellToSet = partialSolution.getSymbols()[cell];
			    			partialSolution.setCell(getRealX, possibleY, cellToSet);
		    		    	

			    			//now that we have added the possible row to the partial solution, delete the row and col
			    			isValid = recursiveSolve(row, col, count, colCount, usableMatrix, partialSolution);
			    			//go down 1 level
		    				if(!isValid) {				
				    			//Unsent the cell at the given position
		    					partialSolution.setCell(getRealX, possibleY, null);				    			
				    			//need to repair the row and columns

		    				}else {
		    					return true;
		    				}
		    				//simply terminates unsuccessful of all cells contraint have been exhausted
		    				if(cell == partialSolution.getSymbols().length -1) {
			    				partialSolution.setCell(getRealX, possibleY, null);	
			    				return false;
			    			}
	
		    			}else {
		    				//simply terminates unsuccessfuly of all cells contraint have been exhausted
		    				if(cell == partialSolution.getSymbols().length -1) {
			    				partialSolution.setCell(getRealX, possibleY, null);
			    				return false;
		    				}
		    			}
		    			++count;
	    			}
					if(colIsValid) {
					    ++colCount;
					}
	    		}
    		}
    	}
    	return true;
    }
    
    
    //copies array / matrix to new object. This is used to 'cover' the grid. this could be improved.
    private Integer[][] copyMatrix(Integer[][] matrix) {
    	/*
    	 * simply creates a new matrix which is the same size as the matrix, and copies row by row, column by column
    	 */
    	Integer[][] newMatrix = new Integer[matrix.length][matrix[0].length];
    	for(int row = 0; row < totalNumRows; ++row) {
    		for(int col = 0; col < totalNumCols; ++col) {
    			newMatrix[row][col] = matrix[row][col];
    		}
    	}
    	return newMatrix;
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
    
    // simply check if the array has any 1s, if so then if there are 4 1s then the row meets the column constraints.
    // Nullify operation to remove all 1s every column where a 1 is in the row.
    private boolean validateRowAndPurgeCols(Integer[] rowArray, Integer[] colArray, Integer[][] matrix, int cell) {
    	boolean isValid = false;
    	int count = 0;
    	for(int i = 0; i < rowArray.length; ++i) {
    		if(rowArray[i] == POSSIBLE_SET) {
    			count++;
    		}
    	}

    	if(count == 4) {
	    	for(int i = 0; i < colArray.length; ++i) {
	    		if(colArray[i] == POSSIBLE_SET) {	
	    			//purge columns if a 1 is found
	    			nullifyRow(i+cell, matrix);
	    			isValid = true;
	    			return true;
	    		}
	    	}
    	}
    	return isValid;
    }
    
    //'cover' the grid based on the selection. 
    // note this method is a wrapper to cover.
    // if I have time I will come back and improve speed by efficiently covering rather then cover by deep copying the matrix.
    private boolean cover(Integer[] rowArray, Integer[] colArray, Integer[][] matrix, int cell) {
    	boolean isValid = false;
    	isValid = validateRowAndPurgeCols(rowArray, colArray, matrix,cell);
    	return isValid;
    }

   
    //nullify the row 
    private void nullifyRow(int rowPos, Integer[][] matrix) {

		for(int col = 0; col < totalNumCols; ++col) {
			if(matrix[rowPos][col] == POSSIBLE_SET) {
				nullifyCol(col, matrix);
			}
	    	matrix[rowPos][col] = -1;
		}
	}
    
    //nullify the column.
    private void nullifyCol(int colPos, Integer[][] matrix) {
		for(int row = 0; row < totalNumRows; ++row) {
				matrix[row][colPos] = -1;
		}
	}
    
    // # # # replace the array back to the grid # # #
    
    //repair the row in the matrix that was removed.
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
