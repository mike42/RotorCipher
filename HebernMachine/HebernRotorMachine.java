/**
 * Sub-class of RotorMachine for the single-rotor Hebern Rotor Machine (google it for details)
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-10
 *
 */
import java.util.Random;

public class HebernRotorMachine extends RotorMachine {
	/**
	 * Construct single-rotor machine from known wiring and English alphabet
	 */
	public HebernRotorMachine() {
		super(new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 1, 1, false);
		Rotor rotor = new Rotor(this.alphabet, new String[] {"FA", "TB", "QC", "JD", "VE", "AF", "XG", "MH", "WI", "DJ", "SK", "NL", "HM", "LN", "RO", "UP", "CQ", "OR", "KS", "BT", "PU", "EV", "IW", "GX", "ZY", "YZ"});
		this.setRotor(0, rotor);
	}
	
	/**
	 * Generate a random Hebern Machine rotor. For the English alphabet, there are:
	 * 
	 * 26! / (2^13 * 13!) ~= 7.9*10^12 possible outputs.
	 * 
	 * @return - A rotor with random wiring which is compatible with this machine
	 */
	public Rotor getRandomRotor() {
		char[] letters = this.alphabet.toCharArray();	
		int alphabetSize = letters.length;
		String[] pair = new String[alphabetSize];
		char[] newPair;
		
		/* Randomly pair characters */
		for(int i = 0; i < alphabetSize / 2; i++) {
			/* Get pair and remove them from the array */
			newPair = randomPair(letters);
			letters = purgeEmptyElements(letters);
			
			/* Add this pair to the list twice, as wiring is reciprocol */
			pair[alphabet.getIndexFromChar(newPair[0])] = new String(new char[] {newPair[1], newPair[0]});
			pair[alphabet.getIndexFromChar(newPair[1])] = new String(new char[] {newPair[0], newPair[1]});
		}
		
		Rotor newRotor = new Rotor(this.alphabet, pair);
		return newRotor; //newRotor;
	}
	
	/**
	 * Randomly pair the next character
	 * 
	 * @param source an array of input characters
	 * @return a char array containing the first value of source[], and a random pair
	 */
	private static char[] randomPair(char[] source) {
		Random rand = new Random();
		char[] outp = new char[2];
		
		/* Choose values */
		int n = rand.nextInt(source.length - 1) + 1;
		outp = new char[] {source[0], source[n]};
		
		/* Strip values out of source (these are collected by purgeEmptyElements() below) */
		source[n] = '\0';
		source[0] = '\0';
		
		return outp;
	}
	
	/**
	 * Remove blank entries from a char array.
	 * 
	 * @param source an array containing some '\0' characters which we want to remove
	 * @return A new array, less than or equal to source.length, with no '\0' characteras at all.
	 */
	private static char[] purgeEmptyElements(char[] source) {
		int count = 0;
		for(int i = 0; i < source.length; i++) {
			/* Copy elements down to fill gaps, and count gaps as we go */
			if(source[i] == '\0') {
				count++;
			} else if(count != 0) {
				source[i - count] = source[i];
			}
		}
		
		if(count == 0) {
			/* No changes to be made to input */
			return source;
		}
		
		/* Copy values to new array of smaller size (ie, trim blanks of the end of source[]) */
		char[] outp = new char[source.length - count] ;
		System.arraycopy(source, 0, outp, 0, outp.length);
        return outp;
	}
}
