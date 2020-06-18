/**
 * 
 */
package clueGame;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * SideBar - GUI for the side bar of Clue Game
 * @author sethasadi, dquintana
 */
public class SideBar extends JPanel {
	//instance variables
	private JTextArea rooms;
	private JTextArea weapons;
	private JTextArea people;
	
	/**
	 * default constructor that sets up the side bar layout
	 */
	public SideBar() {
		setLayout(new GridLayout(3, 1));
		
		//center text in the text boxes
		weapons = new JTextArea("\n\n", 7, 13);
		rooms = new JTextArea("\n\n", 7, 13);
		people = new JTextArea("\n\n", 7, 13);
		
		//make them non editable
		weapons.setEditable(false);
		rooms.setEditable(false);
		people.setEditable(false);
		
		add(createPeoplePanel());
		add(createRoomPanel());
		add(createWeaponPanel());
		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		
	}
	
	/**
	 * createPeoplePanel - create the JPanel to show cards that are people
	 * @return JPanel
	 */
	private JPanel createPeoplePanel() {
		JPanel peoplePanel = new JPanel();
		//create JPanel turnPanel and createButtonPanel()
		peoplePanel.add(people);
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		return peoplePanel;
	}

	/**
	 * createRoomPanel - create the JPanel to show cards that are rooms
	 * @return JPanel
	 */
	private JPanel createRoomPanel() {
		JPanel roomPanel = new JPanel();
		roomPanel.add(rooms);
		roomPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		return roomPanel;
	}

	/**
	 * createWeaponPanel - create the JPanel to show cards that are weapons
	 * @return JPanel
	 */
	private JPanel createWeaponPanel() {
		JPanel weaponPanel = new JPanel();
		weaponPanel.add(weapons);
		weaponPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		return weaponPanel;
	}
	
	/**
	 * loadPlayerCards - take in a list of cards and separate them accordingly
	 * @param cards
	 */
	public void loadPlayerCards(ArrayList<Card> cards) {
		for (Card card : cards) {
			switch (card.getCardType()) {
			case ROOM:
				//add the position for a room card in the room sidebar
				rooms.setText(rooms.getText() + "\n" + card.getCardName());
				break;
			case WEAPON:
				//add the position for a room card in the weapon sidebar
				weapons.setText(weapons.getText() + "\n" + card.getCardName());
				break;
			case PERSON:
				//add the position for a room card in the person sidebar
				people.setText(people.getText() + "\n" + card.getCardName());
				break;
			}
		}
	}
}
