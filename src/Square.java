
public class Square {
	private char value;
	private char validValue[] = { '.', 'x', 'o', ':' };// allowed chars for in value

	public char getValue() {
		return value;
	}

	public Square(char value) {
		this.value = value;
	}

	public void setValue(char value) {
		if (testvalue(value, validValue)) {
			this.value = value;
		} else {
			// handle wrong data exeption
			System.out.println("wrong square value, using " + validValue[0] + " now");
			this.value = validValue[0];
		}
	}

	private boolean testvalue(char value, char[] allowed) {
		for (char correctValue : allowed) {
			if (value == correctValue) {
				return true;
			}
		}
		return false;
	}
}
