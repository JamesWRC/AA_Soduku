/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.LinkedList;

import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
	private int gameSize = 0;
	private int gridSize = 0;
	private int totalNumRows = 0;
	private int totalNumCols = 0;
	private int symbolAmt = 0;
	
	
	
	private static final int NUM_OF_COL_CONSTRAINTS = 4;
	private static final Integer POSSIBLE_SET = 1;
	private Integer[] possibleCells = null;
    public DancingLinksSolver() {
        // TODO: any initialisation you want to implement.
    } // end of DancingLinksSolver()


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
    	printMatrix(matrix);
    	translateMatrixToCircular2DDoublyLinkedList(matrix, grid); // bit of a long method, but it is descriptive.

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
    	//construct grid / matrix with 1s or nulls.
    	int count = 0;
    	int cellCount = 0;
		for(int row = 0; row < gridSize; ++row) {
	    	int colCount = 0;
    		for(int col = 0; col < gridSize; ++col) {
    			for(int cell = 0; cell < symbolAmt; ++cell) {
            		matrix[count][cellCount] = POSSIBLE_SET;
            		matrix[count][(row*gridSize)+rowConstraintOffset+cell] = POSSIBLE_SET;
            		matrix[count][colCount+colConstraintStart] = POSSIBLE_SET;
            		/*	Get the box number to offset.
            		 * 	- Scanning from top to bottom of grid, left to right 
            		 */
            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
            		matrix[count][boxConstraintStart+cell+((boxNumber)*symbolAmt)] = POSSIBLE_SET;
//					if(grid.getCell(row, col) == grid.getSymbols()[cell]) {
//						//nullifies the row.
//						for(int i = 0; i < totalNumCols; ++i) {
//		    				if(matrix[count][i] == POSSIBLE_SET) {
//	    						matrix[count][i] = -2;
//	    						
//		    				}else {
//    		    				matrix[count][i] = -1;
//		    				}
//		        		}
//					}
    			++count;
        		++colCount;
            	}
    			++cellCount;
    		}
    	}
//		for(int col = 0; col < totalNumCols; ++col) {
//			boolean isValid = false;
//			for(int row = 0; row < totalNumRows; ++row) {
//    			if(matrix[row][col] != null && matrix[row][col] == -2) {
//    				isValid = true;
//    			}
//    		}
//    		if(isValid) {
//    			for(int row = 0; row < totalNumRows; ++row) {
//    				matrix[row][col] = -1;
//        		}
//    		}
//    	}
		return matrix;
    }
    
	private void translateMatrixToCircular2DDoublyLinkedList(Integer[][] matrix, SudokuGrid grid) {
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
    	//	Construct the 2D LinkedList.
    	LinkedList<ColumnNode> columnList = new LinkedList<ColumnNode>();
    	//header
    	columnList.addFirst(new ColumnNode());
    	for(int col = 0; col < totalNumCols; ++col) {
    		//create new columnNode
    		ColumnNode colNode = new ColumnNode();
    		
    		//set colNode left reference to the head of the list.
    		colNode.setLeft(columnList.get(col));
    		
    		//set the head of the lists right reference to colNode.
    		columnList.get(col).setRight(colNode);
    		
    		//add the new node with references into the list.
    		columnList.add(colNode);
    		
    		// set the head to the last node in the list. This makes the list circular.
    		columnList.getFirst().setLeft(columnList.getLast());
    		
    		// set the tail to the first node in the list. This makes the list circular.
    		columnList.getLast().setRight(columnList.getFirst());
    	}
    	System.exit(0);
    	
    	/*
    	 * NOTES: im botta go bed, good night girl ill see ya tomorrow.
    	 * 
    	 * 		- Possibly constructed initial colNode linked list, not sure
    	 * 		- Need to continue building the list structure.
    	 * 		- THEN need to actually make it all work...
    	 */
    	
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
    				
    				
    				
				++count;
        		++colCount;
    			}
        	++cellCount;
    		}
    	}
    }
    
    
    
  //this method is just used for debugging.
    private void printMatrix(Integer[][] matrix) {
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
    
} // end of class DancingLinksSolver




