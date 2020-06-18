package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

/**
 * gameSetupTests - JUnit tests for Board to make sure the players and cards load correctly and that the cards are dealt correctly
 * @author sethasadi, dquintana
 *
 */
public class gameSetupTests {

	//static Board because it should be setup beforehand and only one should exist
	private static Board gameboard;

	//test objects to use when testing players and cards
	Player testPlayer;
	Color testColor;
	ArrayList<Player> testPlayers;
	ArrayList<Card> testCards;

	@BeforeClass
	public static void setUp() {
		//Board is singleton, get the only instance
		gameboard = Board.getInstance();
		//set the file names to use my config files
		gameboard.setConfigFiles("Rooms.csv", "rooms.txt");		
		//set the file names to use player and card config files
		gameboard.setGameSetupConfigFiles("players.txt", "cards.txt");	
		//Initialize will load ALL config files 
		gameboard.initialize();
		//load the players
		gameboard.loadPlayers();
		//load the cards
		gameboard.loadCards();
		//deal the cards
		gameboard.dealCards();
	}

	@Test
	public void testFirstPlayerLoadedCorrectly() {
		//assign testPlayers with the gameboard's players
		testPlayers = gameboard.getPlayers();

		//test the size of the players ArrayList to ensure 6 players have been loaded
		assertEquals(testPlayers.size(), 6);

		//assign testPlayer to the first player in order to test its properties
		testPlayer = testPlayers.get(0);

		//assign testColor to what the player's color should be
		testColor = new Color(64, 180, 232);

		//test the player is human
		assert(testPlayer instanceof HumanPlayer);
		//test the player's name
		assertEquals(testPlayer.getPlayerName(), "The Billionaire");
		//test the player's color
		assertEquals(testPlayer.getColor(), testColor);
		//test the player's starting location
		assertEquals(testPlayer.getRow(), 10);
		assertEquals(testPlayer.getColumn(), 0);
	}

	@Test
	public void testFourthPlayerLoadedCorrectly() {
		//assign testPlayers with the gameboard's players
		testPlayers = gameboard.getPlayers();

		//test the size of the players ArrayList to ensure 6 players have been loaded
		assertEquals(testPlayers.size(), 6);

		//assign testPlayer to the fourth player in order to test its properties
		testPlayer = testPlayers.get(3);

		//assign testColor to what the player's color should be
		testColor = new Color(0, 11, 175);

		//test the player is a computer
		assert(testPlayer instanceof ComputerPlayer);
		//test the player's name
		assertEquals(testPlayer.getPlayerName(), "The Pool Boy");
		//test the player's color
		assertEquals(testPlayer.getColor(), testColor);
		//test the player's starting location
		assertEquals(testPlayer.getRow(), 16);
		assertEquals(testPlayer.getColumn(), 24);
	}

	@Test
	public void testLastPlayerLoadedCorrectly() {
		//assign testPlayers with the gameboard's players
		testPlayers = gameboard.getPlayers();

		//test the size of the players ArrayList to ensure 6 players have been loaded
		assertEquals(testPlayers.size(), 6);

		//assign testPlayer to the last player in order to test its properties
		testPlayer = testPlayers.get(5);

		//assign testColor to what the player's color should be
		testColor = new Color(255, 255, 255);

		//test the player is a computer
		assert(testPlayer instanceof ComputerPlayer);
		//test the player's name
		assertEquals(testPlayer.getPlayerName(), "The Chef");
		//test the player's color
		assertEquals(testPlayer.getColor(), testColor);
		//test the player's starting location
		assertEquals(testPlayer.getRow(), 5);
		assertEquals(testPlayer.getColumn(), 5);
	}
	
	@Test
	public void testCardsLoadedCorrectly() {
		//get the cards to test with
		testCards = gameboard.getCards();
		
		//ensure the amount of cards is correct
		assertEquals(testCards.size(), 21);
		
		//add to 3 different ints whenever a card is of a certain type
		int weaponCards = 0, personCards = 0, roomCards = 0;
		for (Card card : testCards) {
			switch (card.getCardType()) {
			case WEAPON:
				weaponCards++;
				break;
			case PERSON:
				personCards++;
				break;
			case ROOM:
				roomCards++;
				break;
			}
		}
		
		//ensure the number of each type of cards is correct
		assertEquals(weaponCards, 6);
		assertEquals(personCards, 6);
		assertEquals(roomCards, 9);
		
		//check to see if a specific weapon, person, and room card are in the cards list based on their index
		assert(testCards.get(1).equals("Indoor Pool")); //Room Card
		assert(testCards.get(13).equals("The Gardener")); //Person Card
		assert(testCards.get(18).equals("240p Resolution")); //Weapon Card
	}
	
	@Test
	public void testCardsDealtCorrectly() {
		//get the cards to test with
		//space is instantiated and then used addAll so when cards are removed from one
		// they are not removed from the other
		testCards = new ArrayList<Card>();
		testCards.addAll(gameboard.getCards());
		
		//get the players to test with
		testPlayers = gameboard.getPlayers();
		
		//calculate the average amount of cards each player should have
		int averageCardsPerPlayer = testCards.size() / testPlayers.size();
		
		//iterate over every player and every player's cards and remove them from the testCards deck
		//this will be done inside of an assert() since the ArrayList .remove() returns false if the
		// specified object is not in the ArrayList, so if the same card is ever dealt twice, an assert() will fail
		//also ensure each player has no more than 1 more/less than the averageCardsPerPlayer value
		for (Player player : testPlayers) {
			//get the player's cards
			ArrayList<Card> playersCards = player.getPlayerCards();
			
			//ensure the player has at most one more/less than the averageCardsPerPlayer
			assert(Math.abs(averageCardsPerPlayer - playersCards.size()) <= 1);
			
			//remove each card in playerCards from testCards and ensure each test passes
			//the test will fail if more than one player has the same card
			for (Card card : playersCards) {
				assert(testCards.remove(card));
			}
		}
		
		//finally, ensure the testCards ArrayList has size 3 to mean every card but the solution has been dealt
		assertEquals(testCards.size(), 3);
	}
}
