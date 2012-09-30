public abstract class Table<T extends column> {
	public <T>[] column;
	public char[] headings;
	public char[] sidings;

	private Alphabet alphabet;

	public Table(Alphabet alphabet) {
		this.alpahbet = alphabet;
		
	}

	public abstract addLetter();

}
