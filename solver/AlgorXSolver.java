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

    public AlgorXSolver() {
        // TODO: any initialisation you want to implement.
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	gameSize = (int) Math.sqrt(grid.getSize());

    	System.out.println(grid.toString());

        	
    	
        return false;
    } // end of solve()

    
    
    public void buildMatrix(){
    	//this function builds the matrix of 1s or nulls.
    	//rows = (gameSize*gameSize) * gameSize
    	int totalNumRows = (gameSize*gameSize) * gameSize;
    	//cols = (gameSize*gameSize) * 4 <--num of col restrictions.
    	int totalNumCols = (gameSize*gameSize) * 4;

    	System.out.println("rows: " + totalNumRows + ", cols: " + totalNumCols);
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
