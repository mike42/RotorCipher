public class RotorMachine {
	Alphabet alphabet;

	Rotor[] rotorLibrary;
	int[] selectedRotors;

	boolean hasPlugBoard;
	Rotor plugBoard;

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

	public Rotor getRotor(int id) {
		/* Return rotor from library */
		if(id >= 0 && id < rotorLibrary.length) {
			return rotorLibrary[id];
		} else {
			return null;
		}
	}

	public void setRotor(int id, Rotor newRotor) {
		/* Set rotor in library */
		rotorLibrary[id] = newRotor;
	}

	public boolean selectRotors(int[] id) {
		/* Configure which rotors are in use */

		return false;
	}

	public boolean setRotorsTo(char[] inp) {
		/* Set rotors to a certain position */
		Rotor rotor = rotorLibrary[selectedRotors[0]];
		rotor.setTo(inp[0]);
		return true;
	}

	public char[] encipher(char[] in) {
		/* Encipher some text */
		char[] out = new char[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = encipherChar(in[i]);
		}
		return out;
	}

	public char encipherChar(char in) {
		/* Run it through first rotor */
		if(!alphabet.hasChar(in)) {
			return in;
		} else {
			Rotor rotor = rotorLibrary[selectedRotors[0]];
			rotor.rotate();
			return rotor.encipherChar(in);
		}
	}
}
