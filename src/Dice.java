import java.util.Random;

public class Dice {
	private int bound = 6; // the max number that can be thrown
	private int[] excludedNumbers = { 0, 5 }; // these numbers will be excluded from the output
	private Random random = new Random();

	public int throwSticks() {
		// generate a random number and check if its allowed
		int n = randomInt();
		while(!testNumber(n, excludedNumbers)) {
			n = randomInt();
		}
		return n;
	}
	//tests if n != one of the numbers, returns false if n == one of the numbers
	private boolean testNumber(int n, int[] numbers) {
		for (int num : excludedNumbers) {
			if (n == num) {
				return false;
			}
		}
		return true;
	}

	private int randomInt() {
		int max = bound;
		// fixing the exclusive number issue of nextInt by adding one
		max++;
		int r = random.nextInt(max);
		return r;
	}
}
