package solver;

public class Node {

	private Node up = null;				//	References the Node above this Node.
	private Node down = null;			//	References the Node below this Node.
	private Node left = null;			// 	References the Node to the left of this Node.
	private Node right = null;			//	References the Node to the right of this Node.
	private ColumnNode colNode = null;		//	References the colNode (the 'header' of the column this Node is in).
	private int rowPosition;			// 	The row position in the sudoku grid.
	
	public Node() {
		//	Construct nothing
	}

	
	//	Return the reference to the node above this Node.
	public Node getUp() {
		return up;
	}

	//	Points 'up' to the Node above
	public void setUp(Node up) {
		this.up = up;
	}

	//	Returns the reference to the node below this Node.
	public Node getDown() {
		return down;
	}

	//	Points 'down' to the Node below this Node.
	public void setDown(Node down) {
		this.down = down;
	}

	// 	Returns the reference to the Node left of this Node.
	public Node getLeft() {
		return left;
	}
	
	//	Points 'Left' to the Node left of this Node.
	public void setLeft(Node left) {
		this.left = left;
	}

	// 	Returns the reference to the Node right of this Node.
	public Node getRight() {
		return right;
	}
	//	Points 'right' to the Node right of this Node.
	public void setRight(Node right) {
		this.right = right;
	}
	
	// 	Returns the reference to the colHead Node.
	public ColumnNode getColNode() {
		return this.colNode;
	}
	
	// 	Points 'colNode' to the column header Node.
	public void setColNode(ColumnNode colNode) {
		this.colNode = colNode;
	}
	
	//	Set the row position in the sudoku grid for this Node.
	public void setRowPosition(int rowPosition) {
		this.rowPosition = rowPosition;
	}
	
	//	Return the row position in the sudoku grid.
	public int getRowPosition() {
		return rowPosition;
	}
	
	
}
