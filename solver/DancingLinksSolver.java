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

    	columnList = translateMatrixToCircular2DDoublyLinkedList(columnList); // bit of a long method, but it is descriptive.
//    	printColumnNodes(columnList);
    	recursiveSolve(columnList, grid);
        return false;
    } // end of solve()
    
	private LinkedList<ColumnNode> translateMatrixToCircular2DDoublyLinkedList(LinkedList<ColumnNode> columnList) {
    	
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
            		/*
            		 *	Operations in the below if statements:
            		 *	BEGIN
            		 *		IF linked list in the columnNode is empty:
            		 *			SET columnNode 'down' reference to the current Node.
            		 *			SET current Node 'up' reference to the columnNode
            		 *			SET current Node 'down' reference to the columnNode.
            		 *		ELSE
            		 *			SET columnNode 'up' referenced Nodes 'down' reference to the current Node.
            		 *			SET current Node 'up' reference to the columnNodes 'up' referenced Node.
            		 *			SET current Node 'down' reference to the columnNode.
            		 *	END
            		 *	
            		 *	- These operations make the columns a circular doubly linked list.
            		 */
            		
            		//	Construct the references for cellNode and cellConstraintColumn.
            		if(cellConstraintColumn.getList().isEmpty()) {
            			cellConstraintColumn.setDown(cellNode);
            			cellNode.setUp(cellConstraintColumn);
            			cellNode.setDown(cellConstraintColumn);
            		}else {
            			cellConstraintColumn.getUp().setDown(cellNode);
            			cellNode.setUp(cellConstraintColumn.getUp());
            			cellNode.setDown(cellConstraintColumn);
            		}
            		
            		//	Construct the references for rowNode and rowConstraintColumn.
            		if(rowConstraintColumn.getList().isEmpty()) {
            			rowConstraintColumn.setDown(rowNode);
            			rowNode.setUp(rowConstraintColumn);
            			rowNode.setDown(rowConstraintColumn);
            		}else {
            			rowConstraintColumn.getUp().setDown(rowNode);
            			rowNode.setUp(rowConstraintColumn.getUp());
            			rowNode.setDown(rowConstraintColumn);
            		}
            		
            		//	Construct the references for colNode and colConstraintColumn.
            		if(colConstraintColumn.getList().isEmpty()) {
            			colConstraintColumn.setDown(colNode);
            			colNode.setUp(colConstraintColumn);
            			colNode.setDown(colConstraintColumn);
            		}else {
            			colConstraintColumn.getUp().setDown(colNode);
            			colNode.setUp(colConstraintColumn.getUp());
            			colNode.setDown(colConstraintColumn);
            		}
            		
            		//	Construct the references for boxNode and boxConstraintColumn.
					if(boxConstraintColumn.getList().isEmpty()) {
						boxConstraintColumn.setDown(boxNode);
						boxNode.setUp(boxConstraintColumn);
						boxNode.setDown(boxConstraintColumn);
					}else {
						boxConstraintColumn.getUp().setDown(boxNode);
						boxNode.setUp(boxConstraintColumn.getUp());
						boxNode.setDown(boxConstraintColumn);
					}
            		
            		//	Set the 'up' reference for the column.
            		cellConstraintColumn.setUp(cellNode);
            		rowConstraintColumn.setUp(rowNode);
            		colConstraintColumn.setUp(colNode);
            		boxConstraintColumn.setUp(boxNode);
            		
            		//	Add the nodes to their respective column lists.
            		cellConstraintColumn.getList().add(cellNode);
            		rowConstraintColumn.getList().add(rowNode);
            		colConstraintColumn.getList().add(colNode);
            		boxConstraintColumn.getList().add(boxNode);
            		
            		
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
    
	private boolean recursiveSolve(LinkedList<ColumnNode> columnList, SudokuGrid grid ) {
		/*
		 *	NOTES: need to uncover in reverse order.
		 * 	-	Each column we cover will be stored in the coveredColumns linked list.
		 *	-	Need to search for the first column with the least number of 1s.
		 * 	-	Covering
		 * 		-	columnNodes left and right references need to be updated.
 		 *		-	columnNodes will be inserted at the start of the coveredColumns linked list.
 		 *		-	All nodes the column has will perform 'unlinking' of the column links NOT row links.
 		 * 		-	Update the number of 1s in the col, by referencing the nodes columnNode. -=1
 		 *		-	Will add the cell to the partial answer grid.
		 * 	-	Uncovering 
		 * 		-	Columns will be uncovered in a LIFO (Last In First Out) order.
		 * 		-	columnNodes left and right references need to be updated.
		 * 		-	Every node the column has will go to its right, checking if the node above and below has a reference to it,
		 * 			which if it does not will reset the link again. This process will stop once the nodesColumn ID matches up
		 * 			again. In short it will do a full circle, checking and repairing links.
		 * 		-	Each node that is repaired will update its columnNode. += 1
		 * 		- 	When the node is uncovered (row/col) remove cell from partial answer grid.
		 * 	-	Rinse and repeat until grid is complete.
		 */
		
		//	Initialize the count with the number of symbols allowed in the game. 
		int leastOnesCount = symbolAmt;
		
		//	Create linkedList to hold columnNode we are going to cover
    	LinkedList<ColumnNode> coveredColumns = new LinkedList<ColumnNode>();
		
		//	Need to hold reference of column.
		ColumnNode leastOnesColumn = null;
		
		//	Need to start off at the offset as we don't want to include the master node in the loop.
		for(int col = COL_LIST_OFFSET; col < columnList.size(); ++col) {
			if(columnList.get(col).getNumberOfOnes() < leastOnesCount) {
				leastOnesCount = columnList.get(col).getNumberOfOnes();
				leastOnesColumn = columnList.get(col);
			}
		}
		//	If leastOnesColumn is still null, it means that this is the first iteration. 
		//	and the first column should be selected, as it is the first column with the least amount of ones.
		if(leastOnesColumn == null) {
			leastOnesColumn = columnList.get(COL_LIST_OFFSET);
		}
		
		//	Select first node of the column
		Node nodeSelected = leastOnesColumn.getDown();
		
		//	Get the columnID. Used to ensure unlinking of 'up' and 'down' references don't happen on this node in the row.
		int nodeColumnID = nodeSelected.getColNode().getColumnID();
		int nodeRowID = nodeSelected.getRowPosition();

		System.out.println("col: " + nodeColumnID + " row: " + nodeRowID);
		
		//	Iterate through the nodes to the right until we loop back to the nodeSelected.
		Node tempRowNode = nodeSelected;
		
		//	############ START COVER #############
		
		Node tempChosenCol = nodeSelected;
		for(int i = 0; i < tempChosenCol.getColNode().getNumberOfOnes(); ++i) {
			if(!tempChosenCol.getDown().getClass().equals(tempChosenCol.getColNode().getClass())){
			}
		}
		
		//	Cover other columns and rows
		
		coveredColumns.addFirst(tempRowNode.getColNode());
		System.out.println("starting at colid: " + nodeSelected.getColNode().getColumnID());

		for(int i = 0; i < symbolAmt; ++i) {
			tempRowNode = tempRowNode.getRight();
			if(!nodeSelected.equals(tempRowNode)) {
				System.out.println("aaa: " + nodeSelected.getRowPosition());
				Node tempColNode = tempRowNode;
				System.out.println("B - covering column: " + tempRowNode.getColNode().getColumnID());
				coveredColumns.addFirst(tempRowNode.getColNode());
				coverColumnNode(tempRowNode.getColNode());
				System.out.println("AS: " + tempRowNode.getColNode().getNumberOfOnes());
				for(int j = 0; j < tempRowNode.getColNode().getNumberOfOnes(); ++j) {
					//	Make sure we set the tempColNode to another node in the list and NOT the columnNode itself.
					if(!tempColNode.getUp().getClass().equals(tempColNode.getColNode().getClass())){
						tempColNode = tempColNode.getUp();
					}else {
						//	Skip over the columnNode.
						tempColNode = tempColNode.getUp().getUp();
					}
					if(!tempRowNode.equals(tempColNode)) {
						//	Cover the row Nodes
						Node tempColRowNode = tempColNode;
//						System.out.println("F - covering node col: " + 
//								tempColNode.getColNode().getColumnID() + " node row: " + tempColNode.getRowPosition());
						for(int l = 1; l < symbolAmt; ++l) {
							tempColRowNode = tempColRowNode.getRight();
							if(!tempRowNode.equals(tempColNode)) {
							System.out.println("A - covering node col: " + 
							tempColRowNode.getColNode().getColumnID() + " node row: " + tempColRowNode.getRowPosition());
//							tempColRowNode.getColNode().setNumberOfOnes(tempColRowNode.getColNode().getNumberOfOnes() - 1);

							coverNodeInRow(tempColRowNode);
							}
						}
					}
				}
				//	Cover column
				
				//	Need to iterate through row
				
				
				
				//	Do 'up' and 'down' node dereferencing of node.
//				System.out.println("DOES NOT EQUAL");
//				System.out.println("C - col: " + tempRowNode.getColNode().getColumnID() + " row: " + tempRowNode.getRowPosition());

			}
			System.out.println("C - covering col: " + tempRowNode.getColNode().getColumnID() + " row: " + tempRowNode.getRowPosition());

		}
		
		//	############ END COVER #############
		printColumnNodes(columnList);
		System.out.println("---------------------");
		
		//	############ START UNCOVER #############
		
		for(int numCols = 0; numCols < coveredColumns.size(); ++numCols) {
			if(!leastOnesColumn.equals(coveredColumns.get(numCols))) {
			nodeSelected = coveredColumns.get(numCols).getDown();
			tempRowNode = nodeSelected;
			System.out.println("x - " + nodeSelected.getRowPosition());
			System.out.println("starting at colid: " + nodeSelected.getColNode().getColumnID());
			
			Node tempColNode = tempRowNode;
			uncoverColumnNode(tempRowNode.getColNode());

			for(int i = 0; i < tempColNode.getColNode().getNumberOfOnes(); ++i) {
				if(!tempColNode.getDown().getClass().equals(tempColNode.getColNode().getClass())){
					tempColNode = tempColNode.getDown();
				}else {
					//	Skip over the columnNode.
					tempColNode = tempColNode.getDown().getDown();
				}
				if(!tempRowNode.equals(tempColNode)) {

				Node tempColRowNode = tempColNode;

				for(int l = 1; l < symbolAmt; ++l) {
					if(!tempRowNode.equals(tempColNode)) {
					tempColRowNode = tempColRowNode.getLeft();
					System.out.println("A - covering node col: " + 
					tempColRowNode.getColNode().getColumnID() + " node row: " + tempColRowNode.getRowPosition());
//					tempColRowNode.getColNode().setNumberOfOnes(tempColRowNode.getColNode().getNumberOfOnes() + 1);

					uncoverNodeInRow(tempColRowNode);
					}
				}
				}
					
				System.out.println("C - covering col: " + tempRowNode.getColNode().getColumnID() + " row: " + tempRowNode.getRowPosition());
	
			}
		}
		}
		

		
		//	############ END UNCOVER #############

		
		printColumnNodes(columnList);
		System.out.println("==========");
		for(int i = 0; i < coveredColumns.size(); i++) {
			System.out.println("removed col IDs: " + coveredColumns.get(i).getColumnID());

		}
		boolean isValid = false;
		
		
		printColumnNodes(columnList);

		
		return isValid;
	}
    
	//	Covers and removes the 'up' and 'down' links to this Node in the columnNode linked list.
	private void coverNodeInRow(Node nodeToCover) {
		nodeToCover.getUp().setDown(nodeToCover.getDown());
		nodeToCover.getDown().setUp(nodeToCover.getUp());
		//	Remove one from the number of 1s in the column
		nodeToCover.getColNode().setNumberOfOnes(nodeToCover.getColNode().getNumberOfOnes() - 1);
	}
	
	//	Uncovers and resets the 'up' and 'down' links to this Node in the columnNode linked list.
	private void uncoverNodeInRow(Node nodeToUncover) {
		nodeToUncover.getUp().setDown(nodeToUncover);
		nodeToUncover.getDown().setUp(nodeToUncover);
		//	Add one from the number of 1s in the column
		nodeToUncover.getColNode().setNumberOfOnes(nodeToUncover.getColNode().getNumberOfOnes() + 1);

	}
	
	//	Covers and removes the 'left' and 'right' links to this columnNode in the columnList.
	private void coverColumnNode(ColumnNode columnNodeToCover) {
		columnNodeToCover.getLeft().setRight(columnNodeToCover.getRight());
		columnNodeToCover.getRight().setLeft(columnNodeToCover.getLeft());
	}
	
	//	Uncovers and resets the 'left' and 'right' links to this columnNode in the columnList.
	private void uncoverColumnNode(ColumnNode columnNodeToUncover) {
		columnNodeToUncover.getLeft().setRight(columnNodeToUncover);
		columnNodeToUncover.getRight().setLeft(columnNodeToUncover);
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




