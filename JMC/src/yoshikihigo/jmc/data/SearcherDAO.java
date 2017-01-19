package yoshikihigo.jmc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yoshikihigo.jmc.JMCConfig;

public class SearcherDAO {

	static public final SearcherDAO SINGLETON = new SearcherDAO();

	private Connection connector;
	private PreparedStatement statementSelection;
	private PreparedStatement methodSelection;
	private PreparedStatement hashSelection;

	private SearcherDAO() {
	}

	public void initialize() {

		try {
			Class.forName("org.sqlite.JDBC");
			final String database = JMCConfig.getInstance().getDATABASE();
			this.connector = DriverManager.getConnection("jdbc:sqlite:"
					+ database);

			this.statementSelection = this.connector
					.prepareStatement("select distinct methodID from statements where hash = ?");
			this.methodSelection = this.connector
					.prepareStatement("select file, fromLine, toLine from methods where id = ?");
			this.hashSelection = this.connector
					.prepareStatement("select hash, line from statements where methodID = ? order by line asc");

		} catch (final ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void initialize(String database) {

		try {
			Class.forName("org.sqlite.JDBC");
			//final String database = JMCConfig.getInstance().getDATABASE();
			this.connector = DriverManager.getConnection("jdbc:sqlite:"
					+ database);

			this.statementSelection = this.connector
					.prepareStatement("select distinct methodID from statements where hash = ?");
			this.methodSelection = this.connector
					.prepareStatement("select file, fromLine, toLine from methods where id = ?");
			this.hashSelection = this.connector
					.prepareStatement("select hash, line from statements where methodID = ? order by line asc");

		} catch (final ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public List<Integer> getMethodIDs(final Hash hash) {
		final List<Integer> methodIDs = new ArrayList<>();
		try {
			this.statementSelection.setBytes(1, hash.value);
			final ResultSet results = this.statementSelection.executeQuery();
			while (results.next()) {
				final int methodID = results.getInt(1);
				methodIDs.add(methodID);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return methodIDs;
	}

	public DBMethod getDBMethod(final int id) {
		DBMethod method = null;
		try {
			this.methodSelection.setInt(1, id);
			final ResultSet results = this.methodSelection.executeQuery();
			if (results.next()) {
				final String file = results.getString(1);
				final int fromLine = results.getInt(2);
				final int toLine = results.getInt(3);
				method = new DBMethod(id, file, fromLine, toLine);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return method;
	}

	public List<DBStatement> getDBStatements(final int methodID) {
		final List<DBStatement> statements = new ArrayList<>();
		try {
			this.hashSelection.setInt(1, methodID);
			final ResultSet results = this.hashSelection.executeQuery();
			while (results.next()) {
				final Hash hash = new Hash(results.getBytes(1));
				final int line = results.getInt(2);
				final DBStatement statement = new DBStatement(hash, line);
				statements.add(statement);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return statements;
	}

	public void close() {
		try {
			this.statementSelection.close();
			this.methodSelection.close();
			this.hashSelection.close();
			this.connector.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
