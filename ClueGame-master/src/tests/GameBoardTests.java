package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

/**
 * GameBoardTests - JUnit tests for Board to make sure the board loads correctly from the given files
 * @author sethasadi, dquintana
 *
 */
public class GameBoardTests {
	//values relating to proper readings of my configuration files
	//constant so that they will not be accidentally changed
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLS = 30;

	//static Board because it should be setup beforehand and only one should exist
	private static Board gameboard;

	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		gameboard = Board.getInstance();
		// set the file names to use my config files
		gameboard.setConfigFiles("Rooms.csv", "rooms.txt");		
		// Initialize will load BOTH config files 
		gameboard.initialize();
	}

	@Test
	public void testRooms() {
		//Get the map legend representing the rooms
		Map<Character, String> legend = gameboard.getLegend();
		
		//Ensure the correct number of rooms are read in
		assertEquals(LEGEND_SIZE, legend.size());
		
		//Test a few rooms to ensure the load worked, specifically testing the first and last ones
		assertEquals("Theater Room", legend.get('T'));
		assertEquals("Tech Lair", legend.get('E'));
		assertEquals("VR Room", legend.get('V'));
		assertEquals("Dining Hall", legend.get('D'));
		assertEquals("Walkway", legend.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		//Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, gameboard.getNumRows());
		assertEquals(NUM_COLS, gameboard.getNumColumns());		
	}

	//Test a doorway in each direction (RIGHT/LEFT/UP/DOWN) and two cells that are not a doorway.
	@Test
	public void FourDoorDirections() {
		//test for a door opening from the right
		BoardCell testRoom = gameboard.getCellAt(0, 5);
		assertTrue(testRoom.isDoorway());
		assertEquals(DoorDirection.RIGHT, testRoom.getDoorDirection());
		
		//test for a door opening from the left
		testRoom = gameboard.getCellAt(18, 10);
		System.out.println(testRoom.getDoorDirection());
		assertTrue(testRoom.isDoorway());
		assertEquals(DoorDirection.LEFT, testRoom.getDoorDirection());
		
		//test for a door opening from above
		testRoom = gameboard.getCellAt(8, 22);
		assertTrue(testRoom.isDoorway());
		assertEquals(DoorDirection.UP, testRoom.getDoorDirection());
		
		//test for a door opening from below
		testRoom = gameboard.getCellAt(5, 11);
		assertTrue(testRoom.isDoorway());
		assertEquals(DoorDirection.DOWN, testRoom.getDoorDirection());
		
		//test that non-door cells represent that they are not doors
		testRoom = gameboard.getCellAt(5, 13);
		assertFalse(testRoom.isDoorway());
		
		// Test that walkways are not doors
		BoardCell cell = gameboard.getCellAt(11, 19);
		assertFalse(cell.isDoorway());		
	}
	
	//Test that every door is detected by checking door total is correct
	@Test
	public void testDoorwaysTotal() {
		int numDoors = 0;
		for (int i = 0; i < gameboard.getNumRows(); i++)
			for (int j = 0; j < gameboard.getNumColumns(); j++) {
				BoardCell testCell = gameboard.getCellAt(i, j);
				if (testCell.isDoorway()) {
					numDoors++;
				}
			}
		Assert.assertEquals(29, numDoors);
	}

	//Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		//Test cells in rooms (perimeters, wall borders, cells in the center, and cells next to doorways
		assertEquals('M', gameboard.getCellAt(4, 12).getInitial());
		assertEquals('R', gameboard.getCellAt(21, 4).getInitial());
		assertEquals('V', gameboard.getCellAt(18, 29).getInitial());
		assertEquals('T', gameboard.getCellAt(10, 21).getInitial());

		//Test a walkway
		assertEquals('W', gameboard.getCellAt(5, 27).getInitial());
		
		//Test the closet
		assertEquals('C', gameboard.getCellAt(10, 16).getInitial());
	}
}
