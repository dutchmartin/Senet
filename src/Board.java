
//@Author Martijn Groeneveldt
public class Board {
	private Square[] square;
	private int boardSize;
	private int boardRows;
	private char blank = '.';

	public Board(int boardSize, int boardRows) {
		this.boardSize = boardSize;
		this.boardRows = boardRows;
		square = new Square[boardSize];
		for (int i = 0; i < boardSize; i++) {
			square[i] = new Square(blank);
		}
	}

	public Board(int boardRows, String testboard) {
		this.boardSize = testboard.length();
		this.boardRows = boardRows;
		square = new Square[boardSize];
		char[] boardValues = testboard.toCharArray();
		int i = 0;
		for (char value : boardValues) {
			square[i] = new Square(value);
			i++;
		}
	}

	public void print() {
		printSide();
		int rowSize = getRowSize();
		boolean countUp = true;
		for (int i = 0; i < boardRows; i++) {
			printBar(false);
			if (countUp) {
				// count up
				for (int j = i * rowSize; j < (i + 1) * rowSize; j++) {
					printSquare(j);
				}
			} else {
				// count down
				for (int j = ((i + 1) * rowSize) - 1; j >= i * rowSize; j--) {
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
			System.out.println("ERROR: Your asking number " + position + " which does not exist in this universe");
			return null;
		}
	}

	public void move(int posFrom, int posTo) {
		getSquare(posTo).setValue(getSquare(posFrom).getValue());
		getSquare(posFrom).setValue(blank);
	}

	public void reverse(int posA, int posB) {
		char valueA = getSquare(posA).getValue();
		getSquare(posA).setValue(getSquare(posB).getValue());
		getSquare(posB).setValue(valueA);
	}

	public void setBlank(int position) {
		getSquare(position).setValue(blank);
	}

	public int getBoardsize() {
		return this.boardSize;
	}

	public int getRowSize() {
		return (int) boardSize / boardRows;
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
