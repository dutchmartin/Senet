//@Author Martijn Groeneveldt
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Scanner;

public class Senet {
	private Board board;
	private Dice dice;
	private Player[] player;
	private Scanner input;
	private int playerSize = 2;
	private char validValue[] = { '.', 'x', 'o' };// allowed chars in the actual playing field
	private char emptyField = validValue[0];
	private char[] playerChars = { validValue[1], validValue[2] };
	private int startNumber = 1; // the number to throw to start first with the game
	private int[] doubleTurn = { 1, 4, 6 };
	private int fallingShaftPosition = 27;
	private int[] protectedPlaces = { 26, 28, 29 };
	private int boardSize = 30;
	private int boardRows = 3;
	private int endPosition;

	public Senet() {
		this.board = new Board(boardSize, boardRows);
		doConstructorTasks();
	}

	public Senet(Board test) {
		this.board = test;
		doConstructorTasks();
	}

	private void doConstructorTasks() {
		this.dice = new Dice();
		this.input = new Scanner(System.in);
		this.player = new Player[playerSize];
		this.endPosition = board.getBoardsize();
	}

	public void play() {
		// initialize game by setting up players
		for (int i = 0; i < playerSize; i++) {
			System.out.println("speler " + i + ", voer jouw naam in:");
			String name = input.nextLine();
			this.player[i] = new Player(name, playerChars[1]);
		}

		// decide who will start and setup the game that way
		int current = whoFirst(playerChars); // contains the current i of player[i] that has their set
		player[current].setColorsign(playerChars[0]);
		System.out.print(player[current].getName());
		System.out.print(" speelt als " + player[current].getColorsign() + " en begint eerst\n");
		current = switchPlayer(current);
		System.out.print(player[current].getName());
		System.out.print(" speelt als " + player[current].getColorsign() + " en zijn zet wordt voor hem gedaan\n");
		// setup the game board
		boolean whatChar = true; // begin with 'o'
		for (int i = 1; i <= board.getRowSize(); i++) {
			board.getSquare(i).setValue(playerChars[boolToInt(whatChar)]);
			whatChar = !whatChar;
		}
		// execute hardcoded first steps
		board.move(10, 11);
		board.move(9, 10);
		runGame(current);
	}

	public void testGame() {
		int current = 0;
		for (int i = 0; i < playerChars.length; i++) {
			this.player[i] = new Player("" + playerChars[i], playerChars[i]);
			// x moet beginnen
			if (this.player[i].getColorsign() == playerChars[0]) {
				current = switchPlayer(i);
			}
		}
		runGame(current);
	}

	private void runGame(int current) {
		current = switchPlayer(current);
		// start game loop
		nextturn: while (!hasSomeoneWon()) {
			// print board
			board.print();
			// inform player of the status
			System.out.print(player[current].getName() + " (");
			System.out.print(player[current].getColorsign());
			System.out.print(") is aan de beurt\n");
			int diceNumber = dice.throwSticks();
			System.out.println("Je hebt " + diceNumber + " gegooid!");
			if (canGoAgian(diceNumber)) {
				System.out.println("Omdat je " + diceNumber + " hebt gegooid mag je volgende beurt opnieuw");
			}
			System.out.println("Geef jouw actie door of typ h voor help");
			// main game turn loop
			game: while (!hasSomeoneWon()) {
				// Process input from user
				String userInput = input.nextLine();
				// decide what to do: exit, print help, or continue the game by setting a new
				// step on the board
				userInput = userInput.trim().toLowerCase();
				if (isValidNumber(userInput)) {
					int stonePlace = Integer.parseInt(userInput);
					// input number may not be greater than the number of fields
					int goTo = stonePlace + diceNumber;
					if (!(stonePlace > board.getBoardsize()) && !(goTo > board.getBoardsize())) {
						// check if the player even owns this stone, like you know, users can be stupid
						if (player[current].getColorsign() == board.getSquare(stonePlace).getValue()) {
							// check if player goes to end position
							if (goTo == endPosition) {
								if (hasAllStonesAtLastRow(player[current].getColorsign())) {
									// delete stone of board
									board.setBlank(stonePlace);
									current = switchPlayer(current);
									continue nextturn;
								} else {
									// Illegal move, try something else
									notifyMoveIsInvalid();
									continue game;
								}
							} else {
								// check for rule 3: the falling shaft
								if (goTo == fallingShaftPosition) {
									executeRule3(stonePlace);
									current = switchPlayer(current);
									continue nextturn;
								}
								// rule 3 does not apply, go further
								else {
									// check if the step is allowed by the game rules
									if (stepIsSuccesfull(stonePlace, diceNumber, current)) {
										// if the player has thrown the right number he may go again
										if (canGoAgian(diceNumber)) {
											continue nextturn;
										} else {
											current = switchPlayer(current);
											continue nextturn;
										}
									}
								}
							}
						}
					}
					notifyMoveIsInvalid();
					continue game;
				} else {
					switch (userInput) {
					case "h":
						printHelp();
						break;
					case "q":
						System.out.println(player[current].getName() + " geeft het op");
						current = switchPlayer(current);
						printHasWon(player[current].getName());
						return;
					case "s":
						System.out.println(player[current].getName() + " zijn beurt stopt");
						current = switchPlayer(current);
						break game;
					case "r":
						printRules();
						break;
					case "p":
						board.print();
						break;
					default:
						System.out
								.println(userInput + " is geen valide actie, geef jouw actie door of typ h voor help");
						break;
					}
				}
			}
		}
	}

