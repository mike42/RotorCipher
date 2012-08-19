import java.util.Scanner;
import java.awt.EventQueue;

/**
 * Basic simulation of a Hebern rotor machine. 
 * 
 * @author Michael Billington <michael.billington@gmail.com>
 * @since 2012-09-08
 * @bugs cockroaches, beetles, and earwigs.
 */

public class HebernMachineSimulator {
	static public HebernRotorMachine machine;

	/**
	 * Parse arguments and launch the requested interface.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		/* Demonstration of loading the week1 rotor machine from a file */
	/*	try {
			RotorMachine foo = RotorMachine.fromFile("week01.machine");
			foo.setRotorsTo("Z".toCharArray());
			System.out.println(foo.encipher("ENEMY".toCharArray()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} */
		
		
		machine = new HebernRotorMachine();
		if(args.length == 0 || args[0].equals("--gui")) {
			System.out.println("Starting graphical interface. Try --cli for command-line version.");
			hebernGUI();
		} else if(args[0].equals("--cli")) {
			hebernCLI();
		} else {
			System.out.println("Invalid argument. Try --cli or --gui to launch the interface you are after.");
		}
	}

	/**
	 * Interface with machine via command-line
	 */
	final private static void hebernCLI() {
		String action, setting, todo; /* Next rotor setting, and next text block to encipher/decipher */
		Scanner input = new Scanner(System.in);

		do {
			/* Encipher, decipher, or quit */
			action = "";
			while(!action.equals("e") && !action.equals("d") && !action.equals("q")) {
				System.out.print("Encipher, decipher, or quit? (e / d / q): ");
				action = input.nextLine();
			}

			/* Stop on quit selection */
			if(action.equals("q")) {
				return;
			}

			/* Set rotor */
			setting = "";
			while(setting.length() < 1) {
				System.out.print("Set rotor: ");
				setting = input.nextLine().toUpperCase();
			}
			machine.setRotorsTo(setting.toCharArray());

			if(action.equals("e")) {
				/* Encipher */
				System.out.print("Plaintext: ");
				todo = input.nextLine().toUpperCase(); 
				System.out.println(machine.encipher(todo.toCharArray()));
			} else {
				/* Decipher */
				System.out.print("Ciphertext: ");
				todo = input.nextLine().toUpperCase(); 
				System.out.println(machine.decipher(todo.toCharArray()));
			}
		} while(!action.equals("q"));
	}

	/**
	 * Launch graphical interface
	 */
	private static void hebernGUI() {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			private HebernMachineGUI window;
			
			public void run() {
				try {
					window = new HebernMachineGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}


