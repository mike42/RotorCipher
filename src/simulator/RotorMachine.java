package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Simulates a generic rotor machine
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-08
 */
public class RotorMachine {
	Alphabet alphabet;

	/* Dealing with which rotors are in use */
	Rotor[] rotorLibrary;
	int[] selectedRotors;

	/* Plugboard options (unimplemented) */
	Rotor plugBoard;

	/* Adjustible in/out wiring */
	Rotor inWiring;
	Rotor outWiring;

	String name;

	/**
	 * Constructor
	 * 
	 * @param alphabet			The alphabet to use
	 * @param rotorCount		The number of rotors which this machine can use at once
	 * @param rotorLibrarySize	The number of rotors in the library (includes all defined rotors, regardless of which are in use)
	 * @param hasPlugBoard		True if the machine also has a plugboard, false otherwise
	 */
	public RotorMachine(Alphabet alphabet, int rotorCount, int rotorLibrarySize) {
		setName("Untitled");
		setAlphabet(alphabet);

		/* Default in-and-out wiring is 1-1 */
		inWiring = new Rotor(alphabet, new String[0]);
		outWiring = new Rotor(alphabet, new String[0]);

		/* Set number of rotors in machine */

		setRotorCount(rotorCount);
		setRotorLibrarySize(rotorLibrarySize);
	}

	/**
	 * Default to a single-rotor machine which uses the English alphabet and does no encipherment at all.
	 */
	public RotorMachine() {
		this(new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 1, 1);
	}

	/**
	 * Set the alphabet for the machine. Change this before you add rotors.
	 * 
	 * @param alphabet The new alphabet to use
	 */
	public void setAlphabet(Alphabet alphabet) {
		/* Load alphabet */
		this.alphabet = alphabet;
	}


	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	/**
	 * Set the number of rotors which you can select from
	 * 
	 * @param rotorLibrarySize	number of rotors which can be selected from
	 * @return false if the size was invalid, true otherwise
	 */
	public boolean setRotorLibrarySize(int rotorLibrarySize) {
		int rotorCount = selectedRotors.length;
		/* Must have enough selectable rotors to fill machine */
		if(rotorLibrarySize >= rotorCount) {
			rotorLibrary = new Rotor[rotorLibrarySize];

			/* Fill rotor library with blank rotors */
			for(int i = 0; i < rotorLibrary.length; i++) {
				rotorLibrary[i] = new Rotor(alphabet);
				if(i < rotorCount) {
					/* Selecting the first few rotors from library by default */
					selectedRotors[i] = i;
				}
			}
			return true;
		}
		return false;
	}

	public int getRotorLibrarySize() {
		return rotorLibrary.length;
	}

