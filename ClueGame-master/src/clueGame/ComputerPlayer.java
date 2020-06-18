package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * ComputerPlayer - child class of Player that has the functionality to be controlled by the computer
 * @author sethasadi dquintana
 *
 */
public class ComputerPlayer extends Player {

	public ComputerPlayer() {
	}

	//implement selectTarget functionality
	@Override
	public BoardCell selectTarget(Set<BoardCell> targets) {
		//local ArrayList<BoardCell> of targets that are rooms and not last visited
		ArrayList<BoardCell> roomTargets = new ArrayList<BoardCell>();

		//local ArrayList<BoardCell> of targets that are walkways
		ArrayList<BoardCell> walkwayTargetsAndRoomLastVisited = new ArrayList<BoardCell>();

		//random used to select targets
		Random rand = new Random();

		//iterate over the targets and find rooms to add to roomTargets
		for (BoardCell cell : targets) {
			if (cell.isDoorway() && cell.getInitial() != lastVisitedRoom && cell.getInitial() != twoLastVisitedRoom) {
				roomTargets.add(cell);
			} else {
				walkwayTargetsAndRoomLastVisited.add(cell);
			}
		}

		//if no rooms that were not last visited found, randomly select target
		if (roomTargets.isEmpty()) {
			int targetToChoose = rand.nextInt(walkwayTargetsAndRoomLastVisited.size());
			return walkwayTargetsAndRoomLastVisited.get(targetToChoose);
		} else {
			//odds of choosing a room will be based on how many of that room's door you are close to
			int targetToChoose = rand.nextInt(roomTargets.size());
			return roomTargets.get(targetToChoose);
		}
	}
	
	/**
	 * createSuggestion - generates a suggestion made by a computer player
	 * @return Solution
	 */
	@Override
	public Solution createSuggestion() {
		//generate a solution
		//the suggested room should be based on where the player is
		Card suggestRoom = new Card();
		for (Card card : gameboard.getCards()) {
			if (card.getCardName().equals(gameboard.getLegend().get(gameboard.getCellAt(row, column).getInitial()))) {
				suggestRoom = card;
				break;
			}
		}
		
		Card suggestPerson;
		Card suggestWeapon;
		
		//get all the cards
		ArrayList<Card> toSuggest = new ArrayList<Card>();
		toSuggest.addAll(gameboard.getCards());
		
		//create variables to hold the available people and wepaons
		ArrayList<Card> toSuggestWeapon = new ArrayList<Card>();
		ArrayList<Card> toSuggestPerson = new ArrayList<Card>();
		
		//iterate through all the cards and sort the unseen people and weapon cards
		for (int i = 0; i < toSuggest.size(); i++) {
			//skip the card if its a room
			if (toSuggest.get(i).getCardType() == CardType.ROOM) {
				continue;
			}
			
			//check if the card exists in the player's cards or the player's seen cards
			boolean found = false;
			for (Card card : myCards) {
				if (card.equals(toSuggest.get(i))) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				for (Card card : seenCards) {
					if (card.equals(toSuggest.get(i))) {
						found = true;
						break;
					}
				}
			}
			
			//if the card was never found, check its type and sort it
			if (!found) {
				if (toSuggest.get(i).getCardType() == CardType.PERSON) {
					toSuggestPerson.add(toSuggest.get(i));
				} else if (toSuggest.get(i).getCardType() == CardType.WEAPON) {
					toSuggestWeapon.add(toSuggest.get(i));
				}
			}
		}
		
		//randomly generate among the remaining cards which to suggest
		Random randNum = new Random();
		
		int toChoose = randNum.nextInt(toSuggestPerson.size());
		suggestPerson = toSuggestPerson.get(toChoose);
		
		toChoose = randNum.nextInt(toSuggestWeapon.size());
		suggestWeapon = toSuggestWeapon.get(toChoose);
		
		return new Solution(suggestPerson, suggestRoom, suggestWeapon);
	}
	
	/**
	 * makeMove - moves a computer player when it is their turn 
	 * @param ctrlPanel
	 * @return true
	 */
	@Override
	public boolean makeMove(GUI_ClueControl ctrlPanel) {
		if (super.makeMove(ctrlPanel) == false) {
			return true;
		}
		BoardCell target = selectTarget(gameboard.getTargets());
		row = target.getBoardRow();
		column = target.getBoardCol();
		gameboard.repaint();
		
		if (target.isRoom()) {
			//mark this as the last room visited for the player
			setLastVisitedRoom(target.getInitial());
			
			//the computer entered a room so make a suggestion and handle the results
			Solution toSuggest = createSuggestion();
			
			//move the suggested player to the room unless it is this player
			for (Player player : gameboard.getPlayers()) {
				if (player != this && player.getPlayerName().equals(toSuggest.person.getCardName())) {
					player.moveForGuess(target);
					break;
				}
			}
			
			//display the guess on the ClueControl GUI
			ctrlPanel.setPlayerGuess(toSuggest.person.getCardName() + " " + toSuggest.room.getCardName() + " " + toSuggest.weapon.getCardName());
			
			//make the suggestion
			Card disprovedCard = gameboard.handleSuggestion(toSuggest, playerName);
			
			//check whether the card was disproved
			if (disprovedCard == null) {
				//tell all the players it went all the way around without being disproved
				gameboard.tellPlayersNonDisprovedSuggestion(toSuggest);
				//display that no new clue was found
				ctrlPanel.setResult("No New Clue");
			} else {
				//tell all the players which card was shown
				gameboard.tellPlayersDisprovenCard(disprovedCard);
				
				//update the ClueControl GUI with what card was shown
				ctrlPanel.setResult(disprovedCard.getCardName());
			}
		}
		
		playerHolder = 0;
		
		return true;
	}
}
