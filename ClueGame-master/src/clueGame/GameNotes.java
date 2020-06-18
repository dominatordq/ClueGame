package clueGame;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * GameNotes - interactive GUI that keeps track of suggested/accused cards
 * @author sethasadi dquintana
 *
 */
public class GameNotes extends JDialog {
	//instance variable that can be updated
	private JComboBox<String> weapon;
	private JComboBox<String> room;
	private JComboBox<String> person;
	
	private JPanel roomCheckBoxPanel;
	private JPanel weaponCheckBoxPanel;
	private JPanel peopleCheckBoxPanel;
	
	/**
	 * default constructor that sets up the Game Notes
	 */
	public GameNotes() {
		setTitle("Game Notes");
		setSize(700, 800);
		setLayout(new GridLayout(3, 2));
		
		weapon = new JComboBox<String>();
		room = new JComboBox<String>();
		person = new JComboBox<String>();
		
		roomCheckBoxPanel = createRoomPanel();
		weaponCheckBoxPanel = createWeaponPanel();
		peopleCheckBoxPanel = createPeoplePanel();

		//add everything to the dialog box
		add(peopleCheckBoxPanel);
		add(createPersonGuessPanel());
		add(roomCheckBoxPanel);
		add(createRoomGuessPanel());
		add(weaponCheckBoxPanel);
		add(createWeaponGuessPanel());
	}
	
	/**
	 * createWeaponGuessPanel - create a panel to show weapon guesses
	 * @return JPanel
	 */
	private JPanel createWeaponGuessPanel() {
		JPanel weaponGuessPanel = new JPanel();
		weaponGuessPanel.setLayout(new GridLayout(1, 2));
		
		JLabel test = new JLabel();

		weaponGuessPanel.add(weapon);
		weaponGuessPanel.add(test);

		weaponGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
		return weaponGuessPanel;
	}

	/**
	 * createWeaponPanel - create a panel to show weapons seen
	 * @return JPanel
	 */
	private JPanel createWeaponPanel() {
		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(0, 2));

		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		return weaponPanel;
	}

	/**
	 * createRoomGuessPanel - create a panel to show room guesses
	 * @return JPanel
	 */
	private JPanel createRoomGuessPanel() {
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setLayout(new GridLayout(1, 2));
		JLabel test = new JLabel();

		roomGuessPanel.add(room);
		roomGuessPanel.add(test);

		roomGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
		return roomGuessPanel;
	}

	/**
	 * createRoomPanel - create a panel to show rooms seen
	 * @return JPanel
	 */
	private JPanel createRoomPanel() {
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(0, 2));

		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		return roomPanel;
	}

	/**
	 * createPersonGuessPanel - create a panel to show person guesses
	 * @return JPanel
	 */
	private JPanel createPersonGuessPanel() {
		JPanel personGuessPanel = new JPanel();
		personGuessPanel.setLayout(new GridLayout(1, 2));
		JLabel test = new JLabel();
		
		personGuessPanel.add(person);
		personGuessPanel.add(test);

		personGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));

		return personGuessPanel;
	}

	/**
	 * createPeoplePanel - create a panel to show people seen
	 * @return JPanel
	 */
	private JPanel createPeoplePanel() {
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(0, 2));

		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		return peoplePanel;
	}
	
	//functions to add weapons, rooms, and people for modular programming
	public void addWeapon(String weaponName) {
		//add a weapon to the panel
		weapon.addItem(weaponName);
		weaponCheckBoxPanel.add(new JCheckBox(weaponName));
	}
	
	public void addPerson(String personName) {
		//add a person to the panel
		person.addItem(personName);
		peopleCheckBoxPanel.add(new JCheckBox(personName));
	}

	public void addRoom(String roomName) {
		//add a room to the panel
		room.addItem(roomName);
		roomCheckBoxPanel.add(new JCheckBox(roomName));
	}


}
