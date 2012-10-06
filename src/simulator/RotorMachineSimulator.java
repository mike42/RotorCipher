package simulator;

import gui.SimulatorGUI;

import java.text.SimpleDateFormat;
import java.util.Date;
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
		if(simMode.equals("encipher") || simMode.equals("decipher")) {
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
	
	/**
	 * Generate a random rotor machine
	 * 
	 * @param output Destination for new machine
	 */
	public static void randomMachine(String output){
		try {
			BufferedWriter out;
			if(output.equals("-")) {
				out = new BufferedWriter(new OutputStreamWriter(System.out));
			} else {
				out = new BufferedWriter(new FileWriter(output));
			}
			String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime());
			out.write("# Automatically generated machine definition, created " + timestamp);
			out.newLine();
			out.newLine();
			out.write("# Machine name");
			out.newLine();
			out.write("name \"Random Machine " + timestamp + "\"");
			out.newLine();
			out.newLine();
			
			Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			
			/* Input wiring */
			out.write("# Mixed input");
			out.newLine();
			out.write("inWiring \"");
			char[] random = alphabet.getRandomPermutation();
			char[] ordinary = alphabet.toCharArray();
			for(int i = 0; i < random.length; i++) {
				if(i != 0) {
					out.write(" ");
				}
				out.write(new char[] {ordinary[i], random[i]});
			}
			out.write("\"");
			out.newLine();
			out.newLine();
			
			/* First rotor */
			random = alphabet.getRandomPermutation();
			out.write("# Rotor wiring");
			out.newLine();
			out.write("rotor 1 name \"Rotor 1\"");
			out.newLine();
			out.write("rotor 1 wiring \"");
			random = alphabet.getRandomPermutation();
			for(int i = 0; i < random.length; i++) {
				if(i != 0) {
					out.write(" ");
				}
				out.write(new char[] {random[i], ordinary[i]});
			}
			out.write("\"");
			out.newLine();
			out.newLine();
			
			out.write("# Default config");
			random = alphabet.getRandomPermutation();
			out.newLine();
			out.write("selectRotors 1");
			out.newLine();
			out.write("setRotorsTo " + random[0]);
			out.newLine();
			
			out.flush();
			return;
		} catch(Exception e) {
			System.out.println("Failed to create machine due to error");
			e.printStackTrace();
		}
	}


}


