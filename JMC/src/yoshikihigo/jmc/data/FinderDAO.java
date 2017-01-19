package yoshikihigo.jmc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yoshikihigo.jmc.JMCConfig;

public class FinderDAO {

	static public final String CLONES_SCHEMA = "id integer primary key autoincrement, cloneID integer, methodID integer";
	static public final FinderDAO SINGLETON = new FinderDAO();

	private PreparedStatement methodSelection;
	private PreparedStatement cloneInsertion;
	private int numberOfClones;

	private Connection connector;

	private FinderDAO() {
	}

	public void initialize() {

		try {
			Class.forName("org.sqlite.JDBC");
			final String database = JMCConfig.getInstance().getDATABASE();
			this.connector = DriverManager.getConnection("jdbc:sqlite:"
					+ database);

			final Statement statement = this.connector.createStatement();
			statement
					.executeUpdate("drop index if exists index_cloneID_clones");
			statement
					.executeUpdate("drop index if exists index_methodID_clones");
			statement.executeUpdate("drop table if exists clones");
			statement.executeUpdate("create table clones (" + CLONES_SCHEMA
					+ ")");
			statement.close();

			this.methodSelection = this.connector
					.prepareStatement("select file, fromLine, toLine from methods where hash = ?");
			this.cloneInsertion = this.connector
					.prepareStatement("insert into clones (cloneID, methodID) values (?, ?)");

			this.numberOfClones = 0;
			this.connector.setAutoCommit(false);

		} catch (final ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public List<Hash> getClonedHashs(final int threshold) {
		final List<Hash> hashs = new ArrayList<>();
		try {
			final PreparedStatement statement = this.connector
					.prepareStatement("select hash from methods where ? <= tokens group by hash having count(hash) >= 2 order by count(hash) desc;");
			statement.setInt(1, threshold);
			final ResultSet results = statement.executeQuery();
			while (results.next()) {
				final Hash hash = new Hash(results.getBytes(1));
				hashs.add(hash);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return hashs;
	}

	public DBClone getMethods(final Hash hash) {
		final DBClone clone = new DBClone();
		try {
			this.methodSelection.setBytes(1, hash.value);
			final ResultSet results = this.methodSelection.executeQuery();
			while (results.next()) {
				final String file = results.getString(1);
				final int fromline = results.getInt(2);
				final int toline = results.getInt(3);
				final JMethod method = new JMethod(file, fromline, toline);
				clone.addMethod(method);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return clone;
	}

	private void registerClone(final DBClone clone) {

		for (final JMethod method : clone.getMethods()) {
			try {
				this.cloneInsertion.setInt(1, clone.id);
				this.cloneInsertion.setInt(2, method.id);
				this.cloneInsertion.addBatch();
				this.numberOfClones++;

				if (10000 < this.numberOfClones) {
					if (JMCConfig.getInstance().isVERBOSE()) {
						System.out.println("writing \'clones\' table ...");
					}
					this.cloneInsertion.executeBatch();
					this.connector.commit();
					this.numberOfClones = 0;
				}

			} catch (final SQLException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	synchronized public void registerClones(final List<DBClone> clones) {
		clones.stream().forEach(clone -> this.registerClone(clone));
	}

	synchronized public void flush() {
		try {
			if (0 < this.numberOfClones) {
				if (JMCConfig.getInstance().isVERBOSE()) {
					System.out.println("writing \'clones\' table ...");
				}
				this.cloneInsertion.executeBatch();
				this.connector.commit();
				this.numberOfClones = 0;
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
					.executeUpdate("create index index_cloneID_clones on clones(cloneID)");
			statement
					.executeUpdate("create index index_methodID_clones on clones(methodID)");
			this.connector.commit();
			statement.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.methodSelection.close();
			this.connector.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
