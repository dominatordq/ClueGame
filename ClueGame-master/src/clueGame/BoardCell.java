package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * BoardCell - class to represent a single cell and simply keep its location
 * @author sethasadi, dquintana
 *
 */
public class BoardCell {
	private int boardRow;
	private int boardCol;
	private char initial;
	private DoorDirection direction;
	private final static int DRAW_SIZE = 25;  
	private final static int DOOR_WIDTH = 4;
	private Rectangle2D cell;
	private boolean titleCell;
	private boolean personCell;
	private int personHolderNum;
	
	private boolean targetHighlight;
	
	private Board gameboard;
	
	/**
	 * constructor that sets the board row, column, character, and direction
	 * @param row
	 * @param col
	 * @param init
	 */
	public BoardCell(int row, int col, char init) {
		boardRow = row;
		boardCol = col;
		initial = init;
		direction = DoorDirection.NONE;
		titleCell = false;
		gameboard = Board.getInstance();
		targetHighlight = false;
		personCell = false;
		personHolderNum = 0;
	}

	public int getBoardRow() {
		return boardRow;
	}

	public int getBoardCol() {
		return boardCol;
	}
	
	public void setToTitleCell() {
		titleCell = true;
	}
	
	/**
	 * checks to see if a cell is a walkway on the board
	 * @return true or false
	 */
	public boolean isWalkway() {
		//return true if cell is a walkway
		if (initial == 'W') {
			return true;
		}
		return false;
	}
	
	/**
	 * checks to see if a cell is a room on the board
	 * @return true or false
	 */
	public boolean isRoom() {
		//return if cell is a room
		if (this.isWalkway()) {
			return false;
		}
		return true;
	}
	
	/**
	 * checks to see if a cell is a doorway
	 * @return true or false
	 */
	public boolean isDoorway() {
		//return if cell is a doorway
		if (direction == DoorDirection.NONE) {
			return false;
		}
		return true;
	}
	
	public boolean doesHoldPerson() {
		return personCell;
	}
	
	public int getPersonHolderNum() {
		return personHolderNum;
	}
	
	public void setPersonHolderNum(int num) {
		personHolderNum = num;
	}
	
	public DoorDirection getDoorDirection() {
		return direction;
	}
	
	public void setDoorDirection(DoorDirection dir) {
		direction = dir;
	}

	public char getInitial() {
		//return the initial
		return initial;
	}

	/**
	 * draw - draw the cell for the set dimensions above and base the color on the type of cell it is
	 * also if this cell is a title cell, print the name of the room
	 * @param g
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		cell = new Rectangle(boardCol*DRAW_SIZE, boardRow*DRAW_SIZE, DRAW_SIZE, DRAW_SIZE);
		
		//draw the room
		if (isRoom()) {
			g2d.setColor(new Color(0, 88, 232));
			
			if (targetHighlight) {
				g2d.setColor(new Color(130, 0, 178));
			}
			
			g2d.fill(cell);
			
			if (boardRow == 4) {
				if (boardCol < 5) {
					Rectangle2D roomSeparator = new Rectangle(boardCol*DRAW_SIZE, boardRow*DRAW_SIZE + DRAW_SIZE - 1, DRAW_SIZE, 1);
					g2d.setColor(new Color(0, 0, 0));
					
					if (targetHighlight) {
						g2d.setColor(new Color(255, 0, 255));
					}
					
					g2d.fill(roomSeparator);
				}
			}
		}
		//draw a cell with a border
		else {
			g2d.setColor(new Color(232, 221, 81));
			
			if (targetHighlight) {
				g2d.setColor(new Color(255, 0, 0));
			}
			
			g2d.fill(cell);
			g2d.setColor(new Color(0, 0, 0));
			g2d.draw(cell);
		}
		
		
		//draw a indicator of the door direction
		if(isDoorway()) {
			switch (direction) {
			case LEFT:
				cell = new Rectangle(boardCol*DRAW_SIZE, boardRow*DRAW_SIZE, DOOR_WIDTH, DRAW_SIZE);
				break;
			case RIGHT:
				cell = new Rectangle(boardCol*DRAW_SIZE - DOOR_WIDTH + DRAW_SIZE, boardRow*DRAW_SIZE, DOOR_WIDTH, DRAW_SIZE);
				break;
			case UP: 
				cell = new Rectangle(boardCol*DRAW_SIZE, boardRow*DRAW_SIZE, DRAW_SIZE, DOOR_WIDTH);
				break;
			case DOWN:
				cell = new Rectangle(boardCol*DRAW_SIZE, boardRow*DRAW_SIZE - DOOR_WIDTH + DRAW_SIZE, DRAW_SIZE, DOOR_WIDTH);
				break; 
			}
			
			g2d.setColor(new Color(35, 255, 50));
			g2d.fill(cell);
		}
		
		//draw the room name
		if (titleCell) {
			//this cell is supposed to display the room's name
			g2d.setColor(new Color(255, 255, 255));
			g2d.setFont(new Font("Lucida",Font.PLAIN,20));
			g2d.drawString(gameboard.getLegend().get(initial), boardCol*DRAW_SIZE + 5, (boardRow - 1)*DRAW_SIZE - 10);
		}
		
		
		
		targetHighlight = false;
		
	}
	
	public void setHighlight() {
		targetHighlight = true;
	}
	
	public void holdAPerson() {
		personCell = true;
	}
}
