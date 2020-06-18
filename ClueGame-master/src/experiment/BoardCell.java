package experiment;

/**
 * BoardCell - class to represent a single cell and simply keep its location
 * @author sethasadi, dquintana
 *
 */
public class BoardCell {
	private int boardRow;
	private int boardCol;
	
	/**
	 * constructor that sets the cell's row and column
	 * @param row
	 * @param col
	 */
	public BoardCell(int row, int col) {
		boardRow = row;
		boardCol = col;
	}

	public int getBoardRow() {
		return boardRow;
	}

	public int getBoardCol() {
		return boardCol;
	}
	
	
}
