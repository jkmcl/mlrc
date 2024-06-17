package jkml.mlrc;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Coordinate {

	private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

	private static final Pattern separatorPattern = Pattern.compile(Pattern.quote(SEPARATOR));

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

	public static Coordinate fromPath(Path path) {
		var names = separatorPattern.split(path.toString());
		var len = names.length;
		if (len < 3) {
			return null;
		}
		var version = names[--len];
		var artifact = names[--len];
		var sb = new StringBuilder(names[0]);
		for (var i = 1; i < len; ++i) {
			sb.append(".").append(names[i]);
		}

		return new Coordinate(sb.toString(), artifact, version);
	}

	@Override
	public String toString() {
		return toString(groupId, artifactId, version);
	}

	public static String toString(String groupId, String artifactId, String version) {
		return String.join(":", groupId, artifactId, version);
	}

	public Path toPath() {
		return toPath(groupId, artifactId, version);
	}

	public static Path toPath(String groupId, String artifactId, String version) {
		var sb = new StringBuilder(128);
		sb.append(groupId.replace(".", SEPARATOR)).append(SEPARATOR);
		sb.append(artifactId).append(SEPARATOR).append(version);
		return Path.of(sb.toString());
	}

}
