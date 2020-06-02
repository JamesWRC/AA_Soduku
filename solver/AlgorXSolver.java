/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import javax.sound.midi.SysexMessage;

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
	private Integer[] possibleCells = null;
    public AlgorXSolver() {
    	
        // TODO: any initialisation you want to implement.
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
    	System.out.println("MROWS: " + totalNumRows + " MCOLS: " + totalNumCols);
    	Integer[][] matrix = new Integer[totalNumRows][totalNumCols];
    	matrix = buildMatrix(matrix, grid);		
    	
    	//Binary matrix constructed! Ready to solve!!
    	int count = 0;
    	int colCount = 0;
    	int row = 0;
    	int col = 0;
    	boolean isValid = false;
    	isValid = recursiveSolve(row, col, count, colCount, matrix, grid);
    
    	System.out.println("Real grid: \n" + grid.toString());

    	return isValid;
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
//    				if(grid.getCell(row, col) == null) {
	//    					System.out.println("value: " + grid.getCell(row, col) + " @ " + row + " , " + col );
	    				
	            		matrix[count][cellCount] = POSSIBLE_SET;
	            		matrix[count][(row*gridSize)+rowConstraintOffset+cell] = POSSIBLE_SET;
	            		matrix[count][colCount+colConstraintStart] = POSSIBLE_SET;
	            		/*	Get the box number to offset.
	            		 * 	- Scanning from top to bottom of grid, left to right 
	            		 */
	            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
	            		matrix[count][boxConstraintStart+cell+((boxNumber)*symbolAmt)] = POSSIBLE_SET;
//    				}else {
    					if(grid.getCell(row, col) == grid.getSymbols()[cell]) {
							System.out.println("ALREADY ENTERED: " + grid.getCell(row, col) + " @ x:" + col + " ,y: " + row);
							//nullifies the row.
    						for(int i = 0; i < totalNumCols; ++i) {
    		    				if(matrix[count][i] == POSSIBLE_SET) {
    	    						matrix[count][i] = -2;
    	    						
    		    				}else {
        		    				matrix[count][i] = -1;
    		    				}
    		        		}
    					}
//    				}
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
//		printMatrix(matrix);
//		System.exit(0);
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
    
    
    
    private boolean recursiveSolve(int currRow, int currCol, int currCount, int currColCount, Integer[][] matrix, SudokuGrid partialSolution) {
    	/*
    	 * check if grid is empty
    	 * if grid is not empty
    	 *		choose the FIRST column with the lowest number of 1s
    	 * 	NOTES: NEED TO REMOVE ROWS TOO.
    	 */
    	System.out.println("\n\n\n");
    	System.out.println("OPTIONS");
    	System.out.println("gridSize: " + gridSize + " totalNumRows: " + totalNumRows + " totalNumCols: " + totalNumCols + " symbols: " + symbolAmt);

    	System.out.println("\n\n\n");
		System.out.println(partialSolution.toString());
    	System.out.println("\n\n\n");
    	printMatrix(matrix);
    	System.out.println("\n\n\n");

    	Integer[][] usableMatrix = null;
    	System.out.println("PRINTING USABLE");
    	boolean isValid = true;
    	int count = currCount;
    	int colCount = currColCount;
    	System.out.println("RECEIVED row: " + currRow + " RECEIVED col :" + currCol);
    	for(int row = currRow; row < gridSize; ++row) {
    		int getRealY = 0;
    		for(int col = currCol; col < gridSize; ++col) {
    			if(count < totalNumRows) {
	    			boolean colIsValid = false;
	
	    			for(int cell = 0; cell < symbolAmt; ++cell) {
	    		    	System.out.println("TURNED row: " + row + " TURNED col :" + col + " cell: " + cell );
	
	    				
	    				usableMatrix = copyMatrix(matrix);
	    				
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
	    				
		    			int getRealX = (int) Math.floor(count/(gridSize*symbolAmt));
		    			//need to copy the column and row.
		    			System.out.println("\n\t\t\t count: " + count + " colCount: " + colCount);
		    			Integer[] tempRow = copyRow(count, usableMatrix); // <-- this is correct
	//	    			Integer[] tempCol = copyCol(getRealY, matrix);
	//	    			Integer[] tempRow = copyRow(getRealY, matrix);
		    			Integer[] tempCol = copyCol(colCount, usableMatrix);
	//	    			check if column is already in use.
		    			
	
		    			if(!colIsValid) {
			    			System.out.println("\t\t\t\t\t Checking column! " + colIsValid);
			    			int index = 0;
			    			for(int i = 0; i < tempCol.length; ++i) {
	//		    				System.out.print(tempCol[i]);
			    				if(tempCol[i] == POSSIBLE_SET) {
			    					colIsValid = true;
		    						System.out.println("A - i: " + i);
	
			    					if(index == 0) {
			    						System.out.println("B - i: " + i);
			    						index = i;
			    						count = i;
			    					}
			    				}
			    			}
			    			tempRow = copyRow(count, usableMatrix); // <-- this is correct
			    			
			    			
			    			
	//		    			Integer[] tempCol = copyCol(getRealY, matrix);
	//		    			Integer[] tempRow = copyRow(getRealY, matrix);
	//		    			Integer[] tempCol = copyCol(colCount, usableMatrix);
	//		    			System.out.println("\n");
			    			if(!colIsValid) {
			    				System.out.println("breaking");
				    			count += (symbolAmt - cell);
				    			colCount++;
				    			
			    				break;
			    			}
		    			}
		    			
		    			int possibleY = (int) Math.floor(count/symbolAmt);
		    			if(possibleY > (symbolAmt-1)) {
	//	    				possibleY = 0;
		    				col = 0;
		    			}
		    			possibleY = (possibleY % symbolAmt);
		    			System.out.println("Possible Y: " + possibleY);
		    			
		    			
		    			System.out.println("colCount: " + count);
	//	    			System.out.println("row: " + row + ", col: " + col + " | cell: "+ cell);
	
	//    				System.out.println("x: " + getRealX + " y: " + getRealY + " cell: " + cell);
		    			
		    			/*	
		    			 * 	Check if row has a 1 in it, then it is valid.
		    			 *	We only need to check if the column OR the row is valid. Checking row will be less time on larger grids.
		    			 *	If any 1s are found in the selected row it will also purge / nullify the respective column.
		    			 */
	
			    		System.out.println("Trying cell @ " + getRealX + ", " + possibleY + " celPos:  " + cell + " --> " + partialSolution.getSymbols()[cell]);
			    		boolean isPlacementValid = cover(tempRow, tempCol, usableMatrix, cell);
			    		System.out.println("Column Position: " + colCount);
			    		System.out.println("is placement valid: " + isPlacementValid);
	
		    			if(isPlacementValid) {
		    				System.out.println("\tSetting cell @ " + getRealX + ", " + possibleY + " celPos:  " + cell + " --> " + partialSolution.getSymbols()[cell]);
	
	
	    		    		Integer cellToSet = null;
		    		    	if(partialSolution.getCell(getRealX, possibleY) == null) {
		    		    		cellToSet = partialSolution.getSymbols()[cell];
		    		    		System.out.println(partialSolution.getCell(getRealX, possibleY));
			    				partialSolution.setCell(getRealX, possibleY, cellToSet);
		    		    		System.out.println(partialSolution.getCell(getRealX, possibleY));
			    				
		    		    	}
		    		    	
	//	    				printMatrix(matrix);
		    				System.out.println(partialSolution.toString());
	//	    				System.exit(0);
	//		    			//now that we have added the possible row to the partial solution, delete the row and col
	//		    			System.out.println("Nullify");
	//		    			nullifyRow(row, matrix);
	//		    			nullifyCol(col, matrix);
			    			isValid = recursiveSolve(row, col, count, colCount, usableMatrix, partialSolution);
	////	    				go down 1 level
		    				if(!isValid) {				
				    			//Unsent the cell at the given position
		    					partialSolution.setCell(row, col, null);
				    			
				    			//need to repair the row and columns
	//			    			repairRow(col, matrix, tempRow);
	//			    			repairCol(row, matrix, tempCol);
		    				}else {
		    					return true;
		    				}
		    				if(cell == partialSolution.getSymbols().length -1) {
			    				System.out.println("returning false1231");
		    					partialSolution.setCell(row, col, null);
	
			    				return false;
			    			}
	
		    			}else {
		    				System.out.println("FAILED PLACEMENT CHECK");
		    				System.out.println("cell: " + cell + "symbol: " + (partialSolution.getSymbols().length -1));
		    				if(cell == partialSolution.getSymbols().length -1) {
			    				System.out.println("returning false1231");
		    					partialSolution.setCell(row, col, null);
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
    
    
    //copies array / matrix to new object
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
    
    // simply check if the array has any 1s, if so then its true.
    // Nullify operation to remove all 1s every column where a 1 is in the row.

    private boolean validateRowAndPurgeCols(Integer[] rowArray, Integer[] colArray, Integer[][] matrix, int cell) {
    	boolean isValid = false;
    	int count = 0;
    	for(int i = 0; i < rowArray.length; ++i) {
    		
    		System.out.print(rowArray[i]);
    		if(rowArray[i] == POSSIBLE_SET) {
    			count++;
    		}
    	}
		System.out.print("\n");
		System.out.println("\t\t\tcount of 1s: " + count);
		System.out.print("\n");

//    	for(int i = 0; i < colArray.length; ++i) {
//    		System.out.print(colArray[i]);
//    	}
//		System.out.print("\n");

    	if(count == 4) {
	    	for(int i = 0; i < colArray.length; ++i) {
	    		if(colArray[i] == POSSIBLE_SET) {	
	        		System.out.println("COLINDEX index: " + i + " Cell: " + cell + " sum: " + (i+cell));

	    			//purge columns if a 1 is found
	    			nullifyRow(i+cell, matrix);
	    			isValid = true;
	    			return true;
	    		}
	//    		System.out.println("x: " + array[i]);
	    	}
	//    	System.out.println("END");
    	}
    	return isValid;
    }
    
    
    private boolean cover(Integer[] rowArray, Integer[] colArray, Integer[][] matrix, int cell) {
    	boolean isValid = false;
    	isValid = validateRowAndPurgeCols(rowArray, colArray, matrix,cell);
    	return isValid;
    }
    
    
    private void purgeRow(Integer[][] matrix) {
    	for(int row = 0; row < totalNumRows; ++row) {
    		for(int col = 0; col < totalNumCols; ++col) {
    			if(matrix[row][col] != null && matrix[row][col] == -2) {
    				for(int rowClear = 0; rowClear < totalNumCols; ++rowClear) {
    					matrix[row][rowClear] = -1;

    				}
    			}
    		}
    	}
    }
    
    // # # # Nullify the row / col # # #

    //nullify the row 
    private void nullifyRow(int rowPos, Integer[][] matrix) {

		for(int col = 0; col < totalNumCols; ++col) {
			if(matrix[rowPos][col] == POSSIBLE_SET) {
				nullifyCol(col, matrix);
			}
	    	matrix[rowPos][col] = -1;

//    		System.out.println("\ty: " + matrix[rowPos][col]);
		}
		System.out.println("ACBUEAJCKLU");
		printMatrix(matrix);
//		System.out.println("DNE");
	}
    
    //nullify the column.
    private void nullifyCol(int colPos, Integer[][] matrix) {
		for(int row = 0; row < totalNumRows; ++row) {
//    		System.out.println("\t\tZ: " + matrix[row][colPos]);

			if(matrix[row][colPos] == POSSIBLE_SET) {
				matrix[row][colPos] = -1;

			}else {
				matrix[row][colPos] = -1;
			}

		}
//		System.out.println("============");
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
