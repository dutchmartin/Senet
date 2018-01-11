import java.util.Scanner;

public class Senet {
	private Board board;
	private Dice dice;
	private Player[] player;
	private Scanner input;
	private int playerSize = 2;
	private char[] playerChars = { 'x', 'o' };
	private int startNumber = 1; // the number to throw to start first with the game

	public Senet() {
		this.board = new Board();
		this.dice = new Dice();
		this.input = new Scanner(System.in);
		this.player = new Player[playerSize];
	}

	public void play() {
		// print header
		System.out.println("+------------+");
		System.out.println("|--Senet-by--|");
		System.out.println("|-dutchmartin|");
		System.out.println("+------------+");
		// initialize game by setting up players
		for (int i = 0; i < playerSize; i++) {
			System.out.println("speler " + i + ", voer jouw naam in:");
			String name = input.nextLine();
			this.player[i] = new Player(name, playerChars[i]);
		}
		// decide who is gonna start and setup the game that way
		System.out.println(whoFirst(playerChars)+ playerChars.length);
		System.out.println(player[whoFirst(playerChars)].getName() + " speelt als " + "" + "en begint eerst");
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
}