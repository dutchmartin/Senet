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

		// decide who is gonna start and setup the game that way
		int current = whoFirst(playerChars); // contains the current i of player[i] that has their set
		player[current].setColorsign(playerChars[0]);
		System.out.print(player[current].getName());
		System.out.print(" speelt als " + player[current].getColorsign() + " en begint eerst\n");
		current = switchPlayer(current);
		System.out.print(player[current].getName());
		System.out.print(" speelt als " + player[current].getColorsign() + " en zijn zet wordt voor hem gedaan\n");
		// setup the gameboard
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
			while (true) {
				// procces input from user
				String userInput = input.nextLine();
				// decide what to do: exit, print help, or continue the game by setting a new
				// step
				userInput = userInput.trim().toLowerCase();
				switch (userInput) {
				case "h":
					printHelp();
					break;
				case "s":
					System.out.println(player[current].getName() + " geeft het op");
					current = switchPlayer(current);
					printHasWon(player[current].getName());
					return;
				default:
					System.out.println(userInput + " is geen valide actie, geef jouw actie door of typ h voor help");
					break;
				}
			}
		}
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
		System.out.println("s stopt het spel en laat de andere speler winnen");
		System.out.println();
	}

	private void printHasWon(String s) {
		System.out.println(s + " heeft gewonnen.");
		System.out.println("Bedankt voor het spelen en tot de volgende keer!");
	}
}