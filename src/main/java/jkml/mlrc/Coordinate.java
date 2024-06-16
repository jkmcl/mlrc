package jkml.mlrc;

public class Coordinate {

	private final String groupId;

	private final String artifactId;

	private final String version;

	public Coordinate(String groupId, String artifactId, String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return toString(groupId, artifactId, version);
	}

	public static String toString(String groupId, String artifactId, String version) {
		return String.join(":", groupId, artifactId, version);
	}

}
