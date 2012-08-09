/**
 * Simulates a rotor machine
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
	boolean hasPlugBoard;
	Rotor plugBoard;

	/**
	 * Constructor
	 * 
	 * @param alphabet			The alphabet to use
	 * @param rotorCount		The number of rotors which this machine can use at once
	 * @param rotorLibrarySize	The number of rotors in the library (includes all defined rotors, regardless of which are in use)
	 * @param hasPlugBoard		True if the machine also has a plugboard, false otherwise
	 */
	public RotorMachine(Alphabet alphabet, int rotorCount, int rotorLibrarySize, boolean hasPlugBoard) {
		/* Load alphabet */
		this.alphabet = alphabet;

		/* Must have enough selectable rotors to fill machine */
		assert(rotorLibrarySize >= rotorCount);
		rotorLibrary = new Rotor[rotorLibrarySize];

		/* Initialise selected rotors */
		selectedRotors = new int[rotorCount];

		/* Fill rotor library with blank rotors */
		for(int i = 0; i < rotorLibrary.length; i++) {
			rotorLibrary[i] = new Rotor(alphabet, new String[0]);
			if(i < rotorCount) {
				/* Selecting the first few rotors from library by default */
				selectedRotors[i] = i;
			}
		}

		/* Plugboard goes into the cipher as another rotor, but it can be dynamically wired, so we treat it specially */
		this.hasPlugBoard = hasPlugBoard;
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
	 * Encipher a single character with the machine
	 * 
	 * @param	in the character to encipher
	 * @return	a ciphertext representation of the character, or the input character if it is not in the alphabet.
	 */
	public char encipherChar(char in) {
		/* Just run it through first rotor */
		if(!alphabet.hasChar(in)) {
			return in;
		} else {
			Rotor rotor = rotorLibrary[selectedRotors[0]];
			rotor.rotate();
			return rotor.encipherChar(in);
		}
	}
}
