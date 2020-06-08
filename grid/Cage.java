package grid;

public class Cage {
	//	The distance from 0,0 in the grid. Used to sort cages in order of proximity starting at 0,0
	private int distance = 0;
	//	define 0 as an invalid number
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
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public void print() {
		String retString = "";
		for(int i = 0; i< cells.length; ++i) {
			retString+=" " + cells[i][0] + "," + cells[i][1];
		}
		System.out.println("[+] DEBUG: cageSum " + cageSum + " coords: " + retString);
	}
	
}