	/**
	 * Set machine name
	 * 
	 * @param name	The new name. Must not be null or empty.
	 * @return		True if the name was changed, false if the new name was invalid.
	 */
	public boolean setName(String name) {
		if(name != null && !name.equals("")) {
			this.name = name;
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the number of rotors which can be used simultaneously on this machine.
	 * 
	 * @param rotorCount Number of rotors which can be selected at once.
	 * @return 
	 */
	public boolean setRotorCount(int rotorCount) {
		/* Initialise selected rotors */
		if(rotorCount > 0) {
			selectedRotors = new int[rotorCount];
			return true;
		}
		return false;
	}

	/**
	 * Return a rotor from library
	 * 
	 * @param id	The rotor to get
	 * @return		the rotor, or null if there is no such rotor
	 */
	public Rotor getRotor(int id) {
		/* Return rotor from library */
		if(id >= 0 && id < rotorLibrary.length) {
			return rotorLibrary[id];
		} else {
			return null;
		}
	}

	/**
	 * Install rotor in rotor library
	 * 
	 * @param id		ID of the rotor to set, in the rotor library
	 * @param newRotor	A rotor object to put in the rotor library
	 */
	public void setRotor(int id, Rotor newRotor) {
		/* Set rotor in library */
		rotorLibrary[id] = newRotor;
	}

	/**
	 * Set in-wiring (where each letter connects on plugboard)
	 * 
	 * @param newInWiring	A rotor object representing the wiring
	 * @return 
	 */
	public boolean setInWiring(Rotor newInWiring) {
		this.inWiring = newInWiring;
		return true;
	}

	/**
	 * Set out-wiring (how enciphered characters are translated to output)
	 * 
	 * @param newOutWiring	A rotor object representing the wiring
	 */
	public boolean setOutWiring(Rotor newOutWiring) {
		this.outWiring = newOutWiring;
		return true;
	}

	/**
	 * Configure which rotors are in use
	 * 
	 * @param id	a list of rotors to use, given their ID in the rotor library
	 * @return		true if the rotors were set, false if the specified rotor setup is not possible
	 */
	public boolean selectRotors(int[] id) {
		/* Not yet implemented */
		return false;
	}

	/**
	 * Set rotor positions
	 * 
	 * @param inp	the array of rotor positions, left-to-right, eg "ABC".
	 * @return		true if rotating was successful, false if the rotor position given is invalid
	 */
	public boolean setRotorsTo(char[] inp) {
		/* Set rotors to a certain position */
		Rotor rotor = rotorLibrary[selectedRotors[0]];
		rotor.setTo(inp[0]);  /* Just working with one rotor for now */
		return true;
	}

	/**
	 * Encipher an array of characters.
	 * NB: rotors should be set before calling this.
	 * 
	 * @param in	An array of characters to encipher
	 * @return		The corresponding ciphertext
	 */
	public char[] encipher(char[] in) {
		/* Encipher some text */
		char[] out = new char[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = encipherChar(in[i]);
		}
		return out;
	}

	/**
	 * Encipher a single character with the machine by passing it through a series of transformations
	 * 
	 * @param	in the character to encipher
	 * @return	a ciphertext representation of the character, or the input character if it is not in the alphabet.
	 */
	public char encipherChar(char in) {
		if(!alphabet.hasChar(in)) {
			return in;
		}

		Rotor rotor = rotorLibrary[selectedRotors[0]];
		rotor.rotate();

		char out = in;

		/* Pass letter to input wiring (defaults to 1-1 [no transformation] if not set) */
		out = inWiring.encipherChar(out);

		/* Encipher with first rotor */
		out = rotor.encipherChar(out);

		/* Pass through output wiring */
		out = outWiring.encipherChar(out);
		return out;
	}

	/**
	 * Decipher an array of characters. Call setRotorsTo() before this to set starting position.
	 * 
	 * @param in
	 * @return
	 */
	public char[] decipher(char[] in) {
		/* Decipher some text */
		char[] out = new char[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = decipherChar(in[i]);
		}
		return out;
	}

	/**
	 * Decipher a single character. Call setRotorsTo() before this to set starting position. The machine's rotors may move as a side-effect of calling this method.
	 * 
	 * @param in The character to decipher
	 * @return	The plaintext for this character.
	 */
	public char decipherChar(char in) {
		if(!alphabet.hasChar(in)) {
			return in;
		}

		Rotor rotor = rotorLibrary[selectedRotors[0]];

		rotor.rotate();

		char out = in;

		/* Pass through out wiring */
		out = outWiring.decipherChar(out);

		/* Working with single rotor */
		out = rotor.decipherChar(out);

		/* Pass through in wiring */
		out = inWiring.decipherChar(out);
		return out;
	}

	/**
	 * Create a rotor machine which has been defined in a file.
	 * 
	 * @param fileName source
	 * @return RotorMachine object corresponding to defined machine.
	 * @throws FileNotFoundException 
	 */
	public static RotorMachine fromFile(String fileName) throws FileNotFoundException, Exception {
		/* Open file and create new machine */
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		RotorMachine machine = new RotorMachine();

		try {
			/* Parse file top-down */
			String line = file.readLine();
			char[] lineCh;

			/* Goal is to break it down into a command and its arguments */
			ArrayList<String> arg = new ArrayList<String>();
			String command;
			String[] commandArgs;

			int i, start, end, depth;
			while(line != null) {
				lineCh = (line + " ").toCharArray();
				arg.clear();
				end = start = depth = 0;

				/* Parse line to get command */
				for(i = 0; i < lineCh.length && depth >= 0; i++) {
					switch(depth) {
					case 0:
						/* Not in word */
						switch(lineCh[i]) {
						case ' ':
						case '\t':
							/* Skip past whitespace */
							start++; end++;
							break;
						case '#':
							/* Stop parsing once we hit a comment */
							depth = -1;
							break;
						case '"':
						case '{':
							/* Hit an open-quote */
							end = start = i + 1;
							depth += 2;
							break;
						default:
							/* Hit a word */
							depth++;
							end++;
						}
						break;
					case 1:
						/* In a word */
						switch(lineCh[i]) {
						case ' ':
						case '\t':
							arg.add(line.substring(start, end));
							end = start = i + 1;
							depth--;
							break;
						default:
							end++;
						}
						break;
					case 2:
						/* In a quote */
						switch(lineCh[i]) {
						case '"':
						case '}':
							depth--;
							break;
						default:
							end++;
						}
						break;
					}
				}

				/* Take command and fiddle with it */
				if(arg.size() >= 1) {
					/* Split command into command name, and string of arguments */
					command = arg.get(0);
					commandArgs = new String[arg.size() - 1];
					for(i = 1; i < arg.size(); i++) {
						commandArgs[i - 1] = arg.get(i);
					}

					processCommand(machine, command, commandArgs);
				}

				line = file.readLine();			
			}

			/* Successfully loaded machine, yay :-) */
			return machine;
		} catch (IOException e) {
			return null;
		}
	}

	private static boolean processCommand(RotorMachine machine, String command, String[] arg) throws Exception {
		int i;
		/*	# Machine
		name "Week 1 Rotor Machine"

		# Wiring
		rotorCount 1
		rotor 1 name "Rotor 1"
		rotor 1 wiring "FA TB QC JD VE AF XG MH WI DJ SK NL HM LN RO UP CQ OR KS BT PU EV IW GX ZY YZ"

		# Default config
		selectRotors 1
		setRotorsTo A */
		try {
			if(command.equals("name")) {
				/* Set rotor name */
				if(arg.length == 1) {
					return machine.setName(arg[0]);
				} else {
					throw new IllegalArgumentException(command + " expects 1 argument, but got " + arg.length);
				}

			} else if(command.equals("rotorLibrarySize")) {
				/* Set rotor library size */
				if(arg.length == 1) {
					i = Integer.parseInt(arg[0]);
					return machine.setRotorLibrarySize(i);
				} else {
					throw new IllegalArgumentException(command + " expects 1 argument, but got " + arg.length);
				}

			} else if(command.equals("rotorCount")) {
				/* Set rotor count */
				if(arg.length == 1) {
					i = Integer.parseInt(arg[0]);
					return machine.setRotorCount(i);
				} else {
					throw new IllegalArgumentException(command + " expects 1 argument, but got " + arg.length);
				}

			} else if(command.equals("rotor")) {
				/* Set rotor count */
				if(arg.length < 2) {
					throw new IllegalArgumentException(command + " expects at least 2 arguments, but got " + arg.length);
				}

				Rotor rotor;
				i = Integer.parseInt(arg[0]);
				if(i < 0) {
					throw new IllegalArgumentException("Rotor index must be 1 or greater");
				}
				i--; /* Take rotor indices from 1 rather than 0 */

				/* Load up rotor */
				if((rotor = machine.getRotor(i)) == null) {
					throw new IllegalArgumentException(command + " couldn't retrieve the rotor. Try calling rotorLibrarySize first with " + i + " or higher");
				}

				/* Got rotor. Check operation */
				if(arg[1].equals("name")) {
					/* Set rotor name */
					if(arg.length == 3) {
						return rotor.setName(arg[2]);
					} else {
						throw new IllegalArgumentException("Wrong number of arguments");
					}

				} else if(arg[1].equals("wiring")) {
					/* Set rotor wiring */
					if(arg.length != 3) {
						throw new IllegalArgumentException("Wrong number of arguments");
					}

					/* Load given pairs, and alphabet */
					String[] pair = arg[2].split(" ");
					Alphabet alphabet = machine.getAlphabet();
					
					if(!validateWiring(alphabet, pair)) {
						/* Check failed */
						throw new IllegalArgumentException("Invalid rotor wiring. Must contain pairs of letters separated by spaces.");
					}

					/* Okay, set the wiring */
					return rotor.setWiring(pair);
				} else {
					throw new IllegalArgumentException("There is no rotor option '" + arg[1] + "'.");
				}

			} else if(command.equals("selectRotors")) {
				/* Select rotors to use */
				if(arg.length == 1) {
					String[] strRotorIds = arg[0].split(" ");
					int[] intRotorIds = new int[strRotorIds.length];
					for(i = 0; i < strRotorIds.length; i++) {
						intRotorIds[i] = Integer.parseInt(strRotorIds[i]);
						intRotorIds[i]--; /* Take indices from 1 rather than 0 */
					}

					return machine.selectRotors(intRotorIds);
				} else {
					throw new IllegalArgumentException(command + " expects 1 arguments, but got " + arg.length);
				}

			} else if(command.equals("setRotorsTo")) {
				/* Set rotor initial position */
				if(arg.length == 1) {
					return machine.setRotorsTo(arg[0].toCharArray());
				} else {
					throw new IllegalArgumentException(command + " expects 1 argument, but got " + arg.length);
				}
			} else if(command.equals("inWiring") || command.equals("outWiring")) {
				/* In and out wiring */
				if(arg.length != 1) {
					throw new IllegalArgumentException(command + " expects 1 argument, but got " + arg.length);
				}
				
				/* Load given pairs, and alphabet */
				String[] pair = arg[0].split(" ");
				Alphabet alphabet = machine.getAlphabet();
				
				if(!validateWiring(alphabet, pair)) {
					/* Check failed */
					throw new IllegalArgumentException("Invalid rotor wiring. Must contain pairs of letters separated by spaces.");
				}

				/* Okay, set the wiring */
				Rotor rotor = new Rotor(alphabet);
				rotor.setWiring(pair);
				
				/* Set in or out wiring as appropriate */
				if(command.equals("inWiring")) {
					return machine.setInWiring(rotor);
				} else {
					return machine.setOutWiring(rotor);
				}
			} else {
				throw new Exception("Invalid command: " + command);
			}
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Integer expected");
		}
	}
	
	private static boolean validateWiring(Alphabet alphabet, String[] pair) {
		for(int i = 0; i < pair.length; i++) {
			/* Verify that this list contains only pairs of characters in the alphabet */
			if(pair[i].length() != 2 || !alphabet.hasChar(pair[i].charAt(0)) || !alphabet.hasChar(pair[i].charAt(1))) {
				return false;
			}
		}
		return true;
	}
}
