package simulator;

import gui.SimulatorGUI;

import java.util.Scanner;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
	 * Interface with machine via command-line
	 */
	final public static void startCLI(String simMode, String input, String output, String rotorPos) {
		if (simMode.equals("dump")) {
			// TODO: Dump machine definition
			System.err.println("Printing machine definitions has not yet been implemented.");
		} else if(simMode.equals("encipher") || simMode.equals("decipher")) {
			/* Rotor position to set to */
			char[] setting;
			if(rotorPos.length() > 0) {
				setting = rotorPos.toCharArray();
			} else {
				setting = new char[] { machine.getRotor(0).getPositionChar() };
			}
			boolean encipher = simMode.equals("encipher");
			
			BufferedReader in;
			BufferedWriter out;
			try {
				/* Establish input and output buffers */
				if(input.equals("-")) {
					in = new BufferedReader(new InputStreamReader(System.in));
				} else {
					in = new BufferedReader(new FileReader(input));
				}
				if(output.equals("-")) {
					out = new BufferedWriter(new OutputStreamWriter(System.out));
				} else {
					out = new BufferedWriter(new FileWriter(output));
				}

				/* Loop until EOF */
				int next;
				char[] ch = new char[1];
				machine.setRotorsTo(setting);
				while((next = in.read()) != -1) {
					ch[0] = Character.toUpperCase((char) next);
					if(ch[0] == '\n') {
						/* Reset rotor position after each line */
						machine.setRotorsTo(setting);
						out.newLine();
						out.flush();
					} else {
						if(encipher) {
							out.write(machine.encipher(ch));
						} else {
							out.write(Character.toLowerCase(machine.decipher(ch)[0]));
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				return;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return;
			}
		} else {
			/* Use interactive mode by default */
			cliInteractive();
		}
	}
	
	final public static void cliInteractive() {
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
				System.out.println(new String(machine.decipher(todo.toCharArray())).toLowerCase());
			}
		} while(!action.equals("q"));
	}

	/**
	 * Launch graphical interface
	 */
	public static void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					SimulatorGUI window = new SimulatorGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}


