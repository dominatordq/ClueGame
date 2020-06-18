package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Player - parent class that stores details about a single player
 * @author sethasadi dquintana
 *
 */
public class Player {
	//instance variables
	protected String playerName;
	protected Color color;
	protected int row;
	protected int column;
	protected ArrayList<Card> myCards;
	protected ArrayList<Card> seenCards;
	protected final static int PLAYER_DRAW_SIZE = 25;  
	protected int totalDiceRoll;

	protected int playerHolder;
	
	protected boolean readyToMakeAccusation;
	protected Solution accusationReadyToMake;

	//create a static variable for the board
	protected static Board gameboard;

	protected char lastVisitedRoom;
	protected char twoLastVisitedRoom;

	/**
	 * default constructor that initializes a player
	 */
	public Player() {
		myCards = new ArrayList<Card>();
		seenCards = new ArrayList<Card>();
		//get the instance of the board
		gameboard = Board.getInstance();
		playerHolder = 0;
	}

	public void setName(String name) {
		playerName = name;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Color getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public ArrayList<Card> getPlayerCards() {
		return myCards;
	}

	/**
	 * addPlayerCard- function that adds a player card to the arraylist myCards
	 * @param cardToAdd
	 */
	public void addPlayerCard(Card cardToAdd) {
		myCards.add(cardToAdd);
	}

	/**
	 * addSeenCard - function that adds a seen card to a player's arraylist of seen cards
	 * @param cardToAdd
	 */
	public void addSeenCard(Card cardToAdd) {
		seenCards.add(cardToAdd);
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		//no functionality for HumanPlayer
		return null;
	}

	/**
	 * setLastVisitedRoom - sets the last visited room, as well as the second to last visited room
	 * @param c
	 */
	public void setLastVisitedRoom(char c) {
		twoLastVisitedRoom = lastVisitedRoom;
		lastVisitedRoom = c;
	}

	/**
	 * makeAccusation - function for when a player makes an accusation
	 * @param accusationMade
	 * @return accusationMade.(person, weapon, or room) or null
	 */
	public Card makeAccusation(Solution accusationMade) {
		Solution answerToTestAgainst;
		answerToTestAgainst = gameboard.getAnswer();
		
		if (answerToTestAgainst.person != accusationMade.person) {
			return accusationMade.person;
		} else if (answerToTestAgainst.weapon != accusationMade.weapon) {
			return accusationMade.weapon;
		} else if (answerToTestAgainst.room != accusationMade.room) {
			return accusationMade.room;
		}
		
		return null;
	}

	public Solution createSuggestion() {
		//no functionality for HumanPlayer
		return null;
	}

	/**
	 * disproveSuggestion - function that will determine if a suggestion can be disproved or not
	 * @param toDisprove
	 * @return null or inMyCards.get(index)
	 */
	public Card disproveSuggestion(Solution toDisprove) {
		//check if the player has any of the cards toDisprove
		ArrayList<Card> inMyCards = new ArrayList<Card>();

		for (Card card : myCards) {
			if (toDisprove.person == card) {
				inMyCards.add(card);
			} else if (toDisprove.weapon == card) {
				inMyCards.add(card);
			} else if (toDisprove.room == card) {
				inMyCards.add(card);
			}
		}

		if (inMyCards.isEmpty()) {
			return null;
		} else if (inMyCards.size() == 1) {
			return inMyCards.get(0);
		} else {
			Random randNum = new Random();
			int toChoose = randNum.nextInt(inMyCards.size());
			return inMyCards.get(toChoose);
		}
	}

	/**
	 * draw - draw a circle representing the player on the cell they happen to be on
	 * @param g
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(color);
		Ellipse2D playerPiece = new Ellipse2D.Double(column*PLAYER_DRAW_SIZE, row*PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE);

		boolean found = false;

		if (gameboard.getCellAt(row, column).isDoorway()) {
			if (playerHolder == 0) {
				for (int i = 0; i < gameboard.getNumRows(); i++ ) {
					if (found) {
						break;
					}
					for (int j = 0; j < gameboard.getNumColumns(); j++) {
						if (found) {
							break;
						}
						if (gameboard.getCellAt(i, j).getInitial() == gameboard.getCellAt(row, column).getInitial() && gameboard.getCellAt(i, j).doesHoldPerson()) {
							//ensure no other player is on this space
							boolean noPlayerHas = true;
							for (Player player : gameboard.getPlayers()) {
								if (player.getPlayerHolder() == gameboard.getCellAt(i, j).getPersonHolderNum()) {
									noPlayerHas = false;
								} 
							}
							if (noPlayerHas) {
								found = true;
								playerHolder = gameboard.getCellAt(i, j).getPersonHolderNum();
								playerPiece = new Ellipse2D.Double(gameboard.getCellAt(i, j).getBoardCol()*PLAYER_DRAW_SIZE, gameboard.getCellAt(i, j).getBoardRow()*PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE);
							}
						}
					}
				}
			} else {
				for (int i = 0; i < gameboard.getNumRows(); i++ ) {
					if (found) {
						break;
					}
					for (int j = 0; j < gameboard.getNumColumns(); j++) {
						if (found) {
							break;
						}
						if (playerHolder == gameboard.getCellAt(i, j).getPersonHolderNum()) {
							playerPiece = new Ellipse2D.Double(gameboard.getCellAt(i, j).getBoardCol()*PLAYER_DRAW_SIZE, gameboard.getCellAt(i, j).getBoardRow()*PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE, PLAYER_DRAW_SIZE);
							found = true;
							break;
						} 
					}
				}
			}
		}

		g2d.fill(playerPiece);
	}

	/**
	 * makeMove - function that controls a player's movement
	 * @param ctrlPanel
	 * @return true or false
	 */
	public boolean makeMove(GUI_ClueControl ctrlPanel) {
		//update the player name
		ctrlPanel.setPlayerTurn(playerName);
		
		//erase the other fields
		ctrlPanel.setDiceRoll("");
		ctrlPanel.setPlayerGuess("");
		ctrlPanel.setResult("");
		
		if (readyToMakeAccusation) {
			Card wrongCard = makeAccusation(accusationReadyToMake);
			//if the accusation is correct and not disproved, the player making the accusation wins
			if (wrongCard == null) {
				JOptionPane.showMessageDialog(new JFrame(), playerName + " Won!\n" + "Their guess was: " + accusationReadyToMake.person.getCardName() + " " + accusationReadyToMake.room.getCardName() + " " + accusationReadyToMake.weapon.getCardName() + "\nCongratulations!!!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else {
				//create a panel that displays a wrong accusation 
				JOptionPane.showMessageDialog(new JFrame(), playerName + " made an accusation\n" + "Their accusation was: " + accusationReadyToMake.person.getCardName() + " " + accusationReadyToMake.room.getCardName() + " " + accusationReadyToMake.weapon.getCardName() + "\nThe Accusation was false because the " + wrongCard.getCardName() + " card was wrong", "Wrong Accusation", JOptionPane.INFORMATION_MESSAGE);
				
				//tell all the players which card was shown
				gameboard.tellPlayersDisprovenCard(wrongCard);
				
				//have the players update whether they still want to make an accusation
				gameboard.tellPlayersNonDisprovedSuggestion(accusationReadyToMake);
				
				return false;
			}
		}
		Random rand = new Random();;
		totalDiceRoll = rand.nextInt(6) + 1;
		ctrlPanel.setDiceRoll(String.valueOf(totalDiceRoll));
		gameboard.calcTargets(row, column, totalDiceRoll);
		return true;
	}

	public boolean humanTargetSelect(int y, int x, GUI_ClueControl ctrlPanel) {
		// do nothing because the human will override this
		return false;
	}

	public int getPlayerHolder() {
		return playerHolder;
	}
	
	public boolean isReadyToMakeAccusation() {
		return readyToMakeAccusation;
	}
	
	/**
	 * checkThisSupposedAccusation - take a solution and check if the player has seen any of the cards (inclduing their own)
	 * @param accus - a solution that just made it around the entire table without being disproved
	 */
	public void checkThisSupposedAccusation(Solution accus) {
		boolean makeIt = true;
		//check if the weapon in the solution is in the player's own cards or seen cards
		if (myCards.contains(accus.weapon) || seenCards.contains(accus.weapon)) {
			//if the player has seen it, do not make this accusation because it is false
			makeIt = false;
		}
		
		//check if the room in the solution is in the player's own cards or seen cards
		if (myCards.contains(accus.room) || seenCards.contains(accus.room)) {
			//if the player has seen it, do not make this accusation because it is false
			makeIt = false;
		}
		
		//check if the person in the solution is in the player's own cards or seen cards
		if (myCards.contains(accus.person) || seenCards.contains(accus.person)) {
			//if the player has seen it, do not make this accusation because it is false
			makeIt = false;
		}
		
		//if none of the cards in the solution have been seen, mark the flag to make this accusation
		if (makeIt) {
			readyToMakeAccusation = true;
			accusationReadyToMake = accus;
		} else {
			readyToMakeAccusation = false;
		}
	}
	
	/**
	 * moveForGuess - move the player when they are mentioned in a suggestion
	 * @param roomToMoveTo - BoardCell for the room doorway
	 */
	public void moveForGuess(BoardCell roomToMoveTo) {
		playerHolder = 0;
		row = roomToMoveTo.getBoardRow();
		column = roomToMoveTo.getBoardCol();
		gameboard.repaint();
	}
}
