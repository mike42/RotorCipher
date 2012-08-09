/**
 * Simulates a single rotor on a rotor machine
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-08
 */
public class Rotor {
	Alphabet alphabet;
	
	char[][] substTable; 	/* Table of character substitutions */
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
		substTable = new char[numPositions][numPositions];

		int i;
		char c;

		/* Propagate A-A, B-B, etc, to do no transformations for undefined pairs */
		for(i = 0; i < numPositions; i++) {
			c = alphabet.getCharFromIndex(i);
			propagate(c, c);
		}

		/* Apply defined substitutions */
		char[] pairChar;
		for(i = 0; i < pair.length; i++) {
			pairChar = pair[i].toUpperCase().toCharArray();
			assert(pairChar.length == 2); /* 1 char to 1 char, other lengths are no good */

			/* Propagate item in table both ways */
			propagate(pairChar[0], pairChar[1]);
			propagate(pairChar[1], pairChar[0]);
		}
	}

	private void propagate(char in, char out) {
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

	public boolean setTo(char newPosChar) {
		/* Set the rotor to this position */
		position = alphabet.getIndexFromChar(newPosChar);
		return true;
	}

	public void rotate() {
		rotate(1);
	}

	public void rotate(int offset) {
		/* Spin the rotor a certain number of positions */
		position += offset;
		position %= numPositions;
	}	


	public char encipherChar(char c) {
		/* Return the result of putting a single character through this rotor */
		int inID = alphabet.getIndexFromChar(c);
		return substTable[position][inID];
	}
}
