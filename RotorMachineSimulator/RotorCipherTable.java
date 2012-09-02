import java.util.ArrayList;
import java.util.Collections;

public class RotorCipherTable {
	CipherTableColumn[] column;
	Alphabet alphabet;

	public RotorCipherTable(Alphabet alphabet) {	
		this.alphabet = alphabet;
		int l = alphabet.length();
		column = new CipherTableColumn[l];
		for(int i = 0; i < l; i++) {
			column[i] = new CipherTableColumn(alphabet, l);
			column[i].header = alphabet.getCharFromIndex(i);
		}
	}

	public RotorCipherTable() {
		this(new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
	}


	public boolean addLetter(char plain, char cipher, char rotor) {
		int x, y;
		x = alphabet.getIndexFromChar(plain);
		y = alphabet.getIndexFromChar(rotor);
		column[x].cell[y] = cipher;
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
				if(column[x].cell[y] == '\0') {
					row[x] = '-';	
				} else {
					row[x] = column[x].cell[y];
				}
			}
			outp += new String(row) + "\n";
		}
		return outp;
	}
	
	public void analyse() {
		ArrayList<ColumnMatching> match = new ArrayList<ColumnMatching>();
		ColumnMatching thisMatch;
		int i;
		
		/* Find the most populated column */
		int target = 0;
		for(i = 1; i < column.length; i++) {
			if(column[i].getPopulation() > column[target].getPopulation()) {
				target = i;
			}
		}
		
		/* Find distances from this column */
		for(i = 0; i < column.length; i++) {
			thisMatch = new ColumnMatching(column[target].distanceTo(column[i]), column[target].header, column[i].header, Math.min(column[target].getPopulation(), column[i].getPopulation()));
			match.add(thisMatch);
		}
		
		Collections.sort(match);
		
		char[] heading = new char[26];
		int threshold = 1;
		
		/* Now build the list of headings */
		for(i = 0; i < match.size(); i++) {
			thisMatch = match.get(i);
			if(thisMatch.minPopulation < threshold) {
				break;
			}
			
			if(heading[thisMatch.distance] == 0) {
				heading[thisMatch.distance] = thisMatch.to;
			}
		}
		
		/* Clean up blanks with spaces */
		for(i = 0; i < heading.length; i++) {
			if(heading[i] == 0) {
				heading[i] = ' ';
			}
		}
		
		System.out.println(heading);
	}
}

class CipherTableColumn {
	Alphabet alphabet;
	public char[] cell;
	char header;
	boolean hasPopulation;
	int population;
	
	public int getPopulation() {
		if(!this.hasPopulation) {
			int i, c = 0;
			for(i = 0; i < cell.length; i++) {
				if(cell[i] != 0) {
					c++;
				}
			}
			this.population = c;
			this.hasPopulation = true;
		}
		return this.population;
	}
	
	public CipherTableColumn(Alphabet alphabet, int size) {
		cell = new char[size];
		this.alphabet = alphabet;
	}
	
	public int distanceTo(CipherTableColumn other) {
		CipherTableColumn sibling = this.clone();
		for(int i = 1; i < cell.length; i++) {
			sibling = sibling.shiftAlphabetic();
			if(sibling.isCompatible(other)) {
				return i;
			}
		}
		return 0;
	}
	
	public boolean isCompatible(CipherTableColumn other) {
		if(other.cell.length != this.cell.length) {
			return false;
		}
		
		for(int i = 0; i < this.cell.length; i++) {
			if(this.cell[i] != 0 && other.cell[i] != 0) {
				/* If the cells are both non-empty */
				if(other.cell[i] != this.cell[i]) {
					/* Freak out if they aren't equal */
					return false;
				}
			}
		}
		return true;
	}
	
	public CipherTableColumn shiftAlphabetic() {
		int i, j;
		CipherTableColumn sibling = new CipherTableColumn(this.alphabet, this.cell.length);
		sibling.header = alphabet.next(this.header);
		
		for(i = 0; i < this.cell.length; i++) {
			/* Move new letter up */
			j = (i + cell.length - 1) % cell.length;
			if(this.cell[i] != '\0') {
				sibling.cell[j] = alphabet.next(this.cell[i]);
			}
		}
		
		return sibling;
	}
	
	public CipherTableColumn clone() {
		CipherTableColumn clone = new CipherTableColumn(this.alphabet, this.cell.length);
		clone.header = this.header;
		clone.hasPopulation = this.hasPopulation;
		clone.population = this.population;
		
		for(int i = 0; i < this.cell.length; i++) {
			clone.cell[i] = this.cell[i];
		}
		return clone;
	}
	
	public String toString() {
		char[] outp = new char[cell.length];
		for(int i = 0; i < cell.length; i++) {
			outp[i] = (cell[i] == 0)? ' ': cell[i];
		}
		return new String(outp);
	}
}


class ColumnMatching implements Comparable<ColumnMatching> {	
	public int distance;
	public char from, to;
	public int minPopulation;
	
	public ColumnMatching(int distance, char from, char to, int minPopulation) {
		this.distance = distance;
		this.from = from;
		this.to = to;
		this.minPopulation = minPopulation;
	}

	/**
	 * Compare based on minPopulation.
	 */
	@Override
	public int compareTo(ColumnMatching other) {
		return -new Integer(minPopulation).compareTo(other.minPopulation);
	}
	
	public String toString() {
		return distance + "\t" + from + "\t" + to + "\t" + minPopulation;
	}
}