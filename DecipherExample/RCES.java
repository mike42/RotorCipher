import java.util.Scanner;

public class RotorMachineDecoder {

	public static void main(String[] args) {
		String plain, cipher, rotorpos;

		Scanner input = new Scanner(System.in);

		System.out.print("Enter rotor position: ");
		rotorpos = input.nextLine().toUpperCase();
	//	System.out.println(rotorpos);

	//	System.out.println("Enter plaintext: ");
	//	plain = input.nextLine().toUpperCase();
	//	System.out.println(plain);

		System.out.println("Enter ciphertext: ");
		cipher = input.nextLine().toUpperCase();
		System.out.println(cipher);
	

		RotorCipherTable table = new RotorCipherTable();
		table.readIn(plain.toCharArray(), cipher.toCharArray(), rotorpos.toCharArray()[0]);
		System.out.println("Done");
		System.out.println(table.toString());
		//table.resolveClashesMixedInputOnly();
		//table.resolveClashesMixedInputOutput();
	}
}
