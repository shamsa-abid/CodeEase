package yoshikihigo.jmc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JStatement {

	final private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	final public int id;
	final public int methodID;
	final public int line;
	final private List<String> tokens;

	public JStatement(final int methodID, final int line) {
		this.id = ID_GENERATOR.getAndIncrement();
		this.methodID = methodID;
		this.line = line;
		this.tokens = new ArrayList<>();
	}

	public void addToken(final String token) {
		this.tokens.add(token);
	}

	public int getNumberOfTokens() {
		return this.tokens.size();
	}

	public String getText() {
		return String.join(" ", this.tokens);
	}

	public Hash getHash() {
		return Hash.getMD5(this.getText());
	}
}
