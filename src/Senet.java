import java.util.Scanner;

public class Senet {
	private Board board;
	private Dice dice;
	private Player[] player;
	private Scanner input;
	private int playerSize = 2;

	public Senet() {
		this.board = new Board();
		this.dice = new Dice();
		this.input = new Scanner(System.in);
		this.player = new Player[playerSize];
	}

	public void play() {

	}
}