package grid;
import java.util.ArrayList;

public class Cage {
	/*	define -1 as an invalid number
	 * 	since a cage could be a single cell that could be 0
	 */
	private int cageSum = 0;
	//holds array of cells that makes up the cage
	private Integer[][] cells;
	
	public Cage(int cageSum, int numOfCellsInCage) {
		this.cageSum = cageSum;
		cells = new Integer[numOfCellsInCage][2];
	}
	
	public void addCellsToCage(Integer[][] cells) {
		this.cells = cells;
	}
	
	public int getCageSum() {
		//return the amount the cell should sum to
		return cageSum;
	}
	
	public Integer[][] getCells(){
		//return the arraylist of cells.
		return cells;
	}
	
	public void print() {
		String retString = "";
		for(int i = 0; i< cells.length; ++i) {
			retString+=" " + cells[i][0] + "," + cells[i][1];
		}
		System.out.println("[+] DEBUG: cageSum " + cageSum + " coords: " + retString);
	}
	
}
