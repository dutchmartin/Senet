//@Author Martijn Groeneveldt
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		int boardRows = 3;
		Scanner input = new Scanner(System.in);
		// print header
		System.out.println("+-------------+");
		System.out.println("|--Senet--by--|");
		System.out.println("|-dutchmartin-|");
		System.out.println("+-------------+");
		System.out.println("Welkom bij Senet, typ p voor spelen of een van de cijfers 1,2,3 voor een testpositie");
		input: while (true) {
			String s = input.nextLine().trim();
			switch (s) {
			case "p":
				Senet senet = new Senet();
				senet.play();
				break input;
			case "1":
				String game1 = "xo.o.o.x.o" + "o.oox.o.o." + "o.xooo....";
				Senet senet1 = new Senet(new Board(boardRows,game1));
				senet1.testGame();
				break input;
			case "2":
				String game2 = ".........." + ".........." + ".ooo....x.";
				Senet senet2 = new Senet(new Board(boardRows,game2));
				senet2.testGame();
				break input;
			case "3":
				String game3 = ".....o...." + "..o....x.." + ".o..xx.xx.";
				Senet senet3 = new Senet(new Board(boardRows,game3));
				senet3.testGame();
				break input;

			default:
				System.out.println("commando " + s + "is niet geldig");
				System.out.println("typ p voor spelen of een van de cijfers 1,2,3 voor een testpositie");
				break;
			}
		}
		input.close();
	}

}
