package yoshikihigo.jmc;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import yoshikihigo.jmc.data.DBClone;
import yoshikihigo.jmc.data.FinderDAO;
import yoshikihigo.jmc.data.Hash;
import yoshikihigo.jmc.data.JMethod;

public class JMCFinder {

	public static void main(final String[] args) {

		JMCConfig.initialize(args);
		final int threshold = JMCConfig.getInstance().getTHRESHOLD();

		FinderDAO.SINGLETON.initialize();
		final List<Hash> hashs = FinderDAO.SINGLETON.getClonedHashs(threshold);

		final List<DBClone> clones = hashs.stream()
				.map(hash -> FinderDAO.SINGLETON.getMethods(hash))
				.collect(Collectors.toList());

		if (JMCConfig.getInstance().hasCLONES()) {
			try (final PrintWriter writer = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(JMCConfig
							.getInstance().getCLONES()), "UTF-8")))) {

				for (final DBClone clone : clones) {
					for (final JMethod method : clone.getMethods()) {
						writer.print(Integer.toString(clone.id));
						writer.print(", ");
						writer.print(method.file);
						writer.print(", ");
						writer.print(method.fromLine);
						writer.print(", ");
						writer.print(method.toLine);
						writer.println();
					}
				}

			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		else {
			FinderDAO.SINGLETON.registerClones(clones);
			FinderDAO.SINGLETON.flush();
			FinderDAO.SINGLETON.addIndices();
		}
		FinderDAO.SINGLETON.close();
	}
}
