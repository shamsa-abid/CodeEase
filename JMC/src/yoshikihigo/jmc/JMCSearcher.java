package yoshikihigo.jmc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import yoshikihigo.jmc.data.DBMethod;
import yoshikihigo.jmc.data.DBStatement;
import yoshikihigo.jmc.data.Hash;
import yoshikihigo.jmc.data.JMethod;
import yoshikihigo.jmc.data.JStatement;
import yoshikihigo.jmc.data.SearcherDAO;

public class JMCSearcher {

	final public String path;
	final public int caret;

	public static void main(final String[] args) {

		JMCConfig.initialize(args);
		final String path = JMCConfig.getInstance().getTARGET();
		final int caret = JMCConfig.getInstance().getCARET();
		final long timeToStart = System.nanoTime();
		final JMCSearcher searcher = new JMCSearcher(path, caret);
		final List<DBMethod> methods = searcher.search();
		final long timeToEnd = System.nanoTime();

		methods.stream().forEach(method -> {
			final String text = getSourcecode(method);
			System.out.println("-------------------");
			System.out.println(method.id);
			System.out.print(text);
		});

		System.out.println(TimingUtility.getExecutionTime(timeToStart,
				timeToEnd));
	}

	public static String getSourcecode(final DBMethod method) {

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(method.file),
					StandardCharsets.ISO_8859_1);
		} catch (final IOException e) {
			System.err.println("cannot read \"" + method.file + "\"");
			return "";
		}

		final StringBuilder text = new StringBuilder();
		if (lines.size() < method.toLine) {
			System.err.println("incorrect file size \"" + method.file + "\"");
			return "";
		}

		for (int i = method.fromLine; i <= method.toLine; i++) {
			final String line = lines.get(i - 1);
			text.append(line);
			text.append(System.lineSeparator());
		}

		return text.toString();
	}

	public JMCSearcher(final String path, final int caret) {
		this.path = path;
		this.caret = caret;
	}

	public List<DBMethod> search() {

		final JMethod tMethod = this.getTargetMethod();
		final List<JStatement> tStatements = tMethod.getStatements();
		final List<Hash> tHashs = tStatements.stream()
				.map(statement -> statement.getHash())
				.collect(Collectors.toList());

		if (tStatements.isEmpty()) {
			return Collections.emptyList();
		}

		final SearcherDAO dao = SearcherDAO.SINGLETON;
		dao.initialize();
		Hash tHash = tHashs.get(0);
		final Set<Integer> methodIDs = new HashSet<>(dao.getMethodIDs(tHash));
		for (int i = 1; i < tStatements.size(); i++) {
			tHash = tHashs.get(i);
			methodIDs.retainAll(dao.getMethodIDs(tHash));
		}
		final List<DBMethod> dbMethods = methodIDs.stream()
				.map(methodID -> dao.getDBMethod(methodID))
				.collect(Collectors.toList());
		dbMethods.stream().forEach(
				dbMethod -> {
					final List<DBStatement> dbStatements = dao
							.getDBStatements(dbMethod.id);
					dbMethod.addStatements(dbStatements);
					final long sum = dbStatements.size();
					final long count = dbStatements.stream()
							.map(statement -> statement.hash)
							.filter(hash -> tHashs.contains(hash)).count();
					final double fitness = (double) count / (double) sum;
					dbMethod.setFitness(fitness);
				});
		Collections.sort(dbMethods,
				(e1, e2) -> Double.compare(e2.getFitness(), e1.getFitness()));
		dao.close();
		return dbMethods;
	}

	private JMethod getTargetMethod() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(this.path),
					StandardCharsets.UTF_8);
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println(this.path + " is not readable.");
			System.exit(0);
		}

		final ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(String.join(System.lineSeparator(), lines)
				.toCharArray());

		CompilationUnit unit = null;
		try {
			unit = (CompilationUnit) parser
					.createAST(new NullProgressMonitor());
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println(this.path + " is not parsable.");
			System.exit(0);
		}

		final JMCVisitor visitor = new JMCVisitor(this.path, unit);
		unit.accept(visitor);
		final List<JMethod> methods = visitor.getMethods();
		JMethod target = null;
		for (final JMethod method : methods) {
			if ((method.fromLine <= this.caret)
					&& (this.caret <= method.toLine)) {
				target = method;
				break;
			}
		}

		if (null == target) {
			System.err.println("specified caret doesn\'t match any methods.");
		}

		return target;
	}
}
