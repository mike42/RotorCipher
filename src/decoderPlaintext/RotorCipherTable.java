package decoderPlaintext;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import simulator.Alphabet;

public class RotorCipherTable {
	char[] secondHeadings;
	char rotorStart;
	
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
		
		secondHeadings = null;
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
		rotorStart = rotorPos;
		
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		int x, y, l;
		char[] row;
		String outp = "";
		l = alphabet.length();
		for(y = 0; y < l; y++) {
			row = new char[26];
			for(x = 0; x < l; x++) {
				/* BLANKS */
				if(column[x] == null || column[x].cell[y] == '\0') {
					row[x] = '-';	
				} else {
					row[x] = column[x].cell[y];
				}
			}
			outp += new String(row) + "\n";
		}
		return outp;
	}
	
	/**
	 * Spew out a HTML cipher table.
	 */
	public String toHTML() {
		int x, y, l;
		String row;
		String outp = "";
		l = alphabet.length();
		char next;
		
		if(secondHeadings != null) {
			/* Print shuffled headings at the top */
			row = "<th>&nbsp;</th>";
			for(x = 0; x < l; x++) {
				row += "<th>" + Character.toLowerCase(secondHeadings[x]) + "</th>";
			}
			outp += "<tr>" + row + "</tr>\n";
		}
		
		/* Original headings below this */
		row = "<th>&nbsp;</th>";
		for(x = 0; x < l; x++) {
			next = (secondHeadings == null)? Character.toLowerCase(alphabet.getCharFromIndex(x)) : Character.toUpperCase(alphabet.getCharFromIndex(x));
			row += "<th>" + next + "</th>";
		}
		outp += "<tr>" + row + "</tr>\n";
		
		/* Table contents */
		for(y = 0; y < l; y++) {
			row = "<th>" + alphabet.getCharFromIndex(y) + "</th>";
			for(x = 0; x < l; x++) {
				if(column[x] == null || column[x].cell[y] == '\0') {
					row += "<td>&nbsp;</td>";	
				} else {
					row += "<td>" + column[x].cell[y] + "</td>";
				}
			}
			outp += "\t<tr>" + row + "</tr>\n";
		}
		return "<table class=\"ciphertable\">\n\t" + outp + "</table>";
	}

	/**
	 * Extract input wiring from the table assuming the output is not mixed
	 * 
	 * @return The 'most likely' key, based on some simple collisions. For tables with all columns > 50% full, this will be the correct wiring
	 */
	public String resolveClashesMixedInputOnly() {
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
			// TODO Option to print this information
			// System.out.println(thisMatch);
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
		
		return new String(heading);
	}

	/**
	 * Attempt to resolve clashes given mixed input and mixed output.
	 * 
	 * @return
	 */
	public String resolveClashesMixedInputOutput() {

		return null;
	}
	
	/**
	 * Re-shuffle the cipher table according to some new headings.
	 * 
	 * This assumes that the table is currently in A-Z order (ie, don't call it twice or it will get messy)
	 * 
	 * @param headings A string denoting the new headings
	 */
	public void shuffleTo(String headings) {
		secondHeadings = headings.toCharArray();
		
		CipherTableColumn[] old = column;
		column = new CipherTableColumn[26];

		for(int i = 0; i < secondHeadings.length; i++) {
			if(secondHeadings[i] == ' ') {
				/* Spaces represent undefined columns */
				column[i] = null;
			} else {
				column[i] = old[alphabet.getIndexFromChar(secondHeadings[i])];
			}
		}
	}
	
	/**
	 * Fill columns (must be in order by now) with the alphabet.
	 * 
	 * Null columns will now be re-created and filled
	 */
	public void fillColumns() {
		int x, y;
		for(x = 0; x < column.length; x++) {
			if(column[x] == null) {
				column[x] = new CipherTableColumn(alphabet, column.length);
			}
		}
		
		for(x = 0; x < column.length; x++) {
			/* Go down non-null columns */
			for(y = 0; y < column.length; y++) {
				/* Find and project non-empty characters */
				if(column[x].cell[y] != '\0') {
					projectLetter(x, y);
				}
			}
		}
		return;
	}
	
	/**
	 * Project a letter 26 times. The given character must be in a non-null column, and in the alphabet (ie not \0 itself)
	 * 
	 * @param x X-position of character to project.
	 * @param y Y-position of character to project
	 */
	private void projectLetter(int x, int y) {
		int i;
		int nextX, nextY;
		char curChar, nextChar;;
		curChar = column[x].cell[y];
		
		for(i = 0; i < 26; i++) {
			nextChar = alphabet.next(curChar);
			nextX = (x + 1) % alphabet.length();
			nextY = ((y - 1) + alphabet.length()) % alphabet.length();
			if(column[nextX] == null) {
				/* Skip over nulls */
			} else if(column[nextX].cell[nextY] == '\0') {
				column[nextX].cell[nextY] = nextChar;
			} else {
				return;
			}
			x = nextX;
			y = nextY;
			curChar = nextChar;
		}
	}
	
	/**
	 * Create a machine file based on this cipher table.
	 * 
	 * @param output The file to output to, or "-" for stdout
	 */
	public void makeMachine(String output) {
		int i, l = alphabet.length();
		boolean charUsed[] = new boolean[l];
		
		/* Open output */
		try {
			BufferedWriter out;
			if(output.equals("-")) {
				out = new BufferedWriter(new OutputStreamWriter(System.out));
			} else {
				out = new BufferedWriter(new FileWriter(output));
			}
			
			/* Output machine name */
			String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime());
			out.write("# Automatically generated machine definition, created " + timestamp);
			out.newLine();
			out.newLine();
			out.write("# Machine name");
			out.newLine();
			out.write("name \"Copycat Machine " + timestamp + "\"");
			out.newLine();
			out.newLine();
			
			/* Find input wiring */
			boolean first = true;
			for(i = 0; i < l; i++) {
				/* No characters used yet */
				charUsed[i] = false;
			}
			for(i = 0; i < l; i++) {
				if(first) {
					first = false;
					out.write("# Input wiring");
					out.newLine();
					out.write("inWiring \"");
				} else {
					out.write(" ");
				}
				if(secondHeadings[i] == ' ') {
					out.write(new char[] {alphabet.getCharFromIndex(i), '_'});
				} else {
					charUsed[alphabet.getIndexFromChar(secondHeadings[i])] = true;
					out.write(new char[] {alphabet.getCharFromIndex(i), secondHeadings[i]});
				}
			}
			/* Clean up with unused letters */
			for(i = 0; i < l; i++) {
				if(!charUsed[i]) {
					out.write(new char[] {' ', '_', alphabet.getCharFromIndex(i)});
				}
				/* Set back to false to re-use below */
				charUsed[i] = false;
			}
			out.write("\"");
			out.newLine();
			out.newLine();
			
			/* Find rotor wiring */
			first = true;
			for(i = 0; i < l; i++) {
				if(first) {
					first = false;
					out.write("# Rotor wiring");
					out.newLine();
					out.write("rotor 1 name \"Rotor 1\"");
					out.newLine();
					out.write("rotor 1 wiring \"");
				} else {
					out.write(" ");
				}

				if(column[i].cell[0] == '\0') {
					out.write(new char[] {'_', alphabet.getCharFromIndex(i)});
				} else {
					charUsed[alphabet.getIndexFromChar(column[i].cell[0])] = true;
					out.write(new char[] {column[i].cell[0], alphabet.getCharFromIndex(i)});
				}
				
			}
			
			for(i = 0; i < l; i++) {
				if(!charUsed[i]) {
					out.write(new char[] {' ', alphabet.getCharFromIndex(i), '_'});
				}
				/* Set back to false to re-use below */
				charUsed[i] = false;
			}
			
			out.write("\"");
			out.newLine();
			out.newLine();
			
			out.write("# Default config");
			out.newLine();
			
			out.write("selectRotors 1");
			out.newLine();
			
			out.write("setRotorsTo " + new String(new char[] {rotorStart}));
			out.newLine();
			
			out.flush();
		} catch(Exception e) {
			System.out.println("Failed to create machine due to error");
			e.printStackTrace();
		}
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
	
	/**
	 * Assuming un-mixed output, find the shortest distance from this column where no clashes occur
	 * 
	 * @param other
	 * @return
	 */
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
	
	/**
	 * Check for clashing cells between two tables
	 * 
	 * @param other The column to check against
	 * @return true if this column is 'compatible' with another (no clashes), false otherwise
	 */
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
	
	/**
	 * Get the column that is implied to be on the right of this one
	 * 
	 * @return A shifted column
	 */
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
	
	/* 
	 * @see java.lang.Object#clone()
	 */
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
	
	/* 
	 * @see java.lang.Object#clone()
	 */
	public String toString() {
		char[] outp = new char[cell.length];
		for(int i = 0; i < cell.length; i++) {
			outp[i] = (cell[i] == 0)? ' ': cell[i];
		}
		return new String(outp);
	}
}


/**
 * Holds information associating two columns. Eg "A is linked to B with a distance of 1, supported by 26 compatible cells"
 */
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
