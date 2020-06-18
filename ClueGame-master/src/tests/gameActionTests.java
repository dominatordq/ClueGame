package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

/**
 * gameActionTests - JUnit tests for testing target location, accusations, and suggestions
 * @author sethasadi, dquintana
 *
 */
public class gameActionTests {
	//static Board because it should be setup beforehand and only one should exist
	private static Board gameboard;

	//test objects to use when testing target location, accusations, and suggestions
	Player testPlayer;

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
	public void testTargetWithNoRoomInList() {
		//get the third player on the list, which should be the ComputerPlayer "Wife"
		testPlayer = gameboard.getPlayers().get(2);

		//test that the retrieved player is in fact a ComputerPlayer
		assert(testPlayer instanceof ComputerPlayer);

		//initialize a map to hold the correct potential targets for the ComputerPlayer 
		// with a value of how many times it has been selected
		HashMap<BoardCell, Integer> targetsSelected = new HashMap<BoardCell, Integer>();

		//populate map
		targetsSelected.put(gameboard.getCellAt(8, 17), 0);
		targetsSelected.put(gameboard.getCellAt(7, 18), 0);
		targetsSelected.put(gameboard.getCellAt(8, 19), 0);
		targetsSelected.put(gameboard.getCellAt(9, 18), 0);
		targetsSelected.put(gameboard.getCellAt(9, 20), 0);
		//targetsSelected.put(gameboard.getCellAt(10, 19), 0);
		targetsSelected.put(gameboard.getCellAt(11, 18), 0);
		targetsSelected.put(gameboard.getCellAt(11, 20), 0);
		targetsSelected.put(gameboard.getCellAt(12, 17), 0);
		targetsSelected.put(gameboard.getCellAt(12, 19), 0);
		targetsSelected.put(gameboard.getCellAt(13, 18), 0);

		//run calcTargets for the player's location and a roll of 3
		gameboard.calcTargets(testPlayer.getRow(), testPlayer.getColumn() - 1, 3);

		//for loop that calls selectTarget() 1000 times, and increases the map key values 
		// each time a specific BoardCell is visited
		for (int i = 0; i < 1000; i++) {
			BoardCell target = testPlayer.selectTarget(gameboard.getTargets());
			//make sure the retrieved target is in targetsSelected
			assert(targetsSelected.containsKey(target));

			//increment the visited amount by 1
			targetsSelected.put(target, targetsSelected.get(target) + 1);
		}

		//iterate over the map, and assert that each visited value is greater than 10
		for (BoardCell b : targetsSelected.keySet()) {
			assert(targetsSelected.get(b) > 10);
		}
	}

	@Test
	public void testTargetWithRoomInListNotLastVisited() {
		//get the second player on the list, which should be the ComputerPlayer "Butler"
		testPlayer = gameboard.getPlayers().get(1);

		//run calcTargets for the player's location and a roll of 6 to reach a room
		gameboard.calcTargets(testPlayer.getRow(), testPlayer.getColumn(), 6);

		//for loop that calls calcTargets() 50 times to ensure every time the target selected is the room
		for (int i = 0; i < 50; i++) {
			BoardCell target = testPlayer.selectTarget(gameboard.getTargets());
			assertEquals(target, gameboard.getCellAt(18, 10));
		}
	}

	@Test
	public void testTargetwithRoomInListThatIsLastVisited() {
		//get the second player on the list, which should be the ComputerPlayer "Butler"
		testPlayer = gameboard.getPlayers().get(1);

		//run calcTargets for the player's location and a roll of 6 to reach a room
		gameboard.calcTargets(testPlayer.getRow(), testPlayer.getColumn(), 6);


		//initialize a map to hold the correct potential targets for the ComputerPlayer 
		// with a value of how many times it has been selected
		HashMap<BoardCell, Integer> targetsSelected = new HashMap<BoardCell, Integer>();

		//set the player's last visited room to the Dining Hall
		testPlayer.setLastVisitedRoom('D');

		targetsSelected.put(gameboard.getCellAt(18, 10), 0); //this is the room that was last visited
		targetsSelected.put(gameboard.getCellAt(18, 8), 0);
		targetsSelected.put(gameboard.getCellAt(17, 9), 0);

		//for loop that calls selectTarget() 1000 times, and increases the map key values 
		// each time a specific BoardCell is visited
		for (int i = 0; i < 1000; i++) {
			BoardCell target = testPlayer.selectTarget(gameboard.getTargets());
			//make sure the retrieved target is in targetsSelected
			assert(targetsSelected.containsKey(target));

			//increment the visited amount by 1
			targetsSelected.put(target, targetsSelected.get(target) + 1);
		}

		//iterate over the map, and assert that each visited value is greater than 100
		for (BoardCell b : targetsSelected.keySet()) {
			assert(targetsSelected.get(b) > 100);
		}
	}

