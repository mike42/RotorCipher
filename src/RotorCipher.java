import java.io.FileNotFoundException;

import decoderPlaintext.RotorMachineDecoder;

import simulator.RotorMachine;
import simulator.RotorMachineSimulator;

class RotorCipher {
	public static void main(String args[]) {
		/* Default command-line options */
		boolean switchHelp = false;
		boolean switchCli = false;
		boolean switchKnownPlaintext = false;
		String rotorPos = "";
		/* Simulator mode */
		String simulate = "-";
		String simMode = "interactive";
		String input = "-";
		String output = "-";

		/* Control */
		String collect = "";
		boolean invalid = false;
		
		for(int i = 0; i < args.length; i++) {
			if(!collect.equals("")) {
				if(       collect.equals("--rotorpos")) {	rotorPos = args[i].toUpperCase();
				} else if(collect.equals("--output")) {		output = args[i];
				} else if(collect.equals("--simulate")) {	simulate = args[i];
				} else if(collect.equals("--input")) {		input = args[i];
				}
				collect = "";
			} else {
				if(       args[i].equals("--help")) {				switchHelp = true;
				} else if(args[i].equals("--cli")) { 				switchCli = true;
				} else if(args[i].equals("--gui")) {				/* This has no effect */
				} else if(args[i].equals("--rotorpos")) {			collect = "--rotorpos";
				} else if(args[i].equals("--output")) {				collect = "--output"; 	switchCli = true;
				} else if(args[i].equals("--simulate")) {			collect = "--simulate";
				} else if(args[i].equals("--encipher")) {			simMode = "encipher"; 	switchCli = true;
				} else if(args[i].equals("--decipher")) {			simMode = "decipher"; 	switchCli = true;
				} else if(args[i].equals("--dump")) {				simMode = "dump";		switchCli = true;
				} else if(args[i].equals("--input")) {				collect = "--input";
				} else if(args[i].equals("--known-plaintext")) {	switchKnownPlaintext = true;
				} else {
					invalid = true;
					break;
				}
			}
		}
		
		if(invalid) {
			/* Didn't understand command-line arguments */
			System.out.println("invalid");
		} else if(switchHelp) {
			/* Help */
			System.out.println("help");
		} if(switchKnownPlaintext) {
			/* Known plaintext mode */
			if(switchCli) {
				RotorMachineDecoder.decodeCLI(rotorPos);
			} else {
				// TODO
				System.out.println("GUI for finding rotor wiring has not been implemented. Try --cli");
			}
		} else {
			/* Simulator mode */
			if(simulate == "-") {
				// TODO
				System.err.println("Randomly generated machines not yet implemented");
				return;
			}
			
			/* Load machine */
			try {
				RotorMachineSimulator.machine = RotorMachine.fromFile(simulate);
			} catch (Exception e) {
				System.err.println("Could not open machine definition '" + simulate + "': " + e.getMessage());
				return;
			}
			
			if(!rotorPos.equals("")) {
				RotorMachineSimulator.machine.setRotorsTo(rotorPos.toCharArray());
			}

			if(switchCli) {
				RotorMachineSimulator.startCLI(simMode, input, output, rotorPos);
			} else {
				RotorMachineSimulator.startGUI();
			}
		}
	}
}