	private boolean stepIsSuccesfull(int place, int steps, int currentPlayer) {
		int endPos = place + steps;
		int opponentKey = switchPlayer(currentPlayer);
		boolean rule6 = rule6(place, steps, currentPlayer);
		// rule 1: check if field is empty
		if (board.getSquare(endPos).getValue() == emptyField) {
			// field is empty
			// only rule 6 can still stop this
			if (rule6) {
				board.move(place, endPos);
			}
			return rule6;
		} else if (board.getSquare(endPos).getValue() == player[opponentKey].getColorsign()) {
			// rule 2 applies now: field contains stone of the opponent, but can attack it
			// rule 6 can make the move invalid
			// else : the stone attacks enemy stone and stones switch position if rule 5
			// does not apply
			// rule 5: two stones next to each other cannot be attacked
			// rule 7: stones on protected places cannot be attacked
			if (!positionIsProtected(endPos)) {
				if (rule6) {
					if (stoneCanBeAttacked(endPos, currentPlayer)) {
						board.reverse(place, endPos);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean hasSomeoneWon() {
		// per player check if he still has stones on the board
		playerloop: for (Player canidate : player) {
			for (int i = 1; i <= board.getBoardsize(); i++) {
				if (board.getSquare(i).getValue() == canidate.getColorsign()) {
					continue playerloop;
				}
				if (i == endPosition) {
					printHasWon(canidate.getName());
					return true;
				}
			}
		}
		return false;
	}

	private void executeRule3(int place) {
		boolean placed = false;
		// while not placed
		for (int i = 1; !placed; i++) {
			if (board.getSquare(i).getValue() == emptyField) {
				board.getSquare(i).setValue(board.getSquare(place).getValue());
				board.getSquare(place).setValue(emptyField);
				placed = true;
			}
		}
	}

	// rule5: return false if 2 or more stones of the enemy player cannot be
	// attacked
	private boolean stoneCanBeAttacked(int endPos, int currentPlayer) {
		char enemyColorSign = player[switchPlayer(currentPlayer)].getColorsign();
		// check if end position is owned by enemy player
		if (board.getSquare(endPos).getValue() == enemyColorSign) {
			// check if the stone has neighbors with enemy
			boolean checkminone = board.getSquare(endPos - 1).getValue() == enemyColorSign;
			boolean checkplusone = board.getSquare(endPos + 1).getValue() == enemyColorSign;
			if (checkminone || checkplusone) {
				return false;
			}
		}
		return true;
	}

	// rule6: return false if the enemy has 3 stones to block the player
	private boolean rule6(int place, int steps, int currentPlayer) {
		// a player can not jump over 3 stones of another player
		int jumpOverStones = 3;
		int endPos = place + steps;
		char enemyColorSign = player[switchPlayer(currentPlayer)].getColorsign();
		if (steps > jumpOverStones) {
			// validate if the enemy has 3 stones next to each other
			int occurances = 0;
			for (int i = place + 1; i < endPos; i++) {
				if (board.getSquare(i).getValue() == enemyColorSign) {
					occurances++;
				} else {
					occurances = 0;
				}
			}
			if (occurances >= jumpOverStones) {
				// rule 6 applies and move is illegal
				return false;
			}
		}
		// rule 6 does not apply
		return true;
	}

	// return true if the attacked position is protected
	private boolean positionIsProtected(int attackedPos) {
		for (int forbidden : protectedPlaces) {
			if (attackedPos == forbidden) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAllStonesAtLastRow(char colorSign) {
		int maxbound = board.getBoardsize() - board.getRowSize();
		// check if the player still has stones in the first 2 rows
		for (int i = 1; i <= maxbound; i++) {
			if (board.getSquare(i).getValue() == colorSign) {
				return false;
			}
		}
		return true;
	}

	private int whoFirst(char[] players) {
		// decide who is gonna start by looking who throws the startNumber first
		boolean thrownOne = false;
		int arrayNum = 0;
		while (!thrownOne) {
			int n = dice.throwSticks();
			if (n == startNumber) {
				thrownOne = !thrownOne;
			}
			arrayNum++;
			if (arrayNum >= playerSize) {
				arrayNum = 0;
			}
		}
		return arrayNum;
	}

	private int boolToInt(boolean bool) {
		return (bool) ? 1 : 0;
	}

	public static boolean isValidNumber(String str) {
		// empty string is not valid
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		boolean valid = str.length() == pos.getIndex();
		// has to be bigger than minimum size
		int minsize = 0;
		if (valid) {
			if (!(Integer.parseInt(str) > minsize)) {
				return false;
			}
		}
		return valid;
	}

	private int switchPlayer(int current) {
		current++;
		if (current < player.length) {
			return current;
		} else {
			return 0;
		}
	}

	private boolean canGoAgian(int dicenumber) {
		boolean canGo = false;
		for (int i : doubleTurn) {
			if (dicenumber == i) {
				canGo = true;
			}
		}
		return canGo;
	}

	private void printHelp() {
		// print help and game options
		System.out.println("help:");
		System.out.println("h geeft help");
		System.out.println("om een steen te verplaatsen typ je het nummer in van de te verplaatsen steen (zoals '10')");
		System.out.println("q stopt het spel en laat de andere speler winnen");
		System.out.println("r geeft je de regels van het spel");
		System.out.println("s geeft de beurt aan de andere speler");
		System.out.println("p print het bord nog een keer");
	}

	private void notifyMoveIsInvalid() {
		board.print();
		System.out.println("jouw stap is niet mogelijk of tegen de regels van het spel in");
		System.out.println("Geef jouw actie door of typ h voor help");
	}

	private void printRules() {
		System.out.println("De regels van het spel vind je op: http://nl.wikipedia.org/wiki/Senet");
	}

	private void printHasWon(String s) {
		board.print();
		System.out.println(s + " heeft gewonnen.");
		System.out.println("Bedankt voor het spelen en tot de volgende keer!");
	}
}