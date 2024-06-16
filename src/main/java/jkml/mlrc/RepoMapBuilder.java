package jkml.mlrc;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.artifact.versioning.ComparableVersion;

public class RepoMapBuilder {

	// (groupId -> (artifactId -> versions))
	private final SortedMap<String, SortedMap<String, SortedSet<ComparableVersion>>> groupArtifacts = new TreeMap<>();

	public void add(Coordinate coordinate) {
		var artifactVersions = groupArtifacts.get(coordinate.getGroupId());
		if (artifactVersions == null) {
			var versions = new TreeSet<ComparableVersion>();
			versions.add(new ComparableVersion(coordinate.getVersion()));
			artifactVersions = new TreeMap<>();
			artifactVersions.put(coordinate.getArtifactId(), versions);
			groupArtifacts.put(coordinate.getGroupId(), artifactVersions);
			return;
		}

		var versions = artifactVersions.get(coordinate.getArtifactId());
		if (versions == null) {
			versions = new TreeSet<>();
			versions.add(new ComparableVersion(coordinate.getVersion()));
			artifactVersions.put(coordinate.getArtifactId(), versions);
			return;
		}

		versions.add(new ComparableVersion(coordinate.getVersion()));
	}

	public SortedMap<String, SortedMap<String, SortedSet<ComparableVersion>>> build() {
		return groupArtifacts;
	}

}
