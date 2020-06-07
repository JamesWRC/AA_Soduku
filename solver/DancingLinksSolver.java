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
	private static final int MASTER_NODE_INDEX = 0;

	private Integer[] possibleCells = null;
	LinkedList<ColumnNode> coveredColumnsTest = new LinkedList<ColumnNode>(); //	Remove this later

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

    	columnList = translateMatrixToCircular2DDoublyLinkedList(columnList, grid); // bit of a long method, but it is descriptive.
//    	printColumnNodes(columnList);
    	recursiveSolve(columnList, grid);
        return grid.validate();
    } // end of solve()
    
	private LinkedList<ColumnNode> translateMatrixToCircular2DDoublyLinkedList(LinkedList<ColumnNode> columnList, SudokuGrid grid) {
    	
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
    	ColumnNode masterNode = new ColumnNode();
    	masterNode.setColumnID(-1337); //	Placeholder value to identify the root 'master node'
    	columnList.add(masterNode);
    	//	Begin creating the columnNodes and their 'left' and 'right' referenced to each other.
    	for(int col = 0; col < totalNumCols; ++col) {
    		System.out.println(col);
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

		//	Set up variables to cover existing cells in grid.
		int column = 0;
		int row = 0;
		int cell = 0;
		int columnEarlyStop = gridSize*gridSize;
		
		LinkedList<ColumnNode> coveredColumns = new LinkedList<ColumnNode>();
		Node tempNodeCount = null;
		ColumnNode tempColNode = columnList.get(MASTER_NODE_INDEX);
		//	Iterate through the grid and columnList and cover existing cells in grid.
		for(int colCount = 0; colCount < columnList.size(); ++colCount) {
			tempColNode = tempColNode.getRight();
			tempNodeCount = tempColNode.getColNode();
			column = tempColNode.getColumnID();
			for(int rowCount = 0; rowCount < tempColNode.getNumberOfOnes(); ++rowCount) {

				//	Get the column and row.
				tempNodeCount = tempNodeCount.getDown();

				row = tempNodeCount.getRowPosition();
				//	Convert the column and row numbers into usable numbers within the sudoku grid.
				int getRealRow = (int) Math.floor(row/(gridSize*symbolAmt));
				int getRealCol = column%gridSize;
				cell = row%gridSize;

				
				if(grid.getCell(getRealRow, getRealCol) != null && 
						grid.getCell(getRealRow, getRealCol) == grid.getSymbols()[cell]) {
					//	Need to cover the relevant columns and rows.
					Node nodeSelected = tempNodeCount;
					Node toNotTouch = tempNodeCount;
					Node tempRowNode = nodeSelected;
					System.out.println("COVERING!!!!!");
					System.out.println("\t\t coords in grid - row:  " + getRealRow + " col: " + 
							getRealCol + " cell: " + cell + " value: " + grid.getSymbols()[cell]);

					System.out.println("\t\t\t RAW DATA: row: " + nodeSelected.getRowPosition() + " COL: " + nodeSelected.getColNode().getColumnID());
					cover(nodeSelected, tempRowNode, toNotTouch, coveredColumns, grid);
					System.out.println("\t\t\t\t\t COL " + nodeSelected.getColNode().getColumnID() + "CHECKING NUMBERS: " + nodeSelected.getColNode().getNumberOfOnes());
//					if(nodeSelected.getColNode().isCovered()) {
//						System.out.println("isCovered Removing");
//
//						coverColumnNode(nodeSelected.getColNode(), coveredColumns);
//					}
					System.out.println("Z - running down col:");
					printColumnNodes(columnList);

//					Node tempNode = nodeSelected;
//					for(int i = 0; i < 4; ++i) {
//						System.out.println("\t\t --> " + tempNode.getRowPosition());
//						tempNode = tempNode.getDown();
//					}
					
				}
//				++cell;
//				if(cell >= symbolAmt) {
//					cell = 0;
//				}
//				System.out.println("H - DASFASF");
//				Node tempNode = columnList.get(4).getColNode();
//				
//				for(int i = 0; i < 10; ++i) {
//					System.out.println("\t\t --> " + tempNode.getRowPosition());
//					System.out.println("colNode hash: " + tempNode.getColNode().hashCode());
//					System.out.println("hash:  " + tempNode.hashCode());
//					System.out.println(" class below: " + tempNode.getClass());
//					tempNode = tempNode.getDown();
//					System.out.println(".");
//				}
			}
			//	Early stop, we don't want to move out of the cell constraint column offset.
//			System.out.println("EARLY STOP: " + columnEarlyStop + " colCount: " + colCount);
			if(colCount == columnEarlyStop-1) {
				break;
			}
//			System.exit(0);
//			System.out.println("/");
//			cover(nodeSelected, tempRowNode, toNotTouch, coveredColumns, grid);

		}
//		coveredColumnsTest = coveredColumns;
		for(int i = 0; i < coveredColumns.size(); i++) {
			System.out.println("covered cols: " + coveredColumns.get(i).getColumnID());
		}
		
//		System.exit(0);
		System.out.println(" ENDING CREATION");
//		System.exit(0);

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
		ColumnNode leastOnesColumn = columnList.get(MASTER_NODE_INDEX);
		ColumnNode iterNode = columnList.get(MASTER_NODE_INDEX);
		ColumnNode lastSelection = columnList.get(MASTER_NODE_INDEX);
		ColumnNode currSelection = null;
		
		//	Need to start off at the offset as we don't want to include the master node in the loop.

		for(int col = COL_LIST_OFFSET; col < columnList.size(); ++col) {
			iterNode = iterNode.getRight();
			if(iterNode.getNumberOfOnes() < leastOnesCount && iterNode.getNumberOfOnes() > 0) {
				leastOnesColumn = iterNode;
				leastOnesCount = iterNode.getNumberOfOnes();
				currSelection = iterNode;
				System.out.println("going to: " + leastOnesColumn.getColumnID() + " #1s: " + leastOnesCount);
//				break;
			}
			System.out.println(iterNode.getColumnID());
		}
//		if(lastSelection != null && lastSelection.getColumnID() == leastOnesColumn.getColumnID()) {
//			System.out.println("alredy shoces");
//			System.exit(0);
//		}

		//	If leastOnesColumn is still null, it means that this is the first iteration. 
		//	and the first column should be selected, as it is the first column with the least amount of ones.
		if(leastOnesColumn.equals(columnList.get(MASTER_NODE_INDEX))) {
			leastOnesColumn = columnList.get(COL_LIST_OFFSET);
			leastOnesCount = leastOnesColumn.getNumberOfOnes();
			System.out.println("GOT HERE");
		}
//		iterNode = columnList.get(MASTER_NODE_INDEX);
//		for()
		
		if(columnList.get(MASTER_NODE_INDEX).getRight().equals(columnList.get(MASTER_NODE_INDEX))) {
			System.out.println(".");
			System.out.println("A - RETURNING TRUE");
			return true;	//	Return true grid is complete.
		}
//		boolean isAllColumnsCovered
//		iterNode = columnList.get(MASTER_NODE_INDEX);
//		
//		if()
		boolean isValid = false;
		Node nodeSelected = leastOnesColumn;
		Node toNotTouch = leastOnesColumn;
		for(int cellSelection = 0; cellSelection < leastOnesCount; ++cellSelection) {
			//	NOTES: Can probably create a for leep ( number of ones the leastOnesCol has) thus iterating each symbol.
			//	Select first node of the column and hold references.
//			Node nodeSelected = leastOnesColumn.getList().get(cellSelection);
//			Node toNotTouch = leastOnesColumn.getList().get(cellSelection);
			nodeSelected = nodeSelected.getDown();
			toNotTouch = toNotTouch.getDown();
			
			System.out.println(" selected col: " + leastOnesColumn.getColumnID() + " row: " + nodeSelected.getRowPosition() + " # " + leastOnesColumn.getNumberOfOnes());

//			Get the column and row.
			int column = nodeSelected.getColNode().getColumnID();
			int row = nodeSelected.getRowPosition();

			//	Convert the column and row numbers into usable numbers within the sudoku grid.
			int getRealRow = (int) Math.floor(row/(gridSize*symbolAmt));
			int getRealCol = column%gridSize;
			int cell = row%gridSize;
			System.out.println("cell: " + row);
			System.out.println(" \n\n \t\t TRYING row: " + getRealRow + " col: " + getRealCol + " cell: " + cell);


			
//			System.exit(0);

			System.out.println("GETTING X: " + getRealRow + " Y: " + getRealCol);
			//	Get the columnID. Used to ensure unlinking of 'up' and 'down' references don't happen on this node in the row.
	
			//	Iterate through the nodes to the right until we loop back to the nodeSelected.
			Node tempRowNode = nodeSelected;
			System.out.println("======STARTING TO COVER======");
			cover(nodeSelected, tempRowNode, toNotTouch, coveredColumns, grid);
			
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t COL " + tempRowNode.getLeft().getColNode().getColumnID() + "CHECKING NUMBERS: " + nodeSelected.getColNode().getNumberOfOnes());
			if(tempRowNode.getColNode().isCovered()) {
				System.out.println("isCovered Removing");
				coverColumnNode(tempRowNode.getColNode(), coveredColumns);
			}
			//	Set the last selection
				
			Integer cellToSet = null;
	    	cellToSet = grid.getSymbols()[cell];
	    	if(grid.getCell(getRealRow, getRealCol) == null) {
		    	grid.setCell(getRealRow, getRealCol, cellToSet);
		    	System.out.println("\t\t\t\t\t\t colID: " + iterNode.getColumnID());
				System.out.println(grid.toString());
				System.out.println("acvav a");
				
				if(allCovered(columnList)) {
					return true;
				}


	    	}

//	    	boolean isAllCovered = checkCover(columnList, coveredColumns, grid);
//			if(isAllCovered || grid.validate()) {
//				return true;
//			}
//	    	ColumnNode temp = columnList.get(0);
//	    	for(int col = COL_LIST_OFFSET; col < columnList.size(); ++col) {
//	    		temp = temp.getRight();
//	    		System.out.println(temp.getColumnID());
//	    		
//	    	}
			System.out.println(" COVERED COL SIZE: " + coveredColumns.size());
//			for(int i=0; i < coveredColumns.size(); ++i) {
//				System.out.println("removed cols: " + coveredColumns.get(i).getColumnID());
//			}
//			printColumnNodes(columnList);
//			System.out.println("A - covered columns #: " + coveredColumns.size());
//			uncover(nodeSelected, tempRowNode, toNotTouch, leastOnesColumn, coveredColumns, grid);
//			System.out.println("B - covered columns #: " + coveredColumns.size());

			printColumnNodes(columnList);
//			System.exit(0);
			System.out.println("RECURRING MF IOEMCIQWME ML");

			boolean placedSuccessfully = recursiveSolve(columnList, grid);

			lastSelection = leastOnesColumn;
			isValid = placedSuccessfully;
			if(!placedSuccessfully) {
				//	Uncover
				
				uncover(nodeSelected, tempRowNode, toNotTouch, leastOnesColumn, coveredColumns, grid);

				//	remove partial answer from grid.
				grid.setCell(getRealRow, getRealCol, null);
				if(toNotTouch.getDown().equals(toNotTouch.getColNode())) {
					System.out.println("B - RETURNING FALSE");

					return false;
				}
			}
			if(placedSuccessfully) {
				System.out.println("C - RETURNING TRUE");
				return true;
			}
			if(toNotTouch.getDown().equals(toNotTouch.getColNode())) {
				System.out.println("D - RETURNING FALSE");

				return false;
			}

//			boolean isAllCovered = checkCover(columnList, coveredColumns, grid);
//			if(isAllCovered) {
//				return true;
//			}

		}
		
		
		printColumnNodes(columnList);
		System.out.println("coveredColumn count: " + coveredColumns.size());
		return isValid;
	}
    
	private void cover(Node nodeSelected, Node tempRowNode, Node toNotTouch, 
			LinkedList<ColumnNode> coveredColumns, SudokuGrid grid) {
//		############ START COVER #############

			//	Cover other columns and rows
			System.out.println("covering: " + nodeSelected.getColNode().getColumnID());
			System.out.println("starting at colid: " + nodeSelected.getColNode().getColumnID());
//			coverColumnNode(tempRowNode.getColNode(), coveredColumns);
			System.out.println("\n\n \t\t\t\t - DEBUG #1 - COL: " + nodeSelected.getColNode().getColumnID() + " ROW: " + nodeSelected.getRowPosition());
			Node temp = nodeSelected;

//			if(!coveredColumns.contains(nodeSelected)) {
			coverColumnNode(nodeSelected.getColNode(), coveredColumns);
			coveredColumns.addFirst(nodeSelected.getColNode());
//			}
			// DEBUG
			if(coveredColumns.contains(tempRowNode.getColNode())) {
				System.out.println("\n\\n \t\t\t\t - AA - COL: " + tempRowNode.getColNode().getColumnID() + " has successfuly been removed and added to the list");
			}
			Node coverOther = tempRowNode;
			for(int i = 0; i < symbolAmt; i++ ) {
				coverOther = coverOther.getUp();

				if(coverOther.equals(coverOther.getColNode())) {
					coverOther = coverOther.getUp();
				}
//				System.out.println("col: " + coverOther.getColNode().getColumnID() + " row: " + coverOther.getRowPosition());

				if(coverOther.equals(tempRowNode)) {
//					System.out.println("A - col: " + coverOther.getColNode().getColumnID() + " row: " + coverOther.getRowPosition());
//					System.out.println("A - HIT SAME NODE");
					break;
				}
				Node iterNode = coverOther;
				for(int j = 0; j < symbolAmt; ++j) {
					iterNode = iterNode.getRight();
					if(iterNode.equals(coverOther)) {
//						System.out.println("C - col: " + coverOther.getColNode().getColumnID() + " row: " + coverOther.getRowPosition());
//						System.out.println("C - HIT SAME NODE");
						break;
					}
					coverNodeInRow(iterNode);
//					coverColumnNode(iterNode.getColNode(), coveredColumns);
//					coveredColumns.addFirst(iterNode.getColNode());
					System.out.println("B - col: " + iterNode.getColNode().getColumnID() + " row: " + iterNode.getRowPosition());

				}

			}
//			System.exit(0);
			if(!coveredColumns.contains(nodeSelected)) {
				coverColumnNode(nodeSelected.getColNode(), coveredColumns);
				coveredColumns.addFirst(nodeSelected.getColNode());
				}
			for(int i = 0; i < symbolAmt; ++i) {

				tempRowNode = tempRowNode.getRight();

				if(!nodeSelected.equals(tempRowNode)) {
					
					System.out.println("\n\n \t\t\t\t - DEBUG - COL: " + tempRowNode.getColNode().getColumnID());

					
					// DEBUG
					if(coveredColumns.contains(tempRowNode.getColNode())) {
						System.out.println("\n\n \t\t\t\t - ZZ - COL: " + tempRowNode.getColNode().getColumnID() + " has successfuly been removed and added to the list");
					}
					System.out.println("aaa: " + nodeSelected.getRowPosition());
					Node tempColNode = tempRowNode;
					System.out.println("B - covering column: " + tempRowNode.getColNode().getColumnID());
					coveredColumns.addFirst(tempRowNode.getColNode());
					coverColumnNode(tempRowNode.getColNode(), coveredColumns);
					System.out.println("AS: " + tempRowNode.getColNode().getNumberOfOnes());
					for(int j = 0; j < tempRowNode.getColNode().getNumberOfOnes(); ++j) {
						//	Make sure we set the tempColNode to another node in the list and NOT the columnNode itself.
						if(!tempColNode.getUp().equals(tempColNode.getColNode())){
							tempColNode = tempColNode.getUp();
						}else {
							//	Skip over the columnNode.
							tempColNode = tempColNode.getColNode().getUp();
							System.out.println("-A- covering node: " + tempColNode.getRowPosition());

						}

							//	Cover the row Nodes
							Node tempColRowNode = tempColNode;
							for(int l = 1; l < symbolAmt; ++l) {
								tempColRowNode = tempColRowNode.getRight();
								if(!toNotTouch.equals(tempColRowNode)) {
									System.out.println("F - covering node col: " + 
											tempColRowNode.getColNode().getColumnID() + " node row: " + tempColRowNode.getRowPosition());
									coverNodeInRow(tempColRowNode);
									ColumnNode a = tempColRowNode.getColNode();
									if(tempColRowNode.getColNode().isCovered()) {
										coverColumnNode(tempColRowNode.getColNode(), coveredColumns);
										coveredColumns.addFirst(nodeSelected.getColNode());

									}
								}
							}
					}
					
					//	Need to iterate through row
				}
				System.out.println("C - covering col: " + tempRowNode.getColNode().getColumnID() + " row: " + tempRowNode.getRowPosition());

			}
			System.out.println(" {}{}}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{");
	}
	
	
	private void uncover(Node nodeSelected, Node tempRowNode, Node toNotTouch, 
			ColumnNode leastOnesColumn, LinkedList<ColumnNode> coveredColumns, SudokuGrid grid) {
		for(int numCols = 0; numCols < coveredColumns.size(); ++numCols) {
			nodeSelected = coveredColumns.get(numCols);
//			nodeSelected = coveredColumns.get(coveredColumns.lastIndexOf(coveredColumns.get(numCols)));

			uncoverColumnNode(nodeSelected.getColNode());

			if(!leastOnesColumn.equals(coveredColumns.get(numCols))) {

			tempRowNode = nodeSelected;
			System.out.println("x - " + nodeSelected.getRowPosition());
			System.out.println("starting at colid: " + nodeSelected.getColNode().getColumnID());
			
			Node tempColNode = tempRowNode;

			for(int i = 0; i < tempColNode.getColNode().getNumberOfOnes(); ++i) {
				if(!tempColNode.getDown().equals(tempColNode.getColNode())){
					tempColNode = tempColNode.getDown();
				}else {
					//	Skip over the columnNode.
					tempColNode = tempColNode.getColNode().getDown();
				}
//				if(!tempRowNode.equals(tempColNode)) {

				Node tempColRowNode = tempColNode;

				for(int l = 1; l < symbolAmt; ++l) {
					tempColRowNode = tempColRowNode.getLeft();
					if(!toNotTouch.equals(tempColRowNode)) {

					System.out.println("A - uncovering node col: " + 
					tempColRowNode.getColNode().getColumnID() + " node row: " + tempColRowNode.getRowPosition());
//					tempColRowNode.getColNode().setNumberOfOnes(tempColRowNode.getColNode().getNumberOfOnes() + 1);

					uncoverNodeInRow(tempColRowNode);
					}
				}
				}
					
				System.out.println("C - uncovering col: " + tempRowNode.getColNode().getColumnID() + " row: " + tempRowNode.getRowPosition());
	
//			}
		}
		}
		//	Remove all ColumnNodes in the selection so that the same ColumnNode does not get uncovered again.
		coveredColumns.removeAll(coveredColumns);
//		coveredColumns = new LinkedList<ColumnNode>();
	}

	
	//	Covers and removes the 'up' and 'down' links to this Node in the columnNode linked list.
	private void coverNodeInRow(Node nodeToCover) {
		if(!nodeToCover.getUp().getDown().equals(nodeToCover)) {
			System.out.println(" !!!!!!! ALREADY COVERED!");
			return;
		}
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
	private void coverColumnNode(ColumnNode columnNodeToCover, LinkedList<ColumnNode> coveredColumns) {
//		if(coveredColumns.contains(columnNodeToCover)) {
//			return;
//		}
//		System.out.println("left node id: " + columnNodeToCover.getLeft().getColumnID());
//		System.out.println("right node id: " + columnNodeToCover.getRight().getColumnID());
//
//		System.out.println("\t\t\t\t\t\t AAAAAA : " + columnNodeToCover.getColumnID());
		System.out.println("\n\n \t\t\t\t - AZZ - COVERING COL: " + columnNodeToCover.getColumnID());

		columnNodeToCover.getLeft().setRight(columnNodeToCover.getRight());
		columnNodeToCover.getRight().setLeft(columnNodeToCover.getLeft());
//		System.out.println("A - left node id: " + columnNodeToCover.getLeft().getColumnID());
//		System.out.println("A - right node id: " + columnNodeToCover.getRight().getColumnID());
//		System.out.println("B - left node id: " + columnNodeToCover.getLeft().getRight().getColumnID());
//		System.out.println("B - right node id: " + columnNodeToCover.getRight().getLeft().getColumnID());
//		ColumnNode temp = columnNodeToCover.getRight();
//    	for(int col = COL_LIST_OFFSET; col < 10; ++col) {
//    		temp = temp.getRight();
//    		System.out.println(temp.getColumnID());
//    		
//    	}

	}
	
	//	Uncovers and resets the 'left' and 'right' links to this columnNode in the columnList.
	private void uncoverColumnNode(ColumnNode columnNodeToUncover) {
		System.out.println("\n\n \t\t\t\t - B - UNCOVERING COL: " + columnNodeToUncover.getColumnID());
		columnNodeToUncover.getLeft().setRight(columnNodeToUncover);
		columnNodeToUncover.getRight().setLeft(columnNodeToUncover);
	}
	
	
	private boolean checkCover(LinkedList<ColumnNode> columnList, LinkedList<ColumnNode> coveredColumns, SudokuGrid grid) {
		boolean allCovered = true;
		if(!columnList.isEmpty()) {
	    	ColumnNode itterNode = columnList.getFirst().getRight();
	    	for(int col = 0; col <= totalNumCols; ++col) {
    			itterNode = itterNode.getRight();

		    		System.out.println("\n\tColumn ID: " + itterNode.getColumnID());
		    		System.out.println("\t # of 1s: " + itterNode.getNumberOfOnes());
		    		if(itterNode.getNumberOfOnes() != 0) {
		    			allCovered = false;
		    			
		    		}
//		    		if(itterNode.getNumberOfOnes() == 0) {
//		    			coverColumnNode(itterNode, coveredColumns);
//		    			coveredColumns.addFirst(itterNode.getColNode());
//		    		}

	
	    	}
    	}else {
    		System.out.println("ERROR: columnList is empty!");
    	}
		return allCovered;
	}
	
	
    //	DEBUG: prints column IDs and the number of 1s is holds
	//	prints out the columns header ID
    private void printColumnNodes(LinkedList<ColumnNode> columnList) {
    	//prints out the columns header ID
    	if(!columnList.isEmpty()) {
	    	ColumnNode itterNode = columnList.getFirst();
	    	for(int col = 0; col <= totalNumCols; ++col) {
		    		System.out.println("\n\tColumn ID: " + itterNode.getColumnID());
		    		System.out.println("\t # of 1s: " + itterNode.getNumberOfOnes());
		    		if(itterNode.getDown() != null)
		    		System.out.println("row: " + itterNode.getDown().getRowPosition());
		    		itterNode = itterNode.getRight();
	
	    	}
    	}else {
    		System.out.println("ERROR: columnList is empty!");
    	}
    }
    
    
    
    private boolean allCovered(LinkedList<ColumnNode> columnList) {
		ColumnNode tempIterNode = columnList.get(MASTER_NODE_INDEX);
		boolean isAllCovered = true;
		for(int i = 0; i < columnList.size(); i++) {
			tempIterNode = tempIterNode.getRight();
			if(tempIterNode.getNumberOfOnes() != 0) {
				isAllCovered = false;
			}
				
		}
		return isAllCovered;
    }
} // end of class DancingLinksSolver




