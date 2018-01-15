import java.text.NumberFormat;
import java.text.ParsePosition;

public class TestClass {

	public static void main(String[] args) {
//		Dice d = new Dice();
//		for (int i = 0; i < 100; i++) {
//			int r = d.throwSticks();
//			if (r == 5 || r == 0) {
//				System.out.println("funlkfnlen");
//			}
//			System.out.println(i + ": " + r);
//		}
//		Player pl = new Player("ron", 'o');
//		pl.getName();
//		Board board = new Board();
//		char validValue[] = { '.', 'x', 'o' };
//		int n = 0;
//		for (int i = 1; i < board.getBoardsize(); i++) {
//			board.getSquare(i).setValue(validValue[n]);
//			n++;
//			if(n >= validValue.length) {
//				n = 0;
//			}
//		}
//		board.getSquare(11).setValue(':');
//		board.print();
		String s ="";
		System.out.println(isNumeric(""));
		System.out.println(s.trim().isEmpty());
	}
	
	public static boolean isNumeric(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
}
}
