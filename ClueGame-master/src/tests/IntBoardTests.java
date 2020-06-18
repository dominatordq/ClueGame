package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import experiment.BoardCell;
import experiment.IntBoard;

/**
 * IntBoardTests - tests for adjacency lists and targets
 * @author sethasadi, dquintana
 *
 */
public class IntBoardTests {
	IntBoard board;
	
	//to setup board before all tests
	@Before
	public void setupBoard() {
		board = new IntBoard();
	}
	
	//test proper adjacent cells for the top left corner
	@Test
	public void testTopLeftCorner() {
		BoardCell cell = board.getCell(0,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}

	//test proper adjacent cells for the bottom right corner
	@Test
	public void testBottomRightCorner() {
		BoardCell cell = board.getCell(3,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(2, testList.size());
	}

	//test proper adjacent cells for the right edge
	@Test
	public void testRightEdge() {
		BoardCell cell = board.getCell(1,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(3, testList.size());
	}

	//test proper adjacent cells for the left edge
	@Test
	public void testLeftEdge() {
		BoardCell cell = board.getCell(2,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(3, testList.size());
	}

	//test proper adjacent cells for the second column middle cell
	@Test
	public void testSecondColumnMiddleGrid() {
		BoardCell cell = board.getCell(1,1);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
	}

	//test proper adjacent cells for the third column middle cell
	@Test
	public void testThirdColumnMiddleGrid() {
		BoardCell cell = board.getCell(2,2);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(4, testList.size());
	}
	
	//first test to check target creation with one step
	@Test
	public void testTargetCreationOneStep1() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 1);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(1, 0)));
	}
	
	//second test to check target creation with one step
	@Test
	public void testTargetCreationOneStep2() {
		BoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 1);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(0, 2)));
		assertTrue(testList.contains(board.getCell(1, 3)));
	}
	
	//first test to check target creation with two steps
	@Test
	public void testTargetCreationTwoStep1() {
		BoardCell cell = board.getCell(1, 2);
		board.calcTargets(cell, 2);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(6, testList.size());
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		//how did i miss these last 2?
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(3, 2)));
	}
	
	//second test to check target creation with two steps
	@Test
	public void testTargetCreationTwoStep2() {
		BoardCell cell = board.getCell(3, 1);
		board.calcTargets(cell, 2);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(1, 1)));
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(3, 3)));
	}
	
	//first test to check target creation with three steps
	@Test
	public void testTargetCreationThreeStep1() {
		BoardCell cell = board.getCell(2, 1);
		board.calcTargets(cell, 3);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(8, testList.size());
		assertTrue(testList.contains(board.getCell(0, 0)));
		assertTrue(testList.contains(board.getCell(0, 2)));
		assertTrue(testList.contains(board.getCell(1, 1)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertTrue(testList.contains(board.getCell(3, 3)));
	}
	
	//second test to check target creation with three steps
	@Test
	public void testTargetCreationThreeStep2() {
		BoardCell cell = board.getCell(2, 3);
		board.calcTargets(cell, 3);
		Set<BoardCell> testList = board.getTargets();
		assertEquals(7, testList.size());
		assertTrue(testList.contains(board.getCell(0, 2)));
		assertTrue(testList.contains(board.getCell(1, 1)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertTrue(testList.contains(board.getCell(3, 3)));
	}
}
