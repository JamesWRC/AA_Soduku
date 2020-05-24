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
    	//cols = (gameSize*gameSize) * 4 <--num of col restrictions.
    	totalNumCols = (gridSize*gridSize) * 4;
    	
    	//empty binary matrix, ready to be constructed by the buildMatrix method.
    	Integer[][] matrix = new Integer[totalNumRows][totalNumCols];
    	matrix = buildMatrix(matrix);		
    	
    	//Binary matrix constrsucted! Ready to solve!!
    	
    	
    	
    	printMatrix(matrix);
        
    	return false;
    } // end of solve()

    
    
    public Integer[][] buildMatrix(Integer[][] matrix){
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
            		matrix[count][cellCount] = 1;
            		matrix[count][(row*gridSize)+rowConstraintOffset+cell] = 1;
            		matrix[count][colCount+colConstraintStart] = 1;
            		/*	Get the box number to offset.
            		 * 	- Scanning from top to bottom of grid, left to right 
            		 */
            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
            		matrix[count][boxConstraintStart+cell+((boxNumber)*symbolAmt)] = 1;
	    			++count;
	        		++colCount;
            	}
    			++cellCount;
    		}
    	}
		return matrix;
    }
    
    
    
    public void printMatrix(Integer[][] matrix) {
    	//rows = (gameSize*gameSize) * gameSize
    	int totalNumRows = (gridSize*gridSize) * gridSize;
    	//cols = (gameSize*gameSize) * 4 <--num of col restrictions.
    	int totalNumCols = (gridSize*gridSize) * 4;
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
    
    
    public boolean recursiveSolve(SudokuGrid subGrid) {
    	/*
    	 * check if grid is empty
    	 * if grid is not empty
    	 *		choose the FIRST column with the lowest number of 1s
    	 *
    	 */
    	return false;
    }
} // end of class AlgorXSolver
