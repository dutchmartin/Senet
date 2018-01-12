import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Scanner;

public class Senet {
	private Board board;
	private Dice dice;
	private Player[] player;
	private Scanner input;
	private int playerSize = 2;
	private char[] playerChars = { 'x', 'o' };
	// private char validValue[] = { '.', 'x', 'o' };// allowed chars in the actual
	// playing field
	private int startNumber = 1; // the number to throw to start first with the game

	public Senet() {
		this.board = new Board();
		this.dice = new Dice();
		this.input = new Scanner(System.in);
		this.player = new Player[playerSize];
	}

	public void play() {
		// print header
		System.out.println("+-------------+");
		System.out.println("|--Senet--by--|");
		System.out.println("|-dutchmartin-|");
		System.out.println("+-------------+");
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
		board.print();
		current = switchPlayer(current);
		// start game loop
		while (true) {
			// inform player of the status
			System.out.print(player[current].getName());
			System.out.print(" is aan de beurt\n");
			int diceNumber = dice.throwSticks();
			System.out.println("Je hebt " + diceNumber + " gegooid!");
			System.out.println("Geef jouw actie door of typ h voor help");
			game: while (true) {
				// procces input from user
				String userInput = input.nextLine();
				// decide what to do: exit, print help, or continue the game by setting a new
				// step on the board
				userInput = userInput.trim().toLowerCase();
				if (isNumeric(userInput)) {
					int stonePlace = Integer.parseInt(userInput);
					// input number may not be greater than the number of fields
					if (!(stonePlace > board.getBoardsize())) {
						// check if the player even owns this stone, like you know, users can be stupid
						if (player[current].getColorsign() == board.getSquare(stonePlace).getValue()) {
							// check if the step is allowed by the game rules
							if (stepIsValid(stonePlace, diceNumber)) {

							}
						}
					}
					board.print();
					System.out.println("jouw stap is niet mogelijk of tegen de regels van het spel in");
					System.out.println("Geef jouw actie door of typ h voor help");
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
						break game;
					case "r":
						printRules();
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

	private boolean stepIsValid(int place, int steps) {
		return false;
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

	public static boolean isNumeric(String str) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}

	private int switchPlayer(int current) {
		current++;
		if (current < player.length) {
			return current;
		} else {
			return 0;
		}
	}

	private void printHelp() {
		// print help and game options
		System.out.println("help:");
		System.out.println("h geeft help");
		System.out.println("om een steen te verplaatsen typ je het nummer in van de te verplaatsen steen (zoals '10')");
		System.out.println("q stopt het spel en laat de andere speler winnen");
		System.out.println("r geeft je de regels van het spel");
		System.out.println("s geeft de beurt aan de andere speler");
	}

	private void printRules() {
		System.out.println("De regels van het spel vind je op: http://nl.wikipedia.org/wiki/Senet");
	}

	private void printHasWon(String s) {
		System.out.println(s + " heeft gewonnen.");
		System.out.println("Bedankt voor het spelen en tot de volgende keer!");
	}
}