	@Test
	public void testMakingAnAccusation() {
		//get the second player on the list, which should be the ComputerPlayer "Butler"
		testPlayer = gameboard.getPlayers().get(1);

		//create cards that will be the solution
		Card rightWeapon = new Card("240p Resolution", CardType.WEAPON);
		Card rightRoom = new Card("Trophy Room", CardType.ROOM);
		Card rightPerson = new Card("The Gardener", CardType.PERSON);

		//create a card of each type that will be the wrong ones
		Card wrongWeapon = new Card("90-inch OLED TV", CardType.WEAPON);
		Card wrongRoom = new Card("Theater Room", CardType.ROOM);
		Card wrongPerson = new Card("The Billionaire", CardType.PERSON);

		//set the solution to these cards
		gameboard.setAnswer(rightPerson, rightRoom, rightWeapon);

		//test that makeAccuesation returns true when called with the correct cards
		Solution rightCards = new Solution(rightPerson, rightRoom, rightWeapon);
		assertEquals(testPlayer.makeAccusation(rightCards), null);

		//test that makeAccuesation returns false when called with the wrong person
		Solution wrongPersonCard = new Solution(wrongPerson, rightRoom, rightWeapon);
		assertEquals(testPlayer.makeAccusation(wrongPersonCard), wrongPerson);

		//test that makeAccuesation returns false when called with the wrong room
		Solution wrongRoomCard = new Solution(rightPerson, wrongRoom, rightWeapon);
		assertEquals(testPlayer.makeAccusation(wrongRoomCard), wrongRoom);

		//test that makeAccuesation returns false when called with the wrong weapon
		Solution wrongWeaponCard = new Solution(rightPerson, rightRoom, wrongWeapon);
		assertEquals(testPlayer.makeAccusation(wrongWeaponCard), wrongWeapon);
	}

	@Test
	public void testCreatingSuggestion() {
		//create a computer player
		testPlayer = new ComputerPlayer();
		Card oneCard = new Card("The Gardener", CardType.PERSON);
		Card twoCard = new Card("The Chef", CardType.PERSON);
		Card threeCard = new Card("Butcher Knife", CardType.WEAPON);
		testPlayer.addPlayerCard(oneCard);
		testPlayer.addPlayerCard(twoCard);
		testPlayer.addPlayerCard(threeCard);

		//set the testPlayer's location to inside a room
		testPlayer.setRow(21);
		testPlayer.setColumn(4);

		//get the list of cards for the game
		ArrayList<Card> testCards = new ArrayList<Card>();
		testCards = gameboard.getCards();

		//get the testPlayer's cards
		ArrayList<Card> testPlayerCards = new ArrayList<Card>();
		testPlayerCards = testPlayer.getPlayerCards();

		//a card representing the room the player should suggest
		Card correctRoomCard = new Card("Trophy Room", CardType.ROOM);

		//iterate over the deck and put the person cards into testPersons and the weapon cards into testWeapons
		// skipping over any card that testPlayer is holding
		ArrayList<Card> testPersons = new ArrayList<Card>();
		ArrayList<Card> testWeapons = new ArrayList<Card>();

		for (Card card : testCards) {
			if (!card.equals(oneCard) && !card.equals(twoCard) && !card.equals(threeCard)) {
				if (card.getCardType() == CardType.PERSON) {
					testPersons.add(card);
				} else if (card.getCardType() == CardType.WEAPON) {
					testWeapons.add(card);
				}
			}
		}

		//remove the first card from testPersons and testWeapons and add them to testPlayer's seen cards
		testPlayer.addSeenCard(testPersons.get(1));
		testPersons.remove(1);

		testPlayer.addSeenCard(testWeapons.get(1));
		testWeapons.remove(1);

		//add all remaining cards to a map that will have its value increased every time it is suggested
		HashMap<String, Integer> cardMap = new HashMap<String, Integer>();

		for (Card card : testPersons) {
			cardMap.put(card.getCardName(), 0);
		}

		for (Card card : testWeapons) {
			cardMap.put(card.getCardName(), 0);
		}

		//create Solution to hold the player's suggestions
		Solution testPlayerSuggestion;

		//test that the testPlayer's choice of person and weapon are random among his unseen cards
		for (int i = 0; i < 1000; i++) {
			testPlayerSuggestion = testPlayer.createSuggestion();

			//ensure the map contains the card names for the person and the weapon
			assert(cardMap.containsKey(testPlayerSuggestion.person.getCardName()));
			assert(cardMap.containsKey(testPlayerSuggestion.weapon.getCardName()));

			cardMap.put(testPlayerSuggestion.person.getCardName(), cardMap.get(testPlayerSuggestion.person.getCardName()) + 1);
			cardMap.put(testPlayerSuggestion.weapon.getCardName(), cardMap.get(testPlayerSuggestion.weapon.getCardName()) + 1);

			//check that the room card is the correct card
			assert(testPlayerSuggestion.room.equals(correctRoomCard.getCardName()));
		}

		for (String s : cardMap.keySet()) {
			assert(cardMap.get(s) > 50);
		}

		//add all the rest of the person and weapon cards to testPlayer's seen cards except one person and one weapon
		Card onePersonLeft = testPersons.get(1);
		testPersons.remove(1);
		Card oneWeaponLeft = testWeapons.get(1);
		testWeapons.remove(1);

		for (Card card : testPersons) {
			testPlayer.addSeenCard(card);
		}

		for (Card card : testWeapons) {
			testPlayer.addSeenCard(card);
		}

		//ensure the suggested person and weapon are the single cards the player has not seen
		for (int i = 0; i < 10; i++) {
			testPlayerSuggestion = testPlayer.createSuggestion();

			assert(testPlayerSuggestion.person.equals(onePersonLeft));
			assert(testPlayerSuggestion.weapon.equals(oneWeaponLeft));

			//check that the room card is the correct card
			assert(testPlayerSuggestion.room.equals(correctRoomCard.getCardName()));
		}
	}

