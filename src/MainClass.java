
public class MainClass {

	public static void main(String[] args) {
		Dice d = new Dice();
		for (int i = 0; i < 100; i++) {
			int r = d.throwSticks();
			if(r==5 || r==0) {
				System.out.println("funlkfnlen");
			}
			System.out.println(i+": "+r);
		}

	}

}
