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

public class Core {

	private final Logger logger = LoggerFactory.getLogger(Core.class);

	SortedMap<String, SortedMap<String, SortedSet<ComparableVersion>>> walkRepoFileTree(Path repoDir) throws IOException {
		var visitor = new RepoDirectoryVisitor(repoDir);
		Files.walkFileTree(repoDir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, visitor);
		return visitor.getMap();
	}

	public void list(Path repoDir) throws IOException {
		list(repoDir, null, null);
	}

	public void list(Path repoDir, String groupId, String artifactId) throws IOException {
		logger.info("Listing artifacts in repository directory: {}", repoDir);

		var count = 0;
		var sb = new StringBuilder(8192);
		var lineSeparator = System.lineSeparator();

		for (var groupArtifacts : walkRepoFileTree(repoDir).entrySet()) {
			var currentGroupId = groupArtifacts.getKey();
			if (groupId != null && !groupId.equals(currentGroupId)) {
				continue;
			}
			for (var artifactVersions : groupArtifacts.getValue().entrySet()) {
				var currentArtifactId = artifactVersions.getKey();
				if (artifactId != null && !artifactId.equals(currentArtifactId)) {
					continue;
				}
				for (var version : artifactVersions.getValue()) {
					++count;
					sb.append(Coordinate.toString(currentGroupId, currentArtifactId, version.toString())).append(lineSeparator);
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
		purge(repoDir, numberToKeep, null, null);
	}

	public void purge(Path repoDir, int numberToKeep, String groupId, String artifactId) throws IOException {
		logger.info("Deleting older artifacts in repository directory: {}", repoDir);

		var count = 0;
		for (var groupArtifacts : walkRepoFileTree(repoDir).entrySet()) {
			var currentGroupId = groupArtifacts.getKey();
			if (groupId != null && !groupId.equals(currentGroupId)) {
				continue;
			}
			for (var artifactVersions : groupArtifacts.getValue().entrySet()) {
				var currentArtifactId = artifactVersions.getKey();
				if (artifactId != null && !artifactId.equals(currentArtifactId)) {
					continue;
				}
				var versions = artifactVersions.getValue();
				count += purge(repoDir, numberToKeep, currentGroupId, currentArtifactId, versions);
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
