package jkml.mlrc;

import java.nio.file.FileSystems;
import java.util.regex.Pattern;

public class Utils {

	private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

	private static final Pattern separatorPattern = Pattern.compile(Pattern.quote(SEPARATOR));

	private Utils() {
	}

	public static Coordinate parse(String path) {
		var names = separatorPattern.split(path);
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

	public static String format(Coordinate coordinate) {
		var sb = new StringBuilder(128);
		sb.append(coordinate.getGroupId().replace(".", SEPARATOR)).append(SEPARATOR);
		sb.append(coordinate.getArtifactId()).append(SEPARATOR).append(coordinate.getVersion());
		return sb.toString();
	}

}
