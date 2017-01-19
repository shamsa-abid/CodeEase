package yoshikihigo.jmc.data;

public class DBStatement {

	final public Hash hash;
	final public int line;

	public DBStatement(final Hash hash, final int line) {
		this.hash = hash;
		this.line = line;
	}
}
