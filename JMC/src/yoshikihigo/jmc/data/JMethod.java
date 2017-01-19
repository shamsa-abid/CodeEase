package yoshikihigo.jmc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JMethod {

	final private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	final public int id;
	final public String file;
	final public int fromLine;
	final public int toLine;
	final private List<JStatement> statements;

	public JMethod(final String file, final int fromLine, final int toLine) {
		this.id = ID_GENERATOR.getAndIncrement();
		this.file = file;
		this.fromLine = fromLine;
		this.toLine = toLine;
		this.statements = new ArrayList<>();
	}

	public void addStatement(final JStatement statement) {
		this.statements.add(statement);
	}

	public List<JStatement> getStatements() {
		return new ArrayList<JStatement>(this.statements);
	}

	public Hash getHash() {
		final List<String> texts = this.statements.stream()
				.map(statement -> statement.getText())
				.collect(Collectors.toList());
		return Hash.getMD5(String.join(System.lineSeparator(), texts));
	}

	public int getNumberOfTokens() {
		return this.statements.stream()
				.mapToInt(statement -> statement.getNumberOfTokens()).sum();
	}
}