	@Test
	public void testDisprovingASuggestion() {
		//create a computer player
		testPlayer = new ComputerPlayer();
		
		//create a solution that will be used to call disprove suggestion
		Solution toDisprove;
		
		//give the player a card of each type
		Card oneCard = new Card("The Gardener", CardType.PERSON);
		Card twoCard = new Card("Trophy Room", CardType.ROOM);
		Card threeCard = new Card("Butcher Knife", CardType.WEAPON);
		
		testPlayer.addPlayerCard(oneCard);
		testPlayer.addPlayerCard(twoCard);
		testPlayer.addPlayerCard(threeCard);
		
		//make the cards that the player will not have in their hand
		Card fourCard = new Card("The Wife", CardType.PERSON);
		Card fiveCard = new Card("VR Room", CardType.ROOM);
		Card sixCard = new Card("240p Resolution", CardType.WEAPON);
		
		//test that a suggestion for which the player has none of will be returned with null
		toDisprove = new Solution(fourCard, fiveCard, sixCard);
		assert(testPlayer.disproveSuggestion(toDisprove) == null);
		
		//test that a suggestion for which the player has one of the cards returns that card
		toDisprove = new Solution(oneCard, fiveCard, sixCard);
		assert(testPlayer.disproveSuggestion(toDisprove).equals(oneCard));
		
		boolean oneShown = false, twoShown = false, threeShown = false;
		
		toDisprove = new Solution(oneCard, twoCard, threeCard);
		
		//call disproveSuggestion 30 times and make sure each card is shown at least once
		for (int i = 0; i < 30; i++) {
			Card disproved = testPlayer.disproveSuggestion(toDisprove);
			
			if (disproved.equals(oneCard)) {
				oneShown = true;
			} else if (disproved.equals(twoCard)) {
				twoShown = true;
			} else if (disproved.equals(threeCard)) {
				threeShown = true;
			}
		}
		
		assert(oneShown);
		assert(twoShown);
		assert(threeShown);

	}
	
	@Test
	public void testHandlingASuggestion() {
		//get the players
		ArrayList<Player> testPlayers = gameboard.getPlayers();
		
		//get the answer to know which query no one can disprove
		Solution theAnswer = gameboard.getAnswer();
		
		//create a card that will be assigned with the card being looked for
		Card toCheck;
		
		//create a solution that will be used when making accusations
		Solution toAccuse = new Solution();
		
		//run query from player 1 with theAnswer and ensure null is returned
		toAccuse.person = theAnswer.person;
		toAccuse.weapon = theAnswer.weapon;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(1).getPlayerName()) == null);
		
		//run query from player 1 with 1 of his cards and ensure null is returned (other 2 will be from theAnswer)
		toCheck = testPlayers.get(1).getPlayerCards().get(1);
		toAccuse.person = toCheck;
		toAccuse.weapon = theAnswer.weapon;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(1).getPlayerName()) == null);
		
		//run query from player 1 with one of player 0's cards to ensure player 0 shows a card (other 2 will be from theAnswer)
		toCheck = testPlayers.get(0).getPlayerCards().get(1);
		toAccuse.person = toCheck;
		toAccuse.weapon = theAnswer.weapon;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(1).getPlayerName()).equals(toCheck.getCardName()));
		
		//run query from player 0 with 1 of his cards and ensure null is returned (other 2 will be from theAnswer)
		toCheck = testPlayers.get(0).getPlayerCards().get(1);
		toAccuse.person = toCheck;
		toAccuse.weapon = theAnswer.weapon;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(0).getPlayerName()) == null);
		
		//run query from player 0 with one of player 1's cards, one of player 2's cards, and one from theAnswer
		// ensure player 1's card is returned
		toCheck = testPlayers.get(2).getPlayerCards().get(1);
		toAccuse.person = toCheck;
		toCheck = testPlayers.get(1).getPlayerCards().get(1);
		toAccuse.weapon = toCheck;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(0).getPlayerName()).equals(toCheck.getCardName()));
		
		//run query from player 1 with one of player 2's cards, one of player 0's cards, and one from theAnswer
		// ensure player 2's card is returned
		toCheck = testPlayers.get(0).getPlayerCards().get(1);
		toAccuse.person = toCheck;
		toCheck = testPlayers.get(2).getPlayerCards().get(1);
		toAccuse.weapon = toCheck;
		toAccuse.room = theAnswer.room;
		assert(gameboard.handleSuggestion(toAccuse, testPlayers.get(1).getPlayerName()).equals(toCheck.getCardName()));
	}
}
