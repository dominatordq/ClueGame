package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;

import javax.swing.JPanel;

import clueGame.BoardCell;

/**
 * Board - Class to store all the functionality of the gameboard
 * @author sethasadi, dquintana
 *
 */
public class Board extends JPanel {
	//instance variables for rows and columns
	private int numRows;
	private int numColumns;

	final static int MAX_BOARD_SIZE = 50;

	//these two were initially initialized in initialize until they needed to be moved for JUnit Exception Tests
	private BoardCell[][] board;
	private Map<Character, String> legend;

	private Map<BoardCell, Set<BoardCell>> adjCells;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	//files to use for configuring board
	private String boardConfigFile;
	private String roomConfigFile;

	//files to use for setting up players and cards
	private String playerConfigFile;
	private String cardConfigfile;
	
	//int to track whose turn it is
	int currentTurn = -1;

	//ArrayList of the players in the game
	ArrayList<Player> players;

	//ArrayList of the cards in the game
	ArrayList<Card> cards;

	//object of type Solution that holds theAnswer
	private Solution theAnswer;

	//boolean to keep track of calcTargets recursive calls
	private boolean newCallToCalcTargets = true;

	// static instance of board
	private static Board theInstance = new Board();
	
	private boolean doorwayStart = false;
	private char doorwayInitial = ' ';

	// constructor is private to ensure only one can be created
	private Board() {}

	/**
	 * getInstance - returns the only board in memory
	 * @return theInstance
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/**
	 * initialize - sets up the board and the rooms, and sets up adjacency cells and visited cells.
	 * calcAdjacencies() is called to set up adjacencies for each cell on the board
	 */
	public void initialize() {
		try {
			//initialize legend and the board
			loadRoomConfig();
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			e.printStackTrace();
		}

		adjCells = new HashMap<BoardCell, Set<BoardCell>>();
		visited = new HashSet<BoardCell>();
		theAnswer = new Solution();
		calcAdjacencies();
	}

	/**
	 * loadRoomConfig - function to load in the legend file and transfer it into the legend
	 * @throws BadConfigFormatException
	 */
	// **still needs to handle cards/other third indicator**
	public void loadRoomConfig() throws BadConfigFormatException {
		//initialize the board space, the legend space, and numRows and numColumns
		// even though these would be better in the initialize function they need
		// to be here to accommodate the JUnit Exception testing where initialize is 
		// never actually called
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		legend = new HashMap<Character, String>();
		numRows = 0;
		numColumns = 0;
		targets = new HashSet<BoardCell>();
		//try to load the legend file
		try {
			//load the legend file
			FileReader reader = new FileReader(roomConfigFile);
			Scanner in = new Scanner(reader);

			//read through the entire config file
			while (in.hasNextLine()) {
				//read in the next line
				String line = in.nextLine();

				//split every entry separated by a common into an array of Strings
				String [] parts = line.split(", ");

				//place the character at the beginning as the map key and the second part as the map value
				legend.put(parts[0].charAt(0), parts[1]);

				if (!parts[2].equals("Card") && !parts[2].equals("Other")) {
					throw new BadConfigFormatException("The " + parts[1] + " does not have a valid identifier (Card/Other)");
				}
			}
		} catch (FileNotFoundException e) {
			//if file did not load, print the exception message
			System.out.println(e.getMessage());
		}
	}

