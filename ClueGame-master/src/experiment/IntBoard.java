package experiment;

import java.util.*;

/**
 * IntBoard - holds most of the functionality for the board
 * @author dquintana, sethasadi
 *
 */
public class IntBoard {
	//private attributes for the board
	private BoardCell[][] grid = new BoardCell[4][4];
	private Map<BoardCell, Set<BoardCell>> adjCells;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	/**
	 * default constructor that sets the adjacent cells map, targets set, and visited set. it also
	 * adds a BoardCell to each location on the board
	 */
	public IntBoard() {
		adjCells = new HashMap<>();
		targets = new HashSet<>();
		visited = new HashSet<>();
		
		//iterate over entire board
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				//add a BoardCell to each location in the grid
				grid[i][j] = new BoardCell(i, j);
			}
		}
		calcAdjacencies();
	}
	
	/**
	 * calcAdjacencies - stores adjacent cells of each board cell
	 * @return
	 */
	public void calcAdjacencies() {
		// iterate over entire board
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				//initialize a set to store the adjacent cells
				Set<BoardCell> nearbyCells = new HashSet<BoardCell>();
				
				//create 4 if statements to add all the possible adjacent cells if they exist
				if (grid[i][j].getBoardRow() - 1 >= 0) {
					nearbyCells.add(grid[i - 1][j]);
				}
				if (grid[i][j].getBoardRow() + 1 <= 3) {
					nearbyCells.add(grid[i + 1][j]);
				}
				if (grid[i][j].getBoardCol() - 1 >= 0) {
					nearbyCells.add(grid[i][j - 1]);
				}
				if (grid[i][j].getBoardCol() + 1 <= 3) {
					nearbyCells.add(grid[i][j + 1]);
				}
				
				//add an entry to the adjCells Map with grid[i][j] and the formed set
				adjCells.put(grid[i][j], nearbyCells);
			}
		}
		return;
	}
	
	/**
	 * calcTargets - finds the targets of a board cell to see whether it can be moved to or not
	 * @param startCell
	 * @param pathLength
	 * @return
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		// calculate possible targets
		Set<BoardCell> adjs = adjCells.get(startCell);
		visited.add(startCell);
		for (BoardCell cell : adjs) {
			if (visited.contains(cell)) {
				continue;
			}
			else {
				if (pathLength == 1) {
					targets.add(cell);
				}
				else {
					calcTargets(cell, pathLength - 1);
				}
			}
		}
		visited.remove(startCell);
		System.out.println(visited.size());
		return;
	}
	
	public Set<BoardCell> getAdjList(BoardCell cell) {
		return adjCells.get(cell);
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
