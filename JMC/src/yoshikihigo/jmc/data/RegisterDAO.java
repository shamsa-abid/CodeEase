package yoshikihigo.jmc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import yoshikihigo.jmc.JMCConfig;

public class RegisterDAO {

	static public final String METHODS_SCHEMA = "id integer primary key, hash blob, file string, fromline integer, toline integer, tokens integer";
	static public final String STATEMENTS_SCHEMA = "id integer primary key, methodID integer, hash blob, line integer";
	static public final RegisterDAO SINGLETON = new RegisterDAO();

	private Connection connector;
	private PreparedStatement methodInsertion;
	private PreparedStatement statementInsertion;
	private int numberOfMethods;
	private int numberOfStatements;

	private RegisterDAO() {
	}

	public void initialize() {

		try {
			Class.forName("org.sqlite.JDBC");
			final String database = JMCConfig.getInstance().getDATABASE();
			this.connector = DriverManager.getConnection("jdbc:sqlite:"
					+ database);

			final Statement statement = this.connector.createStatement();
			statement.executeUpdate("create table if not exists methods ("
					+ METHODS_SCHEMA + ")");
			statement.executeUpdate("create table if not exists statements ("
					+ STATEMENTS_SCHEMA + ")");
			statement.close();

			this.methodInsertion = this.connector
					.prepareStatement("insert into methods values (?, ?, ?, ?, ?, ?)");
			this.statementInsertion = this.connector
					.prepareStatement("insert into statements values(?, ?, ?, ?)");

			this.numberOfMethods = 0;
			this.numberOfStatements = 0;
			this.connector.setAutoCommit(false);

		} catch (final ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	synchronized public void registerMethod(final JMethod method) {

		try {
			this.methodInsertion.setInt(1, method.id);
			this.methodInsertion.setBytes(2, method.getHash().value);
			this.methodInsertion.setString(3, method.file);
			this.methodInsertion.setInt(4, method.fromLine);
			this.methodInsertion.setInt(5, method.toLine);
			this.methodInsertion.setInt(6, method.getNumberOfTokens());
			this.methodInsertion.addBatch();
			this.numberOfMethods++;

			if (10000 < this.numberOfMethods) {
				if (JMCConfig.getInstance().isVERBOSE()) {
					System.out.println("writing \'methods\' table ...");
				}
				this.methodInsertion.executeBatch();
				this.connector.commit();
				this.numberOfMethods = 0;
			}
		}

		catch (final SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	synchronized public void registerMethods(final Collection<JMethod> methods) {
		methods.stream().forEach(method -> this.registerMethod(method));
	}

	synchronized public void registerStatement(final JStatement statement) {

		try {
			this.statementInsertion.setInt(1, statement.id);
			this.statementInsertion.setInt(2, statement.methodID);
			this.statementInsertion.setBytes(3, statement.getHash().value);
			this.statementInsertion.setInt(4, statement.line);
			this.statementInsertion.addBatch();
			this.numberOfStatements++;

			if (10000 < this.numberOfStatements) {
				if (JMCConfig.getInstance().isVERBOSE()) {
					System.out.println("writing \'statements\' table ...");
				}
				this.statementInsertion.executeBatch();
				this.connector.commit();
				this.numberOfStatements = 0;
			}
		}

		catch (final SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	synchronized public void registerStatements(
			final Collection<JStatement> statements) {
		statements.stream().forEach(
				statement -> this.registerStatement(statement));
	}

	synchronized public void flush() {
		try {
			if (0 < this.numberOfMethods) {
				if (JMCConfig.getInstance().isVERBOSE()) {
					System.out.println("writing \'methods\' table ...");
				}
				this.methodInsertion.executeBatch();
				this.connector.commit();
				this.numberOfMethods = 0;
			}
			if (0 < this.numberOfStatements) {
				if (JMCConfig.getInstance().isVERBOSE()) {
					System.out.println("writing \'statements\' table ...");
				}
				this.statementInsertion.executeBatch();
				this.connector.commit();
				this.numberOfStatements = 0;
			}

		} catch (final SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	synchronized public void addIndices() {
		try {
			final Statement statement = this.connector.createStatement();
			statement
					.executeUpdate("create index index_hash_methods on methods(hash)");
			statement
					.executeUpdate("create index index_file_methods on methods(file)");
			statement
					.executeUpdate("create index index_tokens_methods on methods(tokens)");
			statement
					.executeUpdate("create index index_hash_statements on statements(hash)");
			statement
					.executeUpdate("create index index_methodID_statements on statements(methodID)");
			this.connector.commit();
			statement.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	synchronized public void close() {
		try {
			this.methodInsertion.close();
			this.statementInsertion.close();
			this.connector.close();
		} catch (final SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
