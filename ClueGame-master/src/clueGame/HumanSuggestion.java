package clueGame;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * HumanSuggestion - class that deals with human player's suggestions
 * @author sethasadi dquintana
 *
 */
public class HumanSuggestion extends JDialog {
	JComboBox<String> weapon;
	JComboBox<String> person;
	JButton submit;
	JButton cancel;
	JTextArea room;
	Board gameboard;

	/**
	 * default constructor that is called whenever a suggestion is possible
	 */
	public HumanSuggestion() {
		//set up the dialog box
		setTitle("Make a Suggestion");
		setSize(400, 250);
		setLayout(new GridLayout(4, 1));
		setModal(true);
		
		setLocation(300, 300);

		weapon = new JComboBox<String>();
		person = new JComboBox<String>();
		
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		
		submit.addActionListener(new ButtonListener());
		cancel.addActionListener(new ButtonListener());

		gameboard = Board.getInstance();
		for (Card card : gameboard.getCards()) {
			if (card.getCardType() == CardType.WEAPON) {
				weapon.addItem(card.getCardName());
			} else if (card.getCardType() == CardType.PERSON) {
				person.addItem(card.getCardName());
			}
		}

		//add everything to the dialog box
		add(makeRoomPanel());
		add(makePersonPanel());
		add(makeWeaponPanel());
		add(makeButtonPanel());
	}
	
	/**
	 * ButtonListener - class that deals with a human player's submitted suggestion
	 * @author sethasadi dquintana
	 *
	 */
	private class ButtonListener implements ActionListener {
		/**
		 * actionPerformed - when human player clicks submit, will create the suggestion he/she made
		 * if the human player selects cancel, will dispose of the suggestion
		 * @param e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//if player clicks submit, will create the suggestion
			if (e.getSource() == submit) {
				Card roomCard = new Card();
				Card personCard = new Card();
				Card weaponCard = new Card();
				
				for (Card card : gameboard.getCards()) {
					if (card.getCardName().equals(room.getText())) {
						roomCard = card;
					} else if (card.getCardName().equals(person.getSelectedItem().toString())) {
						personCard = card;
					} else if (card.getCardName().equals(weapon.getSelectedItem().toString())) {
						weaponCard = card;
					}
				}
				
				HumanPlayer player = (HumanPlayer) gameboard.getCurrentPlayer();
				player.makeThisSuggestion(new Solution(personCard, roomCard, weaponCard));
				dispose();
			} else if (e.getSource() == cancel) {
				//else, disposes of the suggestion
				dispose();
			}
			
		}
	}

	/**
	 * makeRoomPanel - creates panel for the human player's room guess 
	 * @return roomPanel
	 */
	private JPanel makeRoomPanel() {
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(1, 2));
		JLabel roomGuess = new JLabel("Your room");
		room = new JTextArea();
		room.setEditable(false);
		room.setAlignmentY(CENTER_ALIGNMENT);
		
		room.setText(gameboard.getLegend().get(gameboard.getCellAt(gameboard.getCurrentPlayer().getRow(), gameboard.getCurrentPlayer().getColumn()).getInitial()));
		
		roomPanel.add(roomGuess);
		roomPanel.add(room);
		return roomPanel;
	}

	/**
	 * makePersonPanel - creates panel for the human player's person guess 
	 * @return personPanel
	 */
	private JPanel makePersonPanel() {
		JPanel personPanel = new JPanel();
		personPanel.setLayout(new GridLayout(1, 2));
		JLabel personGuess = new JLabel("Person");
		
		personPanel.add(personGuess);
		personPanel.add(person);
		return personPanel;
	}

	/**
	 * makeWeaponPanel - creates panel for the human player's weapon guess 
	 * @return weaponPanel
	 */
	private JPanel makeWeaponPanel() {
		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(1, 2));
		JLabel weaponGuess = new JLabel("Weapon");
		
		weaponPanel.add(weaponGuess);
		weaponPanel.add(weapon);
		return weaponPanel;
	}

	/**
	 * makeButtonPanel - creates button to either submit a suggestion or cancel it
	 * @return buttonPanel
	 */
	private JPanel makeButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		return buttonPanel;
	}
}
