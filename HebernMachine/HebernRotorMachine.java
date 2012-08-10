/**
 * Sub-class of RotorMachine for the single-rotor Hebern Rotor Machine (google it for details)
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-10
 *
 */

public class HebernRotorMachine extends RotorMachine {
	/**
	 * Construct single-rotor machine from known wiring and English alphabet
	 */
	public HebernRotorMachine() {
		super(new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 1, 1, false);

		Rotor rotor = new Rotor(this.alphabet, new String[] {"FA", "TB", "QC", "JD", "VE", "AF", "XG", "MH", "WI", "DJ", "SK", "NL", "HM", "LN", "RO", "UP", "CQ", "OR", "KS", "BT", "PU", "EV", "IW", "GX", "ZY", "YZ"});
		this.setRotor(0, rotor);
	}
}
