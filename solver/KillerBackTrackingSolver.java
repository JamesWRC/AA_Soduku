/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.Cage;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
	private int gameSize = 0;
	int[][] boxNumbers;
    public KillerBackTrackingSolver() {
    } // end of KillerBackTrackingSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	Cage[] cages = new Cage[grid.getCages().length];
		System.arraycopy(grid.getCages(), 0, cages, 0, cages.length);		
		
    	//	boxID is the index of the bottom left cell of the boxes in the game
    	gameSize = (int) Math.sqrt(grid.getSize());

    	
 	    
    	int cageIndex = 0;
    	int cageCoordinatesIndex = 0;
    	int cagesSatisfied = 0;
    	int count = 0;
    	/*	gameSize is helping the get the bottom left cell of the cells in each box
    		which is the square root of the game length, as the whole grid is made up
    		of smaller squares (box).
    	*/	

    	
    	recursiveBacktrack(grid, cageIndex, cageCoordinatesIndex, cages, count, cagesSatisfied);
    	

    	
        return grid.validate();
       
    } // end of solve()

    
   
    
    public boolean recursiveBacktrack(SudokuGrid grid, int cageIndex, int cageCoordinatesIndex, Cage[] cages, int count, int cagesSatisfied) {
    	Boolean isValid = false;
    	Integer[] symbols = grid.getSymbols();

		/*
		 * PSEUDOCODE
		 * 	-	SELECT cage at index [x]								done
		 * 	-	SELECT cage coordinates at index [z]					done
		 * 		- FOR EACH symbol										done
		 * 			-	place a cell in coordinates						done
		 * 			-	IF valid placement in grid						done
		 * 				- 	IF cage is filled							done
		 * 					-	IF cage is valid						done
		 * 						- SET cage coordinates [z] = 0			done
		 * 						- SET cage index [x] +=1				done
		 * 					- 	ELSE									done
		 * 						- RETURN false;							done
		 * 					-	END IF									done
		 * 				-	ELSE										done
		 *  				-	SET cage coordinates [z] +=1			done
		 * 				-	END IF										done
		 * 						- 	---> Recursion <---					done
		 * 			
		 * 			-	ELSE
		 * 				- UNSET symbol in grid							done
		 * 			-	END IF
		 */

		Cage chosenCage = cages[cageIndex];
		Integer[][] chosenCageCoordinates = chosenCage.getCells();
		

		int x = chosenCageCoordinates[cageCoordinatesIndex][0];
		int y = chosenCageCoordinates[cageCoordinatesIndex][1];

		int boxIDx = x;
		int boxIDy = y;
		Integer chosenSymbol = null;
		
		//Copy array
		Integer[] tempSymbols = new Integer[symbols.length];
		System.arraycopy(grid.getSymbols(), 0, tempSymbols, 0, grid.getSymbols().length);
		//	See what is already used in the columns
		//	Search row.

		// 	Check what is already in the row
		for(int k=0; k < grid.getSize(); ++k) {
			if(grid.getCell(k, y) == tempSymbols[k]) {
				tempSymbols[k] = null;
			}
		}

		symbols = tempSymbols;
		
		//	Try a 'valid' symbol
		for(int i = 0; i < symbols.length; ++i) {
			if(symbols[i] != null) {
				chosenSymbol = symbols[i];
			}else {
				continue;
			}		
			//	Set cell
			grid.setCell(x, y, chosenSymbol);



			// veryify cell placement, row, col, and box
			if(grid.verifyCell(x, y, boxIDx, boxIDy, chosenSymbol)) {

				if(!placemnetLessThanCageValue(chosenCage, grid)){
					grid.setCell(x, y, null);

					continue;
				}else {
					
				cageCoordinatesIndex+=1;
				}
				//	Check if the cage is full to then be further tested.
				if(isCageFull(chosenCage, grid)) {
					
					//	Validate the cage, if all filled, uinque numbers and sum up the required sum.
					if(grid.validateCage(chosenCage)) {
						
						cageIndex+=1;
						if(cageIndex == cages.length && cageCoordinatesIndex == chosenCage.getCells().length) {
							return true;
						}

						cageCoordinatesIndex = 0;
						cagesSatisfied+=1;
					}else {
						//	Cage is not valid return false;
						grid.setCell(x, y, null);
						continue;
					}
				}else {
					
				}
				// Recursively go down another step.
				boolean placementOk = true;
				placementOk = recursiveBacktrack(grid, cageIndex, cageCoordinatesIndex, cages, count+1, cagesSatisfied);
				//	Check if the the placement is ok or not
				if(placementOk) {
					return true;
				}
				if(!placementOk) {
					grid.setCell(x, y, null);
					cageCoordinatesIndex-=1;
				}
				if(i == symbols.length -1) {
					grid.setCell(x, y, null);
					cageCoordinatesIndex-=1;

					return false;
				}
			}else {
				grid.setCell(x, y, null);
				
			}
			if(i == symbols.length -1) {
				grid.setCell(x, y, null);
				cageCoordinatesIndex-=1;

				return false;
			}
		}
		
		
		
		
		
		
		
		return isValid;
    }

    private boolean placemnetLessThanCageValue(Cage cage, SudokuGrid grid) {
    	boolean isFull = true;
		Integer[][] cageCoordinates = cage.getCells();
		int cageX = 0;
		int cageY = 0;
		int sum = 0;
		//	Check if all cells in cage has been filled in yet.
		
		for(int i = 0; i < cageCoordinates.length; ++i) {
			cageX = cageCoordinates[i][0];
			cageY = cageCoordinates[i][1];
			if(grid.getCell(cageX, cageY) != null) {
				sum+=grid.getCell(cageX, cageY);
				if(sum > cage.getCageSum()) {
					isFull = false;
				}
			}			
		}
		return isFull;
    }
    
    
    private boolean isCageFull(Cage cage, SudokuGrid grid) {
    	boolean isFull = true;
		Integer[][] cageCoordinates = cage.getCells();
		int cageX = 0;
		int cageY = 0;
		//	Check if all cells in cage has been filled in yet.
		for(int i = 0; i < cageCoordinates.length; ++i) {
			cageX = cageCoordinates[i][0];
			cageY = cageCoordinates[i][1];
			if(grid.getCell(cageX, cageY) == null) {
				// Return true for now as the other cells in the grid are yet to be set.
				isFull = false;
			}
		}
		return isFull;
    }
    
    
} // end of class KillerBackTrackingSolver()
