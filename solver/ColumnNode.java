package solver;

import java.util.LinkedList;

public class ColumnNode extends Node{
	
//	private Node up = null;									//	References the Node above this Node.
//	private Node down = null;								//	References the Node below this Node.
	private ColumnNode left = null;							// 	References the Node to the left of this Node.
	private ColumnNode right = null;						//	References the Node to the right of this Node.
	
	private LinkedList<Node> list; 							//	References the list this Node hold
	
	private int numberOfOnes = 0;							//	The number of ones the columns has in it.
	private int columnID = -1;								// 	The column number ID (name) this Node is in the cover 'matrix'.
															//	Note: We need to initialize as a invalid id.	
	public ColumnNode() {
		super();
		list = new LinkedList<Node>();
//		list.add(new Node());
	}
	
	//	Returns the number of ones (nodes) in the column.
	public int getNumberOfOnes() {
		return numberOfOnes;
	}

	//	Set the numbers of ones in the column.
	public void setNumberOfOnes(int numberOfOnes) {
		this.numberOfOnes = numberOfOnes;
	}

	// 	Return the columnID (name) of the column.
	public int getColumnID() {
		return columnID;
	}

	//	Set the columnID (name) of the column.
	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}

	// 	Returns the reference to the Node left of this Node.
	public ColumnNode getLeft() {
		return left;
	}
	
	//	Points 'Left' to the Node left of this Node.
	public void setLeft(ColumnNode left) {
		this.left = left;
	}

	// 	Returns the reference to the Node right of this Node.
	public ColumnNode getRight() {
		return right;
	}
	//	Points 'right' to the Node right of this Node.
	public void setRight(ColumnNode right) {
		this.right = right;
	}
	
	//	Return the list of Nodes this holds.
	public LinkedList<Node> getList(){
		return list;
	}
	
	//	Set the list of Nodes this holds.
	public void setList(LinkedList<Node> list){
		this.list = list;
	}
	
	//	Check if the ColumnNode is covered or not.
	public boolean isCovered() {
		boolean retVal = false;
		if(numberOfOnes == 0) {
			retVal = true;
		}
		return retVal;
	}
}
