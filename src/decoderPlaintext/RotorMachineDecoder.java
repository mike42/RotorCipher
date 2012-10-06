package decoderPlaintext;
import java.util.Scanner;

public class RotorMachineDecoder {
	public static void decodeCLI(String rotorPos, boolean switchHTML, boolean switchVerbose, String output) {
		String plain, cipher;

		Scanner input = new Scanner(System.in);
		if(rotorPos.equals("")) {
			System.out.print("Enter rotor position: ");
			rotorPos = input.nextLine();
			System.out.println(rotorPos);
		}
		rotorPos = rotorPos.toUpperCase();

		System.out.println("Enter plaintext: ");
		plain = input.nextLine().toUpperCase();

		System.out.println("Enter ciphertext: ");
		cipher = input.nextLine().toUpperCase();

		RotorCipherTable table = new RotorCipherTable();
		table.readIn(plain.toCharArray(), cipher.toCharArray(), rotorPos.toCharArray()[0]);
		if(switchVerbose) {
			if(switchHTML) {
				System.out.println(table.toHTML());
			} else {
				System.out.println(table);
			}
		}
		
		String heading = table.resolveClashesMixedInputOnly();
		if(switchVerbose) {
			System.out.println("Headings: " + heading);
		}
		
		/* Reshuffle to new heading order */
		table.shuffleTo(heading);
		if(switchVerbose) {
			if(switchHTML) {
				System.out.println(table.toHTML());
			} else {
				System.out.println(table);
			}
		}
		
		/* Fill columns */
		table.fillColumns();
		if(switchVerbose) {
			if(switchHTML) {
				System.out.println(table.toHTML());
			} else {
				System.out.println(table);
			}
		}
		
		table.makeMachine(output);
	}
}
