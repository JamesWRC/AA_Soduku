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
	private static final int COL_LIST_OFFSET = 1;

	private Integer[] possibleCells = null;
	//create arraylist
    public DancingLinksSolver() {
        // TODO: any initialisation you want to implement.
    } // end of DancingLinksSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	possibleCells = new Integer[grid.getSymbols().length];
    	System.arraycopy(grid.getSymbols(), 0, possibleCells, 0, grid.getSymbols().length); 
    	LinkedList<ColumnNode> columnList = null;
    	LinkedList<ColumnNode> coveredColumns = new LinkedList<ColumnNode>();
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

    	translateMatrixToCircular2DDoublyLinkedList(grid, columnList); // bit of a long method, but it is descriptive.
//    	printColumnNodes(columnList);
    	recursiveSolve(columnList, coveredColumns);
        return false;
    } // end of solve()
    
	private LinkedList<ColumnNode> translateMatrixToCircular2DDoublyLinkedList(SudokuGrid grid, LinkedList<ColumnNode> columnList) {
    	
    	//	Construct the 2D LinkedList.
    	columnList = new LinkedList<ColumnNode>();
		
    	//	boxNumbers is used to get the box number based off coordinates in the grid.
    	int[][] boxNumbers = new int[gameSize][gameSize];
    	
    	int groupSize = gridSize*gridSize;

    	//	row constraints
    	int rowConstraintOffset = groupSize;
    	int rowConstraintEnd = rowConstraintOffset + groupSize;
    	
    	//	col constraints
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

    	// 	Add the header / 'Master Node'
    	columnList.addFirst(new ColumnNode());
    	for(int col = 0; col < totalNumCols; ++col) {
    		//	create new columnNode
    		ColumnNode colNode = new ColumnNode();

    		//	set colNode left reference to the head of the list.
    		colNode.setLeft(columnList.get(col));
    		
    		//	set the head of the lists right reference to colNode.
    		columnList.get(col).setRight(colNode);
    		
    		//	set the column id (the column number).
    		colNode.setColumnID(col);
    		
    		//	set the colNode reference to it self as it is the col
    		colNode.setColNode(colNode);
    		
    		//	add the new node with references into the list.
    		columnList.add(colNode);
    		
    		//	set the head to the last node in the list. This makes the list circular.
    		columnList.getFirst().setLeft(columnList.getLast());
    		
    		//	set the tail to the first node in the list. This makes the list circular.
    		columnList.getLast().setRight(columnList.getFirst());
    	}
    	
    	
    	/*
    	 * NOTES: 
    	 * 
    	 * 		- Possibly constructed initial colNode linked list, not sure
    	 * 		- Need to continue building the list structure.
    	 * 		- THEN need to actually make it all work...
    	 */
    	
    	int rowNumber = 0;
    	int cellCount = 0;
		for(int row = 0; row < gridSize; ++row) {
	    	int colCount = 0;
    		for(int col = 0; col < gridSize; ++col) {
    			for(int cell = 0; cell < symbolAmt; ++cell) {
    				/*
    				 * 	Setup:
    				 * 	1.	Create 4 nodes, which will all link together
    				 *		- left and right.
    				 * 		- make circular.
    				 * 	2.	Add data to node and column about their placement.
    				 * 	3.	Add each node to each respective columns.
    				 * 	4.	Add column up and down references.
    				 */
    				
    				//	Set up cell constraint Node.
    	    		Node cellNode = new Node();
    	    		
    	    		//	Set up row constraint Node.
    	    		Node rowNode = new Node();
    	    		
    	    		//	Set up col constraint Node.
    	    		Node colNode = new Node();
    	    		
    	    		//	Set up box constraint Node.
    	    		Node boxNode = new Node();
    	    		
    	    		
    	    		//	create links between nodes to create a circular 2D doubly linked list.
    	    		cellNode.setLeft(boxNode);
    	    		cellNode.setRight(rowNode);
    	    		
    	    		rowNode.setLeft(cellNode);
    	    		rowNode.setRight(colNode);
    	    		
    	    		colNode.setLeft(rowNode);
    	    		colNode.setRight(boxNode);
    	    		
    	    		boxNode.setLeft(colNode);
    	    		boxNode.setRight(cellNode);
    	    		
    	    		//	Get the column ID (name / position in 'grid').
            		//	set the row position where the node sits in the whole 'grid'.
            		int boxNumber = boxNumbers[(int) Math.floor(row/gameSize)][(int) Math.floor(col/gameSize)];
            		int cellConstraintPosition = cellCount;											//	Set cell constraint
            		int rowConstraintPosition = ((row*gridSize)+rowConstraintOffset+cell);			//	Set row constraint
            		int colConstraintPosition = (colCount+colConstraintStart);						//	Set col constraint
            		int boxConstraintPosition = (boxConstraintStart+cell+((boxNumber)*symbolAmt));	//	Set the box constraint

    	    		//	Now assign node attributes based on the columns.
            		//	-	Need to have an offset of 1 (COL_LIST_OFFSET), since the 'Master Node' is at position 0
            		ColumnNode cellConstraintColumn = columnList.get(COL_LIST_OFFSET+cellConstraintPosition);
            		ColumnNode rowConstraintColumn = columnList.get(COL_LIST_OFFSET+rowConstraintPosition);
            		ColumnNode colConstraintColumn = columnList.get(COL_LIST_OFFSET+colConstraintPosition);
            		ColumnNode boxConstraintColumn = columnList.get(COL_LIST_OFFSET+boxConstraintPosition);
            		
            		//	Assign the nodes their column reference.
            		cellNode.setColNode(cellConstraintColumn);
            		rowNode.setColNode(rowConstraintColumn);
            		colNode.setColNode(colConstraintColumn);
            		boxNode.setColNode(boxConstraintColumn);
            		
            		// 	Assign the nodes their position in the 'grid'. Ie assign the 'row' number.
            		cellNode.setRowPosition(rowNumber);
            		rowNode.setRowPosition(rowNumber);
            		colNode.setRowPosition(rowNumber);
            		boxNode.setRowPosition(rowNumber);
            		
            		//	If the column is empty then set the 'down' reference to the current node.
            		if(cellConstraintColumn.getList().isEmpty()) {
            			cellConstraintColumn.setDown(cellNode);
            		}
            		if(rowConstraintColumn.getList().isEmpty()) {
            			rowConstraintColumn.setDown(rowNode);
            		}
            		if(colConstraintColumn.getList().isEmpty()) {
            			colConstraintColumn.setDown(colNode);
            		}
					if(boxConstraintColumn.getList().isEmpty()) {
						boxConstraintColumn.setDown(boxNode);
					}
					
            		//	Add the nodes to their respective column lists.
            		cellConstraintColumn.getList().add(cellNode);
            		rowConstraintColumn.getList().add(rowNode);
            		colConstraintColumn.getList().add(colNode);
            		boxConstraintColumn.getList().add(boxNode);
            		
            		//	Set the 'up' reference for the column.
            		cellConstraintColumn.setUp(cellNode);
            		rowConstraintColumn.setUp(rowNode);
            		colConstraintColumn.setUp(colNode);
            		boxConstraintColumn.setUp(boxNode);
            		
            		//	Set the columns number of 1s it has
            		cellConstraintColumn.setNumberOfOnes(cellConstraintColumn.getNumberOfOnes() + 1);
            		rowConstraintColumn.setNumberOfOnes(rowConstraintColumn.getNumberOfOnes() + 1);
            		colConstraintColumn.setNumberOfOnes(colConstraintColumn.getNumberOfOnes() + 1);
            		boxConstraintColumn.setNumberOfOnes(boxConstraintColumn.getNumberOfOnes() + 1);
    				
				++rowNumber;
        		++colCount;
    			}
        	++cellCount;
    		}
    	}
		
		return columnList;
    }
    
	private boolean recursiveSolve(LinkedList<ColumnNode> columnList, LinkedList<ColumnNode> coveredColumns) {
		/*
		 *	NOTES: need to uncover in reverse order.
		 * 	-	Each column we cover will be stored in the coveredColumns linked list.
		 * 	-	Covering
 		 *		-	columnNodes will be inserted at the start of the coveredColumns linked list.
 		 *		-	All nodes the column has will perform 'unlinking' of the column links NOT row links.
 		 * 		-	Update the number of 1s in the col, by referencing the nodes columnNode. -=1
 		 *		-	Will add the cell to the partial answer grid.
		 * 	-	Uncovering 
		 * 		-	Columns will be uncovered in a LIFO (Last In First Out) order.
		 * 		-	Every node the column has will go to its right, checking if the node above and below has a reference to it,
		 * 			which if it does not will reset the link again. This process will stop once the nodesColumn ID matches up
		 * 			again. In short it will do a full circle, checking and repairing links.
		 * 		-	Each node that is repaired will update its columnNode. += 1
		 * 		- 	When the node is uncovered (row/col) remove cell from partial answer grid.
		 * 	-	Rinse and repeat untill grid is complete.
		 */
		
		
		boolean isValid = false;
		
		
		
		
		return isValid;
	}
    
    //	DEBUG: prints column IDs and the number of 1s is holds
	//	prints out the columns header ID
    private void printColumnNodes(LinkedList<ColumnNode> columnList) {
    	//prints out the columns header ID
    	ColumnNode itterNode = columnList.getFirst();
    	for(int col = 0; col < totalNumCols; ++col) {
    		itterNode = itterNode.getRight();
    		System.out.println("\n\tColumn ID: " + itterNode.getColumnID());
    		System.out.println("\t # of 1s: " + itterNode.getNumberOfOnes());


    	}
    }
} // end of class DancingLinksSolver