	/**
	 * loadBoardConfig - loads the board file and reads through it, putting it into an array
	 * @throws BadConfigFormatException
	 */
	public void loadBoardConfig() throws BadConfigFormatException {
		//try to load the board file
		try {
			int num = 1;
			//load the board file
			FileReader reader = new FileReader(boardConfigFile);
			Scanner in = new Scanner(reader);

			//create size to capture number of columns
			int currentRowsNumOfColumns = 0;

			//create currentRow to represent the current row being read
			int currentRow = 0;

			//read through the entire config file
			while (in.hasNextLine()) {
				//read in the next line
				String line = in.nextLine();

				//split every room separated by a common into an array of Strings
				String [] cellCharacterCodes = line.split(",");
				currentRowsNumOfColumns = cellCharacterCodes.length;

				//assign numColumns with the length of the String array unless it varies from the previous numColumns
				if (currentRowsNumOfColumns != numColumns && numColumns != 0) {
					throw new BadConfigFormatException("Row " + currentRow + " does not have the same amount of columns as the rest of the rows");
				} else {
					numColumns = currentRowsNumOfColumns;
				}

				//iterate through the entire row to populate the board with BoardCells
				// with the initial found in the config file
				for (int currentColumn = 0; currentColumn < currentRowsNumOfColumns; ++currentColumn) {
					//if the character in the cell does not correlate to a room in the legend, throw a BadConfigFormatException
					if (!legend.containsKey(cellCharacterCodes[currentColumn].charAt(0))) {
						throw new BadConfigFormatException("The cell at row " + currentRow + " column " + currentColumn + "referenced a room that does not exist");
					}
					board[currentRow][currentColumn] = new BoardCell(currentRow, currentColumn, cellCharacterCodes[currentColumn].charAt(0));

					//check if the cell is a doorway
					if (cellCharacterCodes[currentColumn].length() == 2) {
						//if the cell is a doorway, check its direction and assign it the correct DoorDirection
						switch (cellCharacterCodes[currentColumn].charAt(1)) {
						case 'U':
							board[currentRow][currentColumn].setDoorDirection(DoorDirection.UP);
							break;
						case 'D':
							board[currentRow][currentColumn].setDoorDirection(DoorDirection.DOWN);
							break;
						case 'L':
							board[currentRow][currentColumn].setDoorDirection(DoorDirection.LEFT);
							break;
						case 'R':
							board[currentRow][currentColumn].setDoorDirection(DoorDirection.RIGHT);
							break;
						case 'T':
							board[currentRow][currentColumn].setToTitleCell();
							break;
						case 'P':
							board[currentRow][currentColumn].holdAPerson();
							board[currentRow][currentColumn].setPersonHolderNum(num);
							num++;
							break;
						}
					}
				}
				currentRow++;
			}

			//assign numRows with the amount of times a row was read
			numRows = currentRow;
		} catch (FileNotFoundException e) {
			//if file did not load, print the exception message
			System.out.println(e.getMessage());
		}
	}
	/**
	 * loadPlayers - loads the players into the players ArrayList
	 */
	public void loadPlayers() {
		players = new ArrayList<Player>();
		try {
			//open a reader and scanner for the player file
			FileReader reader = new FileReader(playerConfigFile);
			Scanner in = new Scanner(reader);

			//do this while there are still players to be read in
			while (in.hasNextLine()) {
				//read in the line
				String playerLine = in.nextLine();

				//separate the line into individual Strings
				String [] playerParts = playerLine.split(", ");

				//create a new Player to fill with details from this line
				Player currentPlayer;

				//if the last value of playerParts is "C", make it a ComputerPlayer
				//if it is "H", make it a HumanPlayer
				if (playerParts[6].charAt(0) == 'C') {
					currentPlayer = new ComputerPlayer();
				} else if (playerParts[6].charAt(0) == 'H') {
					currentPlayer = new HumanPlayer();
				} else {
					System.out.println("Failed to determine what type of player was indicated, setting to ComputerPlayer");
					currentPlayer = new ComputerPlayer();
				}

				//set the player's name with the first value in playerParts
				currentPlayer.setName(playerParts[0]);

				//create a Color object with the 2nd through 4th values in playerParts parsed into ints
				int redValue = Integer.parseInt(playerParts[1]);
				int greenValue = Integer.parseInt(playerParts[2]);
				int blueValue = Integer.parseInt(playerParts[3]);
				Color playerColor = new Color(redValue, greenValue, blueValue);

				//assign that Color object to the player
				currentPlayer.setColor(playerColor);

				//assign the player's starting row and column with parsed ints from the 5th and 6th values in playerParts
				int startRow = Integer.parseInt(playerParts[4]);
				int startCol = Integer.parseInt(playerParts[5]);
				currentPlayer.setRow(startRow);
				currentPlayer.setColumn(startCol);

				players.add(currentPlayer);
			}
		} catch (FileNotFoundException e) {
			//if file did not load, print the exception message
			System.out.println(e.getMessage());
		}
	}

