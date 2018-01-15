import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GamefileReader {
	private BufferedReader br;
	private FileReader fr;
	@SuppressWarnings("unused")
	private char[] validInput;

	public GamefileReader(String inputFile, char[] validInput) {
		try {
			this.fr = new FileReader(inputFile);
			this.br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.validInput = validInput;
	}
	public char[] getFileContent() {
		char[] content = null;
		try {
			br.read(content, 1,100);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
