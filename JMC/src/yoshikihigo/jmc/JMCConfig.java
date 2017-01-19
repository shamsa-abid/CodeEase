package yoshikihigo.jmc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class JMCConfig {

	static private JMCConfig SINGLETON = null;

	static public boolean initialize(final String[] args) {

		if (null != SINGLETON) {
			return false;
		}

		final Options options = new Options();

		{
			final Option source = new Option("db", "database", true,
					"database to store method information");
			source.setArgName("database");
			source.setArgs(1);
			source.setRequired(false);
			options.addOption(source);
		}

		{
			final Option source = new Option("src", "source", true,
					"root directory of source files to store database");
			source.setArgName("directory");
			source.setArgs(1);
			source.setRequired(false);
			options.addOption(source);
		}

		{
			final Option target = new Option("target", "target", true,
					"source file including a target method");
			target.setArgName("file");
			target.setArgs(1);
			target.setRequired(false);
			options.addOption(target);
		}

		{
			final Option caret = new Option("caret", "caret", true,
					"position of writing method");
			caret.setArgName("number");
			caret.setArgs(1);
			caret.setRequired(false);
			options.addOption(caret);
		}

		{
			final Option thread = new Option("thd", "thread", true,
					"end revision of repository for test");
			thread.setArgName("thread");
			thread.setArgs(1);
			thread.setRequired(false);
			options.addOption(thread);
		}

		{
			final Option verbose = new Option("v", "verbose", false,
					"verbose output for progressing");
			verbose.setRequired(false);
			options.addOption(verbose);
		}

		{
			final Option debug = new Option("debug", "debug", false,
					"print some informlation for debugging");
			debug.setRequired(false);
			options.addOption(debug);
		}

		{
			final Option clones = new Option("clones", "clones", true,
					"file to output detected cloens");
			clones.setArgName("file");
			clones.setArgs(1);
			clones.setRequired(false);
			options.addOption(clones);
		}

		{
			final Option threshold = new Option("t", "threshold", true,
					"minimum clone size to detect");
			threshold.setArgName("number");
			threshold.setArgs(1);
			threshold.setRequired(false);
			options.addOption(threshold);
		}

		try {
			final CommandLineParser parser = new PosixParser();
			final CommandLine commandLine = parser.parse(options, args);
			SINGLETON = new JMCConfig(commandLine);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return true;
	}

	static public JMCConfig getInstance() {

		if (null == SINGLETON) {
			System.err.println("Config is not initialized.");
			System.exit(0);
		}

		return SINGLETON;
	}

	private final CommandLine commandLine;

	private JMCConfig(final CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	public String getSOURCE() {
		if (!this.commandLine.hasOption("src")) {
			System.err.println("option \"src\" is not specified.");
			System.exit(0);
		}
		return this.commandLine.getOptionValue("src");
	}

	public String getDATABASE() {
		if (!this.commandLine.hasOption("db")) {
			System.err.println("option \"db\" is not specified.");
			System.exit(0);
		}
		return this.commandLine.getOptionValue("db");
	}

	public boolean hasCARET() {
		return this.commandLine.hasOption("caret");
	}

	public int getCARET() {
		if (!this.commandLine.hasOption("caret")) {
			System.err.println("option \"caret\" is not specified.");
			System.exit(0);
		}
		final String text = this.commandLine.getOptionValue("caret");
		try {
			return Integer.parseUnsignedInt(text);
		} catch (final NumberFormatException e) {
			System.err
					.println("unsigned number must be specified for option \"caret\".");
			System.exit(0);
		}
		return 0;
	}

	public boolean hasTARGET() {
		return this.commandLine.hasOption("target");
	}

	public String getTARGET() {
		if (!this.commandLine.hasOption("target")) {
			System.err.println("option \"target\" is not specified.");
			System.exit(0);
		}
		return this.commandLine.getOptionValue("target");
	}

	public int getTHREAD() {
		return this.commandLine.hasOption("thd") ? Integer
				.parseInt(this.commandLine.getOptionValue("thd")) : 1;
	}

	public boolean isVERBOSE() {
		return this.commandLine.hasOption("v");
	}

	public boolean isDEBUG() {
		return this.commandLine.hasOption("debug");
	}
	
	public boolean hasCLONES(){
		return this.commandLine.hasOption("clones");
	}

	public String getCLONES() {
		if (!this.commandLine.hasOption("clones")) {
			System.err.println("option \"clones\" is not specified.");
			System.exit(0);
		}
		return this.commandLine.getOptionValue("clones");
	}

	public int getTHRESHOLD() {
		if (!this.commandLine.hasOption("threshold")) {
			System.err.println("option \"threshold\" is not specified.");
			System.exit(0);
		}
		final String text = this.commandLine.getOptionValue("threshold");
		try {
			return Integer.parseUnsignedInt(text);
		} catch (final NumberFormatException e) {
			System.err
					.println("unsigned number must be specified for option \"threshold\".");
			System.exit(0);
		}
		return 0;
	}
}
