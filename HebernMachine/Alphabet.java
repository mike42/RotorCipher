public class Alphabet {
	public final static int MAX = 256; /* Number of characters that we are willing to accept */

	private char[] letter;
	private int[] letterIndex; /* Reverse of letter table */

	private int length;
	private boolean[] inAlphabet;

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
 
	public boolean hasChar(char letter) {
		/* Returns true if a letter is in this alphabet, false otherwise */
		if(letter >= 0 && letter < MAX) {
			return inAlphabet[letter];
		} else {
			return false;
		}
	}

	public int length() {
		return this.length;
	}

	public char next(char last) {
		/* Next char in alphabet, from this place */
		int from = getIndexFromChar(last);
		if(from < letter.length - 1) {
			return letter[from + 1];
		} else {
			return letter[0];
		}
	}

	public char prev(char last) {
		/* Next char in alphabet, from this place */
		int from = getIndexFromChar(last);
		if(from > 0) {
			return letter[from - 1];
		} else {
			return letter[letter.length - 1];
		}
	}

	public int getIndexFromChar(char c) {
		/* Return what index a character is in the alphabet. A returns 0, B returns 1, and so on. */
		assert(c >= 0 && c < MAX);
		return letterIndex[c];
	}

	public char getCharFromIndex(int n) {
		/* Return character n of the alphabet. 0 returns A, 1 returns B, and so on */
		assert(n >= 0 && n < letter.length);
		return letter[n];
	}
}
