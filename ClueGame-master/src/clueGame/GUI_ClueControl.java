package clueGame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * GUI_ClueControl - The main GUI "control panel" for Clue
 * @author sethasadi, dquintana
 *
 */
public class GUI_ClueControl extends JPanel implements ActionListener, MouseListener {
	private JTextArea playerTurn;
	private JTextArea number;
	private JTextArea playerGuess;
	private JTextArea result;

	private Board gameboard;
	private JButton nextPlayer;
	private JButton accusation;

	private boolean turnOver;
	private Player turnPlayer;

	/**
	 * default constructor that sets up the layout of the GUI
	 */
	public GUI_ClueControl() {
		setLayout(new GridLayout(3, 1));
		add(createTopPanel());
		add(createBottomPanel());
		add(createButtonPanel());
		gameboard = Board.getInstance();
		turnOver = true;
	}

	/**
	 * createButtonPanel - creates the "Next Player" and "Make an Accusation" button
	 * @return buttonPanel
	 */
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		//create buttons
		nextPlayer = new JButton("Next Player");
		accusation = new JButton("Make an Accusation");

		buttonPanel.add(nextPlayer);
		buttonPanel.add(accusation);

		nextPlayer.addActionListener(this);
		accusation.addActionListener(this);
		
		return buttonPanel;
	}

	/**
	 * createTopPanel - adds the turn panel and dice roll panel
	 * @return topPanel
	 */
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.add(createTurnPanel());
		topPanel.add(createDiceRoll());
		return topPanel;
	}

	/**
	 * createTurnPanel - creates the panel relating to who's turn it is
	 * @return turnPanel
	 */
	private JPanel createTurnPanel() {
		JPanel turnPanel = new JPanel();
		JLabel turn = new JLabel("Whose turn?");
		playerTurn = new JTextArea(1, 20);
		playerTurn.setEditable(false);
		turnPanel.add(turn);
		turnPanel.add(playerTurn);
		turnPanel.setBorder(new TitledBorder (new EtchedBorder(), "Player Turn"));
		return turnPanel;
	}

	/**
	 * createBottomPanel - adds the guess panel and guess result panel
	 * @return bottomPanel
	 */
	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		//create JPanel guessPanel, guessResult
		bottomPanel.add(createGuessPanel());
		bottomPanel.add(createGuessResult());
		return bottomPanel;
	}

	/**
	 * createDiceRoll - creates the dice roll panel for the GUI
	 * @return
	 */
	private JPanel createDiceRoll() {
		JPanel diceRoll = new JPanel();
		JLabel roll = new JLabel("Roll");
		number = new JTextArea(1, 5);
		number.setEditable(false);
		diceRoll.add(roll);
		diceRoll.add(number);
		diceRoll.setBorder(new TitledBorder (new EtchedBorder(), "Die"));
		return diceRoll;
	}

	/**
	 * createGuessPanel - creates the guess panel for the GUI
	 * @return guessPanel
	 */
	private JPanel createGuessPanel() {
		JPanel guessPanel = new JPanel();
		JLabel guess = new JLabel("Guess");
		playerGuess = new JTextArea(1, 30);
		playerGuess.setEditable(false);
		guessPanel.add(guess);
		guessPanel.add(playerGuess);
		guessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		return guessPanel;
	}

	/**
	 * createGuessResult - creates the guess result for the GUI
	 * @return guessResult
	 */
	private JPanel createGuessResult() {
		JPanel guessResult = new JPanel();
		JLabel response = new JLabel("Response");
		result = new JTextArea(1, 8);
		result.setEditable(false);
		guessResult.add(response);
		guessResult.add(result);
		guessResult.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		return guessResult;
	}

	void setPlayerTurn(String player) {
		playerTurn.setText(player);
	}

	public void setDiceRoll(String num) {
		number.setText(num);
	}

	public void setPlayerGuess(String guess) {
		playerGuess.setText(guess);
	}

	public void setResult(String res) {
		result.setText(res);
	}

	/**
	 * actionPerformed - makes the GUI interactive whenever an action is performed
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextPlayer) {
			if (!turnOver) {
				String message = "The Current Turn is Not Yet Finished \nPlease Wait For This Turn to End Before Pressing \"Next Turn\"";

				JOptionPane.showMessageDialog(new JFrame(), message, "Dialog", JOptionPane.ERROR_MESSAGE);
				return;
			}

			turnPlayer = gameboard.changePlayerTurn();
			turnOver = turnPlayer.makeMove(this);
		} else if (e.getSource() == accusation) {
			if (gameboard.getCurrentPlayer() instanceof HumanPlayer && !turnOver) {
				//display the accusation window for the human player
				HumanPlayer human = (HumanPlayer) gameboard.getCurrentPlayer();
				
				boolean accused = human.makeAHumanAccusation();
				
				if (!accused) {
					turnPlayer = gameboard.changePlayerTurn();
					turnOver = turnPlayer.makeMove(this);
				}
				return;
			} else {
				String message = "It is not the beginning of the Human's Turn\nPlease do not click the \"Make Accusation\" button until then";
				JOptionPane.showMessageDialog(new JFrame(), message, "Dialog", JOptionPane.ERROR_MESSAGE);
			}
		}
	} 

	/**
	 * mouseClicked - controls what happens when a valid target is clicked for the human player
	 * @param e
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!turnOver) {
			if (turnPlayer instanceof HumanPlayer) {
				//the human has selected a target, run humanTargetSelect with the position
				turnOver = turnPlayer.humanTargetSelect(e.getY(), e.getX(), this);
			}
		}
		gameboard.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
