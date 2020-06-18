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
 * HumanAccusation - class that deals with a human player's accusation 
 * @author sethasadi dquintana
 *
 */
public class HumanAccusation extends JDialog {
	JComboBox<String> weapon;
	JComboBox<String> person;
	JComboBox<String> room;
	JButton submit;
	JButton cancel;
	Board gameboard;

	/**
	 * default constructor that sets up the box for when a human player makes an accusation
	 */
	public HumanAccusation() {
		//set up the dialog box
		setTitle("Make an Accusation");
		setSize(400, 250);
		setLayout(new GridLayout(4, 1));
		setModal(true);
		
		setLocation(300, 300);

		weapon = new JComboBox<String>();
		person = new JComboBox<String>();
		room = new JComboBox<String>();
		
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
			} else {
				room.addItem(card.getCardName());
			}
		}

		//add everything to the dialog box
		add(makeRoomPanel());
		add(makePersonPanel());
		add(makeWeaponPanel());
		add(makeButtonPanel());
	}
	
	/**
	 * ButtonListener - class that deals with a human player's submitted accusation
	 * @author sethasadi dquintana
	 *
	 */
	private class ButtonListener implements ActionListener {
		/**
		 * actionPerformed - when a human player clicks submit, will submit the accusation
		 * if the human player selects cancel, will dispose of the accusation
		 * @param e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == submit) {
				Card roomCard = new Card();
				Card personCard = new Card();
				Card weaponCard = new Card();
				
				for (Card card : gameboard.getCards()) {
					if (card.getCardName().equals(room.getSelectedItem().toString())) {
						roomCard = card;
					} else if (card.getCardName().equals(person.getSelectedItem().toString())) {
						personCard = card;
					} else if (card.getCardName().equals(weapon.getSelectedItem().toString())) {
						weaponCard = card;
					}
				}
				
				HumanPlayer player = (HumanPlayer) gameboard.getCurrentPlayer();
				player.makeThisAccusation(new Solution(personCard, roomCard, weaponCard));
				dispose();
			} else if (e.getSource() == cancel) {
				dispose();
			}
			
		}
	}

	/**
	 * makeRoomPanel - creates the panel to select a room
	 * @return roomPanel
	 */
	private JPanel makeRoomPanel() {
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(1, 2));
		JLabel roomGuess = new JLabel("Room");
		
		roomPanel.add(roomGuess);
		roomPanel.add(room);
		return roomPanel;
	}

	/**
	 * makePersonPanel - creates the panel to select a person
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
	 * makeWeaponPanel - creates the panel to select a weapon
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
	 * creates the "Submit" and "Cancel" buttons on the accusation panel
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
