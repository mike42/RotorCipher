import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import decoderPlaintext.RotorMachineDecoder;
import simulator.RotorMachine;
import simulator.RotorMachineSimulator;

class RotorCipher {
	final static String VERSION = "0.9";
	
	public static void main(String args[]) throws UnsupportedEncodingException {
		/* Default command-line options */
		boolean switchHelp = false;
		boolean switchCli = false;
		boolean switchKnownPlaintext = false;
		boolean switchHTML = false;
		boolean switchVerbose = false;
		boolean switchVersion = false;
		
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
				} else if(args[i].equals("--version")) {			switchVersion = true;
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
			System.out.println("An invalid option was given. Please use --help for usage information.");
		} else if(switchHelp) {
			/* Help */
			System.out.println(getHelp());
		} else if(switchVersion) {
			/* Version */
			System.out.println(getVersion());
		} else if(switchKnownPlaintext) {
			/* Known plaintext mode */
			if(switchCli) {
				RotorMachineDecoder.decodeCLI(rotorPos, switchHTML, switchVerbose, output);
			} else {
				// TODO create GUI for this
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
				if(switchHTML) {
					System.out.println(RotorMachineSimulator.machine.toHTML());
				} else {
					System.out.println(RotorMachineSimulator.machine);
				}
			}
			
			if(switchCli) {
				RotorMachineSimulator.startCLI(simMode, input, output, rotorPos);
			} else {
				RotorMachineSimulator.startGUI();
			}
		}
	}
	private static String getVersion() {
		return "RotorCipher " + VERSION + " - Tools and simulator for Rotor ciphers.\n" +
				"(c) 2012 Michael Billington, see doc/licence.txt";
	}
	
	private static String getHelp() {
		String help = getVersion() + "\n" + 
			"The following switches apply to all modes:\n" +
			"	--help\n" +
			"		Show basic usage information.\n" +
			"	--version\n" +
			"		Print version and exit.\n" +
			"	--cli\n" +
			"		Do not launch GUI interface.\n" +
			"	--gui\n" +
			"		Use GUI rather than CLI version (this is the default).\n" +
			"	--rotorpos R\n" +
			"		Position to start the rotor from. Default is A\n" +
			"	--output file\n" +
			"		File to dump output to. Use \"-\" for standard output.\n" +
			"	--verbose\n" +
			"		Print extra information which may be useful for debugging\n" + 
			"		unexpected output.\n" +
			"	--html\n" +
			"		Used in conjunction with --verbose to get tables in HTML\n" + 
			"		rather than plaintext.\n" +
			"\n" +
			"Arguments for simulator mode\n" +
			"	--simulate file.machine\n" +
			"		File to read machine definiton from. Set to \"-\" to generate\n" +
			"		a random machine definition.\n" +
			"	--encipher | --decipher | --new\n" +
			"		Use encipher or decipher mode, or create a new machine and\n" + 
			"		output it. Implies --cli.\n" +
			"	--input file\n" +
			"		Input file for the operation, or \"-\" to read from standard\n" +
			"		input (this is the default). Only used with --encipher\n" +
			"		or --decipher.\n" +
			"\n" +
			"Arguments for ciphertext/plaintext decoder mode\n" +
			"	--known-plaintext\n" +
			"		Attempt to figure out wiring based on plaintext/ciphertext. You\n" +
			"		will also be prompted for a rotor position if --rotorpos is not\n" +
			"		specified. The output of this operation is a machine file\n" +
			"		suitable for the simulator.\n" +
			"\n" +
			"See doc/readme.html for more detail/";
		return help;
	}
}