import java.util.Scanner;
import java.awt.EventQueue;
import java.io.FileNotFoundException;

/**
 * Basic simulation of a Hebern rotor machine. 
 * 
 * @author Michael Billington <michael.billington@gmail.com>
 * @since 2012-09-08
 * @bugs cockroaches, beetles, and earwigs.
 */

public class RotorMachineSimulator {
	static public RotorMachine machine;

	/**
	 * Parse arguments and launch the requested interface.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		String fileName;
		if(args.length >= 1) {
			fileName = args[0];
		} else {
			fileName = "week02b.machine";
		}
		
		try {
			machine = RotorMachine.fromFile(fileName);
			if(args.length < 2 || args[1].equals("--gui")) {
				System.out.println("Starting graphical interface. Try --cli for command-line version.");
				simulatorGUI();
			} else if(args[1].equals("--cli")) {
				simulatorCLI();
			} else {
				System.out.println("Invalid argument. Try --cli or --gui to launch the interface you are after.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Interface with machine via command-line
	 */
	final private static void simulatorCLI() {
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
	private static void simulatorGUI() {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			private RotorMachineSimulatorGUI window;
			
			public void run() {
				try {
					window = new RotorMachineSimulatorGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}


