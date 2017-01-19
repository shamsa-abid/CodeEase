package yoshikihigo.jmc.data;

import java.util.ArrayList;
import java.util.List;

public class DBMethod {

	final public int id;
	final public String file;
	final public int fromLine;
	final public int toLine;
	private double fitness;
	final List<DBStatement> statements;

	public DBMethod(final int id, final String file, final int fromLine,
			final int toLine) {
		this.id = id;
		this.file = file;
		this.fromLine = fromLine;
		this.toLine = toLine;
		this.fitness = 0f;
		this.statements = new ArrayList<>();
	}

	public void addStatements(final List<DBStatement> statements) {
		this.statements.addAll(statements);
	}

	public List<DBStatement> getStatements() {
		return new ArrayList<>(this.statements);
	}

	public void setFitness(final double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return this.fitness;
	}
}
