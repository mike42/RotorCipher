import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import decoderPlaintext.RotorMachineDecoder;
import simulator.RotorMachine;
import simulator.RotorMachineSimulator;

class RotorCipher {
	public static void main(String args[]) throws UnsupportedEncodingException {
		/* Default command-line options */
		boolean switchHelp = false;
		boolean switchCli = false;
		boolean switchKnownPlaintext = false;
		boolean switchHTML = false;
		boolean switchVerbose = false;
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
				} else if(args[i].equals("--new")) {				simMode = "new";		switchCli = true;
				} else if(args[i].equals("--input")) {				collect = "--input";
				} else if(args[i].equals("--html")) {				switchHTML = true;
				} else if(args[i].equals("--known-plaintext")) {	switchKnownPlaintext = true;
				} else if(args[i].equals("--verbose")) {			switchVerbose = true;
				} else {
					invalid = true;
					break;
				}
			}
		}
		
		if(invalid) {
			/* Didn't understand command-line arguments */
			System.out.println("An invalid option was given. Please check the documentation.");
		} else if(switchHelp) {
			/* Help */
			System.out.println("See ../doc/readme.html for usage.");
		} if(switchKnownPlaintext) {
			/* Known plaintext mode */
			if(switchCli) {
				RotorMachineDecoder.decodeCLI(rotorPos, switchHTML);
			} else {
				// TODO
				System.out.println("GUI for finding rotor wiring has not been implemented. Use --cli");
			}
		} else {
			/* Simulator mode */
			if(simMode.equals("new")) {
				RotorMachineSimulator.randomMachine(output);
				return;
			} else if (simulate.equals("-")) {
				String path = RotorCipher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				if(decodedPath.endsWith(".jar")) {
					decodedPath = new File(decodedPath).getParent();
				}
				simulate = decodedPath + "/../files/default.machine";		
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

			if(switchVerbose) {
				System.out.println(RotorMachineSimulator.machine);
			}
			
			if(switchCli) {
				RotorMachineSimulator.startCLI(simMode, input, output, rotorPos);
			} else {
				RotorMachineSimulator.startGUI();
			}
		}
	}
}