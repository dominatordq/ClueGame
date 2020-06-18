package clueGame;

import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * HumanPlayer - child class of Player that has the functionality to be controlled by a user
 * @author sethasadi dquintana
 *
 */
public class HumanPlayer extends Player{
	Solution suggestionToMake;
	boolean makeSuggestion;

	Solution accusationToMake;
	boolean makeAccusation;

	/**
	 * default constructor that initializes human player's suggestions and accusations
	 */
	public HumanPlayer() {
		suggestionToMake = new Solution();
		makeSuggestion = false;

		accusationToMake = new Solution();
		makeAccusation = false;
	}

	//implement abstract class functionality
	public BoardCell selectTarget(Set<BoardCell> targets) {
		//do nothing
		return null;
	}

	/**
	 * makeMove - calls Player's makeMove, and highlights all possible moves
	 * @param ctrlPanel
	 * @return false
	 */
	@Override
	public boolean makeMove(GUI_ClueControl ctrlPanel) {
		super.makeMove(ctrlPanel);
		//highlight the targets
		for (BoardCell target : gameboard.getTargets()) {
			target.setHighlight();
		}
		gameboard.repaint();

		return false;
	}

	/**
	 * humanTargetSelect - deals with moving human player to a selected target
	 * @param y
	 * @param x
	 * @param ctrlPanel
	 * @return valid
	 */
	@Override
	public boolean humanTargetSelect(int y, int x, GUI_ClueControl ctrlPanel) {
		//convert the clicked location to a row and a column
		int clickedRow = y / PLAYER_DRAW_SIZE;
		int clickedCol = x / PLAYER_DRAW_SIZE;

		//get the cell at that location
		BoardCell clickedCell = gameboard.getCellAt(clickedRow, clickedCol);

		boolean valid = false;

		//iterate through the current targets and check if this is in there
		for (BoardCell target : gameboard.getTargets()) {
			if (target == clickedCell) {
				valid = true;
			}
		}

		if (valid) {
			row = clickedRow;
			column = clickedCol;
			playerHolder = 0;

			//if the location is a room, the player should make a suggestion
			if (clickedCell.isDoorway()) {
				gameboard.repaint();
				makeAHumanSuggestion(ctrlPanel);
			}
		} else {
			String message = "Selected cell is not a valid target\nPlease select a highlighted cell";

			JOptionPane.showMessageDialog(new JFrame(), message, "Dialog", JOptionPane.ERROR_MESSAGE);

			//re highlight spaces since the user clicked somewhere else and repainted
			for (BoardCell target : gameboard.getTargets()) {
				target.setHighlight();
			}
		}

		return valid;
	}

	/**
	 * makeAHumanSuggestion - lets a human player make a suggestion when entering a room
	 * @param ctrlPanel
	 */
	public void makeAHumanSuggestion(GUI_ClueControl ctrlPanel) {
		HumanSuggestion suggestion = new HumanSuggestion();
		suggestion.setVisible(true);

		//the dialog is modal so this next code should not run until the user takes action on the suggestion window
		if (makeSuggestion) {
			makeSuggestion = false;

			//get our cell
			BoardCell target = gameboard.getCellAt(row, column);

			//move the suggested player to the room unless it is this player
			for (Player player : gameboard.getPlayers()) {
				if (player != this && player.getPlayerName().equals(suggestionToMake.person.getCardName())) {
					player.moveForGuess(target);
					break;
				}
			}

			ctrlPanel.setPlayerGuess(suggestionToMake.person.getCardName() + " " + suggestionToMake.room.getCardName() + " " + suggestionToMake.weapon.getCardName());
			Card disprovedCard = gameboard.handleSuggestion(suggestionToMake, playerName);

			//check whether the card was disproved
			if (disprovedCard == null) {
				//tell all the players it went all the way around without being disproved
				gameboard.tellPlayersNonDisprovedSuggestion(suggestionToMake);
				//display that no new clue was found
				ctrlPanel.setResult("No New Clue");
			} else {
				//tell all the players which card was shown
				gameboard.tellPlayersDisprovenCard(disprovedCard);

				//update the ClueControl GUI with what card was shown
				ctrlPanel.setResult(disprovedCard.getCardName());
			}
		}
	}

	/**
	 * makeThisSuggestion - actually makes the human player's suggestion
	 * @param suggest
	 */
	public void makeThisSuggestion(Solution suggest) {
		makeSuggestion = true;
		suggestionToMake = suggest;
	}

	/**
	 * makeAHumanAccusation - function that deals with a human player's accusation
	 * @return true or false
	 */
	public boolean makeAHumanAccusation() {
		
		HumanAccusation suggestion = new HumanAccusation();
		suggestion.setVisible(true);

		//the dialog is modal so this next code should not run until the user takes action on the accusation window
		if (makeAccusation) {
			makeAccusation = false;

			Card wrongCard = makeAccusation(accusationToMake);

			if (wrongCard == null) {
				JOptionPane.showMessageDialog(new JFrame(), "You, " + playerName + ", Won!\n" + "Your guess was: " + accusationToMake.person.getCardName() + " " + accusationToMake.room.getCardName() + " " + accusationToMake.weapon.getCardName() + "\nCongratulations!!!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "You, " + playerName + ", made an accusation\n" + "Your accusation was: " + accusationToMake.person.getCardName() + " " + accusationToMake.room.getCardName() + " " + accusationToMake.weapon.getCardName() + "\nThe Accusation was false because the " + wrongCard.getCardName() + " card was wrong", "Wrong Accusation", JOptionPane.INFORMATION_MESSAGE);

				//tell all the players which card was shown
				gameboard.tellPlayersDisprovenCard(wrongCard);

				return false;
			}
		}
		
		return true;
	}

	/**
	 * makeThisAccusation - actually makes the human player's accusation
	 * @param suggest
	 */
	public void makeThisAccusation(Solution suggest) {
		makeAccusation = true;
		accusationToMake = suggest;
	}

}
