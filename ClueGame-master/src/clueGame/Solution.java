package clueGame;

/**
 * Solution - class that has the solution to Clue 
 * @author sethasadi dquintana
 *
 */
public class Solution {
	public Card weapon, person, room;
	
	/**
	 * constructor that sets the solution cards
	 * @param person
	 * @param room
	 * @param weapon
	 */
	public Solution(Card person, Card room, Card weapon) {
		this.weapon = weapon;
		this.room = room;
		this.person = person;
	}
	
	public Solution() {
		
	}
}
