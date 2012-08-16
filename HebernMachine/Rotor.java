/**
 * Simulates a single rotor on a rotor machine
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-08
 */
public class Rotor {
	Alphabet alphabet;
	
	char[][] encipherTable;	/* Table of character substitutions */
	char[][] decipherTable;	/* Inverse table of above to save on lookups */
	int position;			/* Current position of the rotor */
	int numPositions;		/* Number of possible positions this rotor can take (=length of the alphabet) */

	/**
	 * @param alphabet 	The alphabet to use. This must be the same for all rotors on a given machine
	 * @param pair 		Array of 2-character strings representing the substitutions that this rotor makes, eg {"AB", "BA"} 
	 */
	public Rotor(Alphabet alphabet, String[] pair) {
		/* Cannot override more characters than the length of the alphabet */
		assert(pair.length <= alphabet.length());
		this.alphabet = alphabet;
		numPositions = alphabet.length();
		encipherTable = new char[numPositions][numPositions];
		decipherTable = new char[numPositions][numPositions];

		int i;
		char c;

		/* Propagate A-A, B-B, etc, to do no transformations for undefined pairs */
		for(i = 0; i < numPositions; i++) {
			c = alphabet.getCharFromIndex(i);
			propagate(encipherTable, c, c);
			propagate(decipherTable, c, c);
		}

		/* Apply defined substitutions */
		char[] pairChar;
		for(i = 0; i < pair.length; i++) {
			pairChar = pair[i].toUpperCase().toCharArray();
			assert(pairChar.length == 2); /* 1 char to 1 char, other lengths are no good */

			/* Propagate item in table (reciprocal wiring not assumed) */
			propagate(encipherTable,	pairChar[1], pairChar[0]);
			propagate(decipherTable,	pairChar[0], pairChar[1]);
		}
	}

	/**
	 * Add a given wire, calculating what it does for each possible rotor position
	 * 
	 * @param in	Input character (left of rotor)
	 * @param out	Output character (right of rotor)
	 */
	private void propagate(char[][]substTable, char in, char out) {
		/* Add this character to the table */
		int inID;
		for(int pos = 0; pos < numPositions; pos++) {
			inID = alphabet.getIndexFromChar(in);
			substTable[pos][inID] = out;

			/* Rotate the rotor and fill in its next translation */
			in = alphabet.prev(in);
			out = alphabet.prev(out);
		}
	}

	/**
	 * Spin this rotor to the given position
	 * 
	 * @param newPosChar	Character to set the rotor to
	 * @return				True if the rotor was rotated, false if the character is not in the alphabet
	 */
	public boolean setTo(char newPosChar) {
		/* Set the rotor to this position */
		if(alphabet.hasChar(newPosChar)) {
			position = alphabet.getIndexFromChar(newPosChar);
			return true;
		}
		return false;
	}

	/**
	 * Spin the rotor in the default direction
	 */
	public void rotate() {
		rotate(1);
	}

	/**
	 * Spin the rotor a given number of positions
	 * 
	 * @param offset	Number of positions to move (can be positive or negative)
	 */
	public void rotate(int offset) {
		position += offset + numPositions;
		position %= numPositions;
	}	


	/**
	 * Encipher one character
	 * 
	 * @param c	Character to encipher
	 * @return	The result of putting this character through the rotor
	 */
	public char encipherChar(char c) {
		int inID = alphabet.getIndexFromChar(c);
		return encipherTable[position][inID];
	}

	public char decipherChar(char c) {
		int inID = alphabet.getIndexFromChar(c);
		return decipherTable[position][inID];
	}
}
