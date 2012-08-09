import java.util.Scanner;

public class HebernMachine {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Alphabet en = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

		RotorMachine hebern = new RotorMachine(en, 1, 1, false);
		Rotor rotor = new Rotor(en, new String[] {"FA", "TB", "QC", "JD", "VE", "AF", "XG", "MH", "WI", "DJ", "SK", "NL", "HM", "LN", "RO", "UP", "CQ", "OR", "KS", "BT", "PU", "EV", "IW", "GX", "ZY", "YZ"});

		hebern.setRotor(0, rotor);
		String setting = "", todo = "";

		do {
			System.out.print("Set rotor (blank to exit): ");
			setting = input.nextLine();

			if(setting.length() > 0) {
				todo = input.nextLine(); 

				hebern.setRotorsTo(setting.toUpperCase().toCharArray());
				System.out.println(hebern.encipher(todo.toUpperCase().toCharArray()));
			}
		} while(setting.length() > 0);
	}


}
