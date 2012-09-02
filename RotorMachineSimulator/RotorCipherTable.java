public class RotorCipherTable {
	char[][] table;
	Alphabet alphabet;

	public RotorCipherTable(Alphabet alphabet) {	
		this.alphabet = alphabet;
		int l = alphabet.length();
		table = new char[l][l];
	}


	public RotorCipherTable() {
		this(new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
	}


	public boolean addLetter(char plain, char cipher, char rotor) {
		int x, y;
		x = alphabet.getIndexFromChar(plain);
		y = alphabet.getIndexFromChar(rotor);
		table[x][y] = cipher;
		return false;
	}

	public boolean readIn(char[] plain, char[] cipher, char rotorPos) {
		if(plain.length != cipher.length) {
			return false;
		}
		
		for(int i = 0; i < plain.length; i++) {
			/* Step the rotor and add the letter */
			if(plain[i] == ' ' || cipher[i] == ' ') {
				if(plain[i] != cipher[i]) {
					return false;
				}
			} else {
				rotorPos = alphabet.next(rotorPos);
				this.addLetter(plain[i], cipher[i], rotorPos);
			}
		}
		return true;
	}

	public String toString() {
		int x, y, l;
		char[] row;
		String outp = "";
		l = alphabet.length();
		for(y = 0; y < l; y++) {
			row = new char[26];
			for(x = 0; x < l; x++) {
				/* BLANKS */
				row[x] = table[x][y];	
			}
			outp += new String(row) + "\n";
		}
		return outp;
	}
}
