package jkml.mlrc.cli;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import jkml.mlrc.Core;

public class Main {

	static final int SUCCESS = 0;

	static final int FAILURE = 1;

	private static final String GROUP_ID = "g";

	private static final String ARTIFACT_ID = "a";

	private static final String DELETE = "d";

	public static void main(String... args) {
		System.exit(new Main().run(args));
	}

	public int run(String... args) {
		try {
			return run(new Core(), args);
		} catch (Exception e) {
			return FAILURE;
		}
	}

	private static void printHelp(Options options) {
		var formatter = new HelpFormatter();
		formatter.setOptionComparator(null);
		formatter.printHelp("Main", options);
	}

	int run(Core core, String... args) throws IOException {

		var options = new Options();
		options.addOption(GROUP_ID, "groupId", true, "Group ID filter");
		options.addOption(ARTIFACT_ID, "artifactId", true, "Artifact ID filter");
		options.addOption(DELETE, "delete", true, "Number of versions to keep");

		var parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			printHelp(options);
			return FAILURE;
		}

		var groupId = cmd.getOptionValue(GROUP_ID);
		var artifactId = cmd.getOptionValue(ARTIFACT_ID);
		var delete = cmd.getOptionValue(DELETE);
		var numToKeep = -1;
		if (delete != null) {
			numToKeep = Integer.parseInt(delete);
		}

		var argList = cmd.getArgList();
		if (argList.isEmpty()) {
			printHelp(options);
			return FAILURE;
		}

		var repoDir = Path.of(argList.get(0));
		if (delete == null) {
			core.list(repoDir, groupId, artifactId);
		} else {
			core.purge(repoDir, numToKeep, groupId, artifactId);
		}

		return SUCCESS;
	}

}
