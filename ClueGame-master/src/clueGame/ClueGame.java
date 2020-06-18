
package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.BeforeClass;

/**
 * @author sethasadi, dquintana
 *
 */
public class ClueGame extends JFrame {
	//private instance variable for the board and custom dialog box
	private Board gameboard;
	private GameNotes gameNotes;
	private SideBar sideBar;

	public ClueGame() {
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
		
		gameNotes = new GameNotes();
		
		//load the notes with all the game values to be modular
		for (Card card : gameboard.getCards()) {
			switch (card.getCardType()) {
			case ROOM:
				//add the position for a room card in the gameNotes dialog box
				gameNotes.addRoom(card.getCardName());
				break;
			case WEAPON:
				//add the position for a room card in the gameNotes dialog box
				gameNotes.addWeapon(card.getCardName());
				break;
			case PERSON:
				//add the position for a room card in the gameNotes dialog box
				gameNotes.addPerson(card.getCardName());
				break;
			}
		}
		
		//get the human player by searching through the players to allow for future feature of human choosing their character
		Player human = new Player();
		
		for (Player player : gameboard.getPlayers()) {
			if (player instanceof HumanPlayer) {
				human = player;
			}
		}
		
		JOptionPane.showMessageDialog(this, "You are " + human.getPlayerName() + ", press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		
		sideBar = new SideBar();
		
		setSideBarCards(gameboard.getPlayers().get(0).getPlayerCards());
		
		GUI_ClueControl controlBar = new GUI_ClueControl();
		
		gameboard.addMouseListener(controlBar);
		
		controlBar.setPlayerTurn(human.getPlayerName());
		
		//set the general details for a JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(950, 870);
		
		//add the board, the controls on the bottom and the players cards on the right
		add(Board.getInstance(), BorderLayout.CENTER);
		add(controlBar, BorderLayout.SOUTH);
		add(sideBar, BorderLayout.EAST);

		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		menu.add(createMenuBar());
		
	}

	private void setSideBarCards(ArrayList<Card> cards) {
		// TODO Auto-generated method stub
		sideBar.loadPlayerCards(cards);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new ClueGame();

		//frame.repaint();
		frame.setVisible(true);
	}

	/**
	 * createMenuBar - creates the menu bar that has exit and notes
	 * @return
	 */
	private JMenu createMenuBar() {
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem gameNotesItem = new JMenuItem("Notes");
		
		//exit when the exit menu is selected
		class ExitItemListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		
		//pull up the user's notes when the Notes menu is selected
		class GameNotesListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameNotes.setVisible(true);
			}
		}
		
		exitItem.addActionListener(new ExitItemListener());
		gameNotesItem.addActionListener(new GameNotesListener());
		
		menu.add(exitItem);
		menu.add(gameNotesItem);

		return menu;
	}

}
