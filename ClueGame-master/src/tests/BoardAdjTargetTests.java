package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

/**
 * BoardAdjTargetTests - JUnit tests for adjacency and targeting methods for ClueGame
 * @author sethasadi @dquintana
 *
 */
public class BoardAdjTargetTests {

	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		//Board is singleton, get the only instance
		board = Board.getInstance();
		//set the file names to use my config files
		board.setConfigFiles("Rooms.csv", "rooms.txt");		
		//set the file names to use player and card config files
		board.setGameSetupConfigFiles("players.txt", "cards.txt");	
		//Initialize will load ALL config files 
		board.initialize();
		//load the players
		board.loadPlayers();
		//load the cards
		board.loadCards();
		//deal the cards
		board.dealCards();
	}

	//---------------------------------------------------Adjacency Tests---------------------------------------------------//

	//Make sure a player stays in his room (Orange on Rooms Spreadsheet)
	@Test
	public void testAdjInsideARoom() {
		//Test a corner of a room and board
		Set<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());

		//Test one that borders the wall of board and room
		testList = board.getAdjList(0, 23);
		assertEquals(0, testList.size());

		//Test one in center of room
		testList = board.getAdjList(22, 4);
		assertEquals(0, testList.size());

		//Test one at edge of theater
		testList = board.getAdjList(10, 21);
		assertEquals(0, testList.size());
	}

	//Make sure the only space adjacent to a doorway is a single walkway cell (Yellow on Rooms Spreadsheet) and every other doorway's cell
	//Test all directions
	@Test
	public void testAdjExitingARoom() {
		//TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(11, 12);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(11, 13)));
		assertTrue(testList.contains(board.getCellAt(10, 13)));
		assertTrue(testList.contains(board.getCellAt(9, 13)));

		//TEST DOORWAY LEFT 
		testList = board.getAdjList(19, 23);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(19, 22)));
		assertTrue(testList.contains(board.getCellAt(20, 22)));
		assertTrue(testList.contains(board.getCellAt(21, 22)));
		assertTrue(testList.contains(board.getCellAt(22, 22)));

		//TEST DOORWAY DOWN
		testList = board.getAdjList(5, 16);
		assertEquals(12, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 16)));
		assertTrue(testList.contains(board.getCellAt(6, 17)));
		assertTrue(testList.contains(board.getCellAt(6, 15)));
		
		assertTrue(testList.contains(board.getCellAt(6, 10)));
		assertTrue(testList.contains(board.getCellAt(6, 11)));
		assertTrue(testList.contains(board.getCellAt(6, 12)));
		
		assertTrue(testList.contains(board.getCellAt(0, 7)));
		assertTrue(testList.contains(board.getCellAt(1, 7)));
		assertTrue(testList.contains(board.getCellAt(2, 7)));
		
		assertTrue(testList.contains(board.getCellAt(0, 20)));
		assertTrue(testList.contains(board.getCellAt(1, 20)));
		assertTrue(testList.contains(board.getCellAt(2, 20)));

		//TEST DOORWAY UP
		testList = board.getAdjList(18, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(17, 7)));

		//TEST DOORWAY RIGHT, WHERE THERE'S A WALKWAY BELOW
		testList = board.getAdjList(9, 5);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 6)));

	}

	//Test at the entrance to rooms (Dark Red on Rooms Spreadsheet)
	//Test all directions
	@Test
	public void testAdjAtDoorways() {
		//TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(4, 29);
		//System.out.println(testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 29)));
		assertTrue(testList.contains(board.getCellAt(5, 29)));
		assertTrue(testList.contains(board.getCellAt(4, 28))); //Doorway
		assertEquals(3, testList.size());

		//TEST DOORWAY DOWN 
		testList = board.getAdjList(13, 22);
		assertTrue(testList.contains(board.getCellAt(13, 21)));
		assertTrue(testList.contains(board.getCellAt(14, 22)));
		assertTrue(testList.contains(board.getCellAt(12, 22))); //Doorway
		assertEquals(3, testList.size());

		//TEST DOORWAY LEFT 
		testList = board.getAdjList(2, 7);
		assertTrue(testList.contains(board.getCellAt(1, 7)));
		assertTrue(testList.contains(board.getCellAt(3, 7)));
		assertTrue(testList.contains(board.getCellAt(2, 6)));
		assertTrue(testList.contains(board.getCellAt(2, 8))); //Doorway
		assertEquals(4, testList.size());

		//TEST DOORWAY UP 
		testList = board.getAdjList(7, 22);
		assertTrue(testList.contains(board.getCellAt(6, 22)));
		assertTrue(testList.contains(board.getCellAt(7, 21)));
		assertTrue(testList.contains(board.getCellAt(8, 22))); //Doorway
		assertEquals(3, testList.size());
	}


	//Tests a whole bunch of varying walkway cells (Dark Blue on Rooms Spreadsheet)
	@Test
	public void testAdjOfWalkways() {
		//Test on top edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(0, 29);
		assertTrue(testList.contains(board.getCellAt(1, 29)));
		assertEquals(1, testList.size());

		//Test on left edge of board, three walkway pieces
		testList = board.getAdjList(14, 0);
		assertTrue(testList.contains(board.getCellAt(13, 0)));
		assertTrue(testList.contains(board.getCellAt(15, 0)));
		assertTrue(testList.contains(board.getCellAt(14, 1)));
		assertEquals(3, testList.size());

		//Test between two rooms, walkways right and left
		testList = board.getAdjList(5, 27);
		assertTrue(testList.contains(board.getCellAt(5, 28)));
		assertTrue(testList.contains(board.getCellAt(5, 26)));
		assertEquals(2, testList.size());

		//Test surrounded by 4 walkways
		testList = board.getAdjList(13, 15);
		assertTrue(testList.contains(board.getCellAt(12, 15)));
		assertTrue(testList.contains(board.getCellAt(14, 15)));
		assertTrue(testList.contains(board.getCellAt(13, 14)));
		assertTrue(testList.contains(board.getCellAt(13, 16)));
		assertEquals(4, testList.size());

		//Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(24, 20);
		assertTrue(testList.contains(board.getCellAt(23, 20)));
		assertTrue(testList.contains(board.getCellAt(24, 21)));
		assertEquals(2, testList.size());

		//Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(15, 29);
		assertTrue(testList.contains(board.getCellAt(15, 28)));
		assertTrue(testList.contains(board.getCellAt(16, 29)));
		assertEquals(2, testList.size());

		//Test on walkway next to  door that is not in the needed
		//direction to enter
		testList = board.getAdjList(10, 5);
		assertTrue(testList.contains(board.getCellAt(10, 4)));
		assertTrue(testList.contains(board.getCellAt(10, 6)));
		assertTrue(testList.contains(board.getCellAt(11, 5)));
		assertEquals(3, testList.size());
	}

	//---------------------------------------------------Targets Tests---------------------------------------------------//

	//Tests of walkway targets, 1 step
	//First walkway is next to a room and the edge of the board
	//Second walkway is in the middle of the map
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(16, 29, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(15, 29)));
		assertTrue(targets.contains(board.getCellAt(16, 28)));	

		board.calcTargets(9, 19, 1);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(9, 20)));
		assertTrue(targets.contains(board.getCellAt(9, 18)));		
		assertTrue(targets.contains(board.getCellAt(8, 19)));		
	}

	//Tests of walkway targets, 2 steps
	//First walkway is next to a room and the edge of the board
	//Second walkway is in the middle of the map
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(16, 29, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(15, 28)));
		assertTrue(targets.contains(board.getCellAt(16, 27)));

		board.calcTargets(9, 19, 2);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(8, 18)));
		assertTrue(targets.contains(board.getCellAt(8, 20)));	
		assertTrue(targets.contains(board.getCellAt(10, 18)));
		assertTrue(targets.contains(board.getCellAt(10, 20)));
		assertTrue(targets.contains(board.getCellAt(7, 19)));	
		assertTrue(targets.contains(board.getCellAt(11, 19)));	
	}

	//Tests of walkway targets, 4 steps
	//First walkway is next to a room and the edge of the board
	//Second walkway is in the middle of the map
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(16, 29, 4);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 25)));
		assertTrue(targets.contains(board.getCellAt(15, 26)));
		assertTrue(targets.contains(board.getCellAt(15, 28)));
		assertTrue(targets.contains(board.getCellAt(16, 27)));

		board.calcTargets(9, 19, 4);
		targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCellAt(8, 16)));
		assertTrue(targets.contains(board.getCellAt(7, 17)));	
		assertTrue(targets.contains(board.getCellAt(6, 18)));	
		assertTrue(targets.contains(board.getCellAt(7, 19)));	

		assertTrue(targets.contains(board.getCellAt(8, 20)));
		assertTrue(targets.contains(board.getCellAt(10, 20)));	
		assertTrue(targets.contains(board.getCellAt(11, 19)));	
		assertTrue(targets.contains(board.getCellAt(12, 18)));	

		assertTrue(targets.contains(board.getCellAt(10, 18)));
		assertTrue(targets.contains(board.getCellAt(8, 18)));	
		assertTrue(targets.contains(board.getCellAt(6, 20)));	
		assertTrue(targets.contains(board.getCellAt(7, 21)));	

		assertTrue(targets.contains(board.getCellAt(12, 20)));
		assertTrue(targets.contains(board.getCellAt(13, 19)));
	}	

	//Tests of walkway targets, 6 steps
	//One walkway tested which is next to a room and the edge of the board and near no doors
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testTargetSixSteps() {
		board.calcTargets(16, 29, 6);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 23)));
		assertTrue(targets.contains(board.getCellAt(15, 24)));	
		assertTrue(targets.contains(board.getCellAt(14, 25)));	
		assertTrue(targets.contains(board.getCellAt(15, 26)));	

		assertTrue(targets.contains(board.getCellAt(15, 28)));	
		assertTrue(targets.contains(board.getCellAt(16, 25)));	
		assertTrue(targets.contains(board.getCellAt(16, 27)));	
	}	

	//Tests of targets resulting in doorways exactly 2 steps away
	//Dark Grey on Rooms Spreadsheet
	@Test 
	public void testTargetsIntoExactRoom() {
		//Two doorways are exactly 2 steps away
		board.calcTargets(0, 21, 2);
		Set<BoardCell> targets = board.getTargets();
		System.out.println(targets.size());
		assertEquals(4, targets.size());

		//directly left
		assertTrue(targets.contains(board.getCellAt(0, 19))); //doorway

		//directly down
		assertTrue(targets.contains(board.getCellAt(2, 21)));

		//down one, left/right one
		assertTrue(targets.contains(board.getCellAt(1, 20)));
		assertTrue(targets.contains(board.getCellAt(1, 22)));
	}

	//Tests of targets resulting in doorways less than and equal to 3 steps away
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testTargetsIntoShorterRooms()  {
		board.calcTargets(19, 9, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());

		//walkways
		assertTrue(targets.contains(board.getCellAt(21, 10)));
		assertTrue(targets.contains(board.getCellAt(17, 8)));
		assertTrue(targets.contains(board.getCellAt(19, 8)));
		assertTrue(targets.contains(board.getCellAt(16, 9)));
		assertTrue(targets.contains(board.getCellAt(18, 9)));

		//into the room in full length
		assertTrue(targets.contains(board.getCellAt(17, 10)));	

		//into the room in shorter length
		assertTrue(targets.contains(board.getCellAt(18, 10)));		
	}

	//Tests of targets resulting from exiting a room
	//Dark Grey on Rooms Spreadsheet
	@Test
	public void testRoomExit() {
		//Take one step, essentially just the adjacency list
		board.calcTargets(21, 23, 1);
		Set<BoardCell> targets = board.getTargets();

		//Ensure doesn't exit through the wall
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(21, 22)));
		assertTrue(targets.contains(board.getCellAt(20, 22)));
		assertTrue(targets.contains(board.getCellAt(22, 22)));
		assertTrue(targets.contains(board.getCellAt(19, 22)));

		//Take two steps
		board.calcTargets(21, 23, 2);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		
		assertTrue(targets.contains(board.getCellAt(21, 22)));
		assertTrue(targets.contains(board.getCellAt(20, 22)));
		assertTrue(targets.contains(board.getCellAt(22, 22)));
		assertTrue(targets.contains(board.getCellAt(19, 22)));
		assertTrue(targets.contains(board.getCellAt(21, 21)));
		assertTrue(targets.contains(board.getCellAt(20, 21)));
		assertTrue(targets.contains(board.getCellAt(22, 21)));
		assertTrue(targets.contains(board.getCellAt(19, 21)));
		assertTrue(targets.contains(board.getCellAt(23, 22)));
		assertTrue(targets.contains(board.getCellAt(18, 22)));
	}

}