	/**
	 * loadCards - loads the cards from the given file into the ArrayList cards
	 */
	public void loadCards() {
		//initialize cards
		cards = new ArrayList<Card>();

		//load the cards into cards
		try {
			//setup file reader and scanner
			FileReader reader = new FileReader(cardConfigfile);
			Scanner in = new Scanner(reader);

			//initialize an int to know which type of cards are being read in
			int cardsBeingRead = 0;

			while (in.hasNextLine()) {
				//read in the next line of cards
				String cardLine = in.nextLine();

				//split cardLine into each card
				String [] currentCards = cardLine.split(", ");

				//create a CardType based on which row is being read
				CardType cardsOnRow;
				if (cardsBeingRead == 0) {
					cardsOnRow = CardType.ROOM;
				} else if (cardsBeingRead == 1) {
					cardsOnRow = CardType.PERSON;
				} else if (cardsBeingRead == 2) {
					cardsOnRow = CardType.WEAPON;
				} else {
					System.out.println("Error in determing card type, setting to WEAPON");
					cardsOnRow = CardType.WEAPON;
				}

				//create cards for each card on the current line
				for (int i = 0; i < currentCards.length; ++i) {
					Card currentCardOnLine = new Card(currentCards[i], cardsOnRow);
					cards.add(currentCardOnLine);
				}

				cardsBeingRead++;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * generateAnswerCard - will randomly generate the winning set of cards
	 * @param type
	 * @param randomCard
	 * @return cardForSolution
	 */
	public int generateAnswerCard(CardType type, Random randomCard) {
		//randomly select card of type type
		int cardForSolution = randomCard.nextInt(cards.size());
		//search for a card
		while (cards.get(cardForSolution).getCardType() != type) {
			cardForSolution = randomCard.nextInt(cards.size());
		}
		
		return cardForSolution;
	}

	/**
	 * dealCards - deal the cards out to each player
	 */
	public void dealCards() {
		//generate an array of booleans the size of the amount of cards
		boolean [] cardsDealt = new boolean[cards.size()];

		//create an instance of Random to use for randomly choosing cards to deal
		Random randomCard = new Random();

		//select a solution that has 1 weapon, 1 person, and 1 room
		int forSolution;
		//randomly select weapon
		forSolution = generateAnswerCard(CardType.WEAPON, randomCard);
		theAnswer.weapon = cards.get(forSolution);
		cardsDealt[forSolution] = true;

		//randomly select person
		forSolution = generateAnswerCard(CardType.PERSON, randomCard);
		theAnswer.person = cards.get(forSolution);
		cardsDealt[forSolution] = true;
		
		//randomly select room
		forSolution = generateAnswerCard(CardType.ROOM, randomCard);
		theAnswer.room = cards.get(forSolution);
		cardsDealt[forSolution] = true;
				
		for (int i = 0; i < cards.size() - 3; i++) {
			//generate a random number within the card amount
			int cardToDeal = randomCard.nextInt(cards.size());

			//keep regenerating that random number until one is generated that has not been dealt yet
			while (cardsDealt[cardToDeal] == true) {
				cardToDeal = randomCard.nextInt(cards.size());
			}

			//deal the card at cardToDeal to the next player
			//determine the next player based on the for-loop i and modding it by the number of players
			players.get(i % players.size()).addPlayerCard(cards.get(cardToDeal));

			//set the value in cardsDealt at cardToDeal to true to represent that it has been dealt
			cardsDealt[cardToDeal] = true;
		}
	}

	/**
	 * calcAdjacencies - calculates the adjacencies of cells by finding nearby cells of a 
	 * current cell and checking if they are adjacent to it, and adds both of them to the 
	 * adjCells map if it is true
	 */
	public void calcAdjacencies() {
		// iterate over entire board
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				//initialize a set to store the adjacent cells
				Set<BoardCell> nearbyCells = new HashSet<BoardCell>();

				//first test to ensure the space is a walkway space
				//if it is not, test to see if it is a doorway
				//if it is not that either, it is a room space, and nothing needs to be done
				if (board[i][j].isWalkway()) {
					//create 4 if statements to add all the possible adjacent cells if they exist and are walkways or proper doorways
					if (i - 1 >= 0) {
						if (board[i - 1][j].isWalkway()) {
							nearbyCells.add(board[i - 1][j]);
						} else if (board[i - 1][j].isDoorway()) {
							if (board[i - 1][j].getDoorDirection() == DoorDirection.DOWN) {
								nearbyCells.add(board[i - 1][j]);
							}
						}
					}
					if (i + 1 < numRows) {
						if (board[i + 1][j].isWalkway()) {
							nearbyCells.add(board[i + 1][j]);
						} else if (board[i + 1][j].isDoorway()) {
							if (board[i + 1][j].getDoorDirection() == DoorDirection.UP) {
								nearbyCells.add(board[i + 1][j]);
							}
						}
					}
					if (j - 1 >= 0) {
						if (board[i][j - 1].isWalkway()) {
							nearbyCells.add(board[i][j - 1]);
						} else if (board[i][j - 1].isDoorway()) {
							if (board[i][j - 1].getDoorDirection() == DoorDirection.RIGHT) {
								nearbyCells.add(board[i][j - 1]);
							}
						}
					}
					if (j + 1 < numColumns) {
						if (board[i][j + 1].isWalkway()) {
							nearbyCells.add(board[i][j + 1]);
						} else if (board[i][j + 1].isDoorway()) {
							if (board[i][j + 1].getDoorDirection() == DoorDirection.LEFT) {
								nearbyCells.add(board[i][j + 1]);
							}
						}
					}
				} else if (board[i][j].isDoorway()) { //else add the single adjacent cell for a doorway and all the other doorways in this room
					for (int k = 0; k < numRows; k++) {
						for (int l = 0; l < numColumns; l++) {
							if (board[k][l].isDoorway() && board[k][l].getInitial() == board[i][j].getInitial()) {
								//this means we have found a cell that is in the same room and is a door
								//switch statement to check DoorDirection and add the correct BoardCell
								switch(board[k][l].getDoorDirection()) {
								case UP: 
									nearbyCells.add(board[k - 1][l]);
									break;
								case DOWN:
									nearbyCells.add(board[k + 1][l]);
									break;
								case LEFT:
									nearbyCells.add(board[k][l - 1]);
									break;
								case RIGHT:
									nearbyCells.add(board[k][l + 1]);
									break;
								default:
									break;
								}
							}
						}
					}
				}

				//add an entry to the adjCells Map with board[i][j] and the formed set
				adjCells.put(board[i][j], nearbyCells);
			}
		}
	}

	/**
	 * calcTargets - calculates the target cells for each cell on the board
	 * @param row
	 * @param col
	 * @param pathLength
	 */
	public void calcTargets(int row, int col, int pathLength) {
		//first check to see if this is a new call to calcTargets and not a recursive call
		if (newCallToCalcTargets) {
			targets.clear();
			if (board[row][col].isDoorway()) {
				doorwayStart = true;
				doorwayInitial = board[row][col].getInitial();
			}
			newCallToCalcTargets = false;
		}

		//generate the targets for board[row][col] with pathLength
		BoardCell startCell = board[row][col];
		Set<BoardCell> adjs = adjCells.get(startCell);
		visited.add(startCell);
		for (BoardCell cell : adjs) {
			if (visited.contains(cell)) {
				continue;
			} else if (cell.isDoorway() && cell.getInitial() == doorwayInitial) {
				continue;
			} else {
				if (pathLength == 1) {
					boolean otherPlayer = false;
					for (Player player : players) {
						if (player.getRow() == cell.getBoardRow() && player.getColumn() == cell.getBoardCol() && !cell.isDoorway()) {
							otherPlayer = true;
						}
					}
					if (!otherPlayer) {
						targets.add(cell);
					}
				} else if (cell.isDoorway()) {
					targets.add(cell);
				} else {
					calcTargets(cell.getBoardRow(), cell.getBoardCol(), pathLength - 1);
				}
			}
		}
		visited.remove(startCell);
		if (visited.isEmpty()) {
			newCallToCalcTargets = true;
			doorwayStart = false;
			doorwayInitial = ' ';
		}

	}

	/**
	 * sets the config files for the board and room
	 * @param boardConfig
	 * @param roomConfig
	 */
	public void setConfigFiles(String boardConfig, String roomConfig) {
		boardConfigFile = boardConfig;
		roomConfigFile = roomConfig;
	}

	/**
	 * sets the game setup config files for the players and the cards
	 * @param playerConfig
	 * @param cardConfig
	 */
	public void setGameSetupConfigFiles(String playerConfig, String cardConfig) {
		playerConfigFile = playerConfig;
		cardConfigfile = cardConfig;
	}

	/**
	 * gets the adjacency list for each cell on the board 
	 * @param row
	 * @param col
	 * @return adjCells.get(board[row][col])
	 */
	public Set<BoardCell> getAdjList(int row, int col) {
		//return the adjList at board[row][col]
		return adjCells.get(board[row][col]);
	}

	/**
	 * returns the cell at a certain row and column on the board
	 * @param row
	 * @param col
	 * @return board[row][col]
	 */
	public BoardCell getCellAt(int row, int col) {
		//return the BoardCell at the given position
		return board[row][col];
	}

	public Map<Character, String> getLegend() {
		return legend;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void setAnswer(Card person, Card room, Card weapon) {
		theAnswer.person = person;
		theAnswer.room = room;
		theAnswer.weapon = weapon;
	}
	
	public Solution getAnswer() {
		return theAnswer;
	}

	public Card handleSuggestion(Solution suggestion, String accuser) {
		//figure out where the accuser lies in the arraylist of players
		int accuserIndex = 0;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerName().equals(accuser)) {
				accuserIndex = i;
			}
		}
		
		Card toDisprove = new Card();
		//iterate over every player and check if that player can disprove the suggestion
		for (int i = accuserIndex + 1; i < players.size() + accuserIndex; i++) {
			toDisprove = players.get(i % players.size()).disproveSuggestion(suggestion);
			//if the player does not return null (they have one of the cards) kick out and return that card
			if (toDisprove != null) {
				break;
			}
		}
		
		//return the found card (or null if no card is found)
		return toDisprove;
	}
	
	//paint the board by cells and paint the players
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				board[i][j].draw(g);
			}
		}
		for (Player player : players) {
			player.draw(g);
		}
		
	}
	
	public Player changePlayerTurn() {
		currentTurn++;
		currentTurn = currentTurn % 6;
		
		return players.get(currentTurn); 
	}
	
	public void tellPlayersNonDisprovedSuggestion(Solution accusation) {
		for (Player player : players) {
			//only tell the player if the player is a computer, because the human should already know
			if (!(player instanceof HumanPlayer)) {
				player.checkThisSupposedAccusation(accusation);
			}
		}
	}
	
	public void tellPlayersDisprovenCard(Card disproven) {
		for (Player player : players) {
			player.addSeenCard(disproven);
		}
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentTurn);
	}
}
