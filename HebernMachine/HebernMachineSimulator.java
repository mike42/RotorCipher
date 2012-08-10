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
		String setting, todo; /* Next rotor setting, and next text block to encipher/decipher */
		Scanner input = new Scanner(System.in);

		do {
			System.out.print("Set rotor (blank to exit): ");
			setting = input.nextLine();

			if(setting.length() > 0) {
				todo = input.nextLine(); 

				machine.setRotorsTo(setting.toUpperCase().toCharArray());
				System.out.println(machine.encipher(todo.toUpperCase().toCharArray()));
			}
		} while(setting.length() > 0);
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


