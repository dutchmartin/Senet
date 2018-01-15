//@Author Martijn Groeneveldt
public class Player {
	private String name;
	private char colorsign;
	private char[] signs = { 'x', 'o' };// the allowed player colorSigns

	public Player(String name, char colorsign) {
		setName(name);
		setColorsign(colorsign);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getColorsign() {
		return colorsign;
	}

	public void setColorsign(char colorsign) {
		if (testsign(colorsign, signs)) {
			this.colorsign = colorsign;
		} else {
			// Handling the input error by assigning the first allowed colorSign
			System.out.println("Invalid colorsign, assuming " + signs[0]);
			this.colorsign = signs[0];
		}
	}

	private boolean testsign(char sign, char[] allowed) {
		for (char correctSign : allowed) {
			if (sign == correctSign) {
				return true;
			}
		}
		return false;
	}

}
