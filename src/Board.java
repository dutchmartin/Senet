
public class Board {
	private Square[] square;
	private int boardSize = 30;
	private int boardRows = 3;

	public Board() {
		square = new Square[boardSize];
		for (int i = 0; i < boardSize; i++) {
			square[i] = new Square('.');
		}
	}

	public void print() {
		printSide();
		int rowSize = boardSize / boardRows;
		boolean countUp = true;
		for (int i = 0; i < boardRows; i++) {
			printBar(false);
			if (countUp) {
				for (int j = i * rowSize; j < (i + 1) * rowSize; j++) {
					printSquare(j);
				}
			} else {
				for (int j = (i + 1) * rowSize; j > i * rowSize; j--) {
					printSquare(j);
				}
			}
			countUp = !countUp;
			printBar(true);
		}
		printSide();
	}

	public Square getSquare(int position) {
		position--;
		if (square[position] != null) {
			return square[position];
		} else {
			// Nothing found, thats an error
			return null;
		}
	}

	private void printSide() {
		System.out.println("+----------+");
	}

	private void printBar(boolean isEndline) {
		// print a char and a newline if isEndline == true
		System.out.print(isEndline ? "|\n" : "|");
	}

	private void printSquare(int position) {
		char value = square[position].getValue();
		System.out.print(value);
	}

}