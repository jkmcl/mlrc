package jkml.mlrc;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void printHelp() {
		System.out.println("Usage: " + Main.class.getName() + " list REPO_DIR");
		System.out.println("       " + Main.class.getName() + " purge n REPO_DIR");
	}

	public static void main(String... args) throws Exception {
		if (args.length > 1 && args[0].equals("list")) {
			new Main().list(Path.of(args[1]));
		} else if (args.length > 2 && args[0].equals("purge") && Integer.parseInt(args[1]) > 0) {
			new Main().purge(Path.of(args[2]), Integer.parseInt(args[1]));
		} else {
			printHelp();
		}
	}

	SortedMap<String, SortedMap<String, SortedSet<ComparableVersion>>> walkRepoFileTree(Path repoDir) throws IOException {
		var visitor = new RepoDirectoryVisitor(repoDir);
		Files.walkFileTree(repoDir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, visitor);
		return visitor.getMap();
	}

	public void list(Path repoDir) throws IOException {
		logger.info("Listing artifacts in repository directory: {}", repoDir);

		var count = 0;
		var sb = new StringBuilder(8192);
		var lineSeparator = System.lineSeparator();

		for (var groupArtifacts : walkRepoFileTree(repoDir).entrySet()) {
			var groupId = groupArtifacts.getKey();
			for (var artifactVersions : groupArtifacts.getValue().entrySet()) {
				var artifactId = artifactVersions.getKey();
				for (var version : artifactVersions.getValue()) {
					++count;
					sb.append(Coordinate.toString(groupId, artifactId, version.toString())).append(lineSeparator);
				}
			}
		}

		System.out.println(sb);

		logger.info("Artifact count: {}", count);
	}

	/**
	 * Purge old versions of each artifact, keeping only the most recent N version
	 */
	public void purge(Path repoDir, int numberToKeep) throws IOException {
		logger.info("Deleting older artifacts in repository directory: {}", repoDir);

		var count = 0;
		for (var groupArtifacts : walkRepoFileTree(repoDir).entrySet()) {
			var groupId = groupArtifacts.getKey();
			for (var artifactVersions : groupArtifacts.getValue().entrySet()) {
				var artifactId = artifactVersions.getKey();
				var versions = artifactVersions.getValue();
				count += purge(repoDir, numberToKeep, groupId, artifactId, versions);
			}
		}
		logger.info("Delete count: {}", count);
	}

	private int purge(Path repoDir, int numberToKeep, String groupId, String artifactId, SortedSet<ComparableVersion> versions) {
		var totalNumber = versions.size();
		if (totalNumber <= numberToKeep) {
			return 0;
		}

		var count = 0;
		var index = 0;
		var numberToDelete = totalNumber - numberToKeep;
		for (var version : versions) {
			if (index++ < numberToDelete) {
				count += delete(repoDir, groupId, artifactId, version.toString());
			}
		}

		return count;
	}

	private int delete(Path repoDir, String groupId, String artifactId, String version) {
		var coordinate = Coordinate.toString(groupId, artifactId, version);
		var coordinateDir = repoDir.resolve(Coordinate.toPath(groupId, artifactId, version));
		try {
			FileUtils.deleteDirectory(coordinateDir.toFile());
			System.out.println("Deleted " + coordinate);
			return 1;
		} catch (Exception e) {
			System.err.println("Failed to delete " + coordinate);
			logger.error("Failed to delete directory: {}", coordinateDir, e);
			return 0;
		}
	}

}
