package simulator;
/**
 * Class for working with a user-defined alphabet for use in a cipher.
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-08
 */

public class Alphabet {
	public final static int MAX = 256; /* Number of characters that we are willing to accept */
	private char[] letter;
	
	private int[] letterIndex; /* Indices of each character (Reverse of letter table) */
	private boolean[] inAlphabet; 	/* Table of characters which are in the alphabet */

	private int length;

	/**
	 * Constructor
	 * 
	 * @param inp String representing all characters of the alphabet.
	 */
	public Alphabet(String inp) {
		/* {{TODO}} strip duplicate and out-of-range letters from input */
		int i;
		char c;
		char[] letter = inp.toCharArray();
		this.letter = letter;
		this.length = letter.length;

		/* Set which letters are in the alphabet */
		inAlphabet = new boolean[MAX];
		letterIndex = new int[MAX];

		/* First we mark all as false */
		for(i = 0; i < MAX; i++) {
			inAlphabet[i] = false;
		}

		/* Now go back and change the included letters to true */
		for(i = 0; i < letter.length; i++) {
			/* Currently cannot downsize alphabet to strip out dumb characters, so using assertion. */
			c = letter[i];
			assert(c >= 0 && c < MAX);
			inAlphabet[c] = true;
			letterIndex[c] = i;
		}
	}
 
	/**
	 * Check if a character is in the alphabet
	 * 
	 * @param letter a character to check
	 * @return true if the character is in this alphabet, false otherwise
	 */
	public boolean hasChar(char letter) {
		/* Returns true if a letter is in this alphabet, false otherwise */
		if(letter >= 0 && letter < MAX) {
			return inAlphabet[letter];
		} else {
			return false;
		}
	}

	/**
	 * Length of alphabet
	 * 
	 * @return the number of characters in this alphabet
	 */
	public int length() {
		return this.length;
	}

	/**
	 * Next character
	 * 
	 * @param last the current character
	 * @return the next character in the alphabet, from this one (or the first character, if the final one is given as input)
	 */
	public char next(char last) {
		/* Next char in alphabet, from this place */
		int from = getIndexFromChar(last);
		if(from < letter.length - 1) {
			return letter[from + 1];
		} else {
			return letter[0];
		}
	}

	/**
	 * Previous character
	 * 
	 * @param last the current character
	 * @return the previous character in the alphabet, from this one (or the last character, if the first is given as input)
	 */
	public char prev(char last) {
		/* Next char in alphabet, from this place */
		int from = getIndexFromChar(last);
		if(from > 0) {
			return letter[from - 1];
		} else {
			return letter[letter.length - 1];
		}
	}

	/**
	 * Look up a character in the alphabet
	 * 
	 * @param c the character to look up
	 * @return The index of this character in the alphabet, for English A becomes 0, B becomes 26.
	 */
	public int getIndexFromChar(char c) {
		/* Return what index a character is in the alphabet. A returns 0, B returns 1, and so on. */
		assert(c >= 0 && c < MAX);
		return letterIndex[c];
	}

	/**
	 * Get the nth letter of the alphabet
	 * 
	 * @param n
	 * @return character n of the alphabet. 0 returns A, 1 returns B, and so on
	 */
	public char getCharFromIndex(int n) {
		assert(n >= 0 && n < letter.length);
		return letter[n];
	}

	/**
	 * Get alphabet as array of single-character strings (used for spinner models in GUI)
	 * 
	 * @return array of Strings, one entry for each character, eg {"A", "B", "C"} for English
	 */
	public String[] toStringArray() {
		String[] outp = new String[letter.length];
		for(int i = 0; i < letter.length; i++) {
			outp[i] = new String(new char[] {letter[i]});
		}
		return outp;
	}
	
	/**
	 * Get alphabet as an array of characters (ie, return a copy of letter[])
	 * 
	 * @return A copy of this.letter[]
	 */
	public char[] toCharArray() {
		char[] outp = new char[letter.length];
		System.arraycopy(letter, 0, outp, 0, letter.length);
        return outp;
	}
}
