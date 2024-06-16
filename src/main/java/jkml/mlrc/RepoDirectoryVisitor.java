package jkml.mlrc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.SortedMap;
import java.util.SortedSet;

import org.apache.maven.artifact.versioning.ComparableVersion;

class RepoDirectoryVisitor extends SimpleFileVisitor<Path> {

	private final RepoMapBuilder mapBuilder = new RepoMapBuilder();

	private final Path repoDir;

	public RepoDirectoryVisitor(Path repoDir) {
		this.repoDir = repoDir;
	}

	public SortedMap<String, SortedMap<String, SortedSet<ComparableVersion>>> getMap() {
		return mapBuilder.build();
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		try (var ds = Files.newDirectoryStream(dir, entry -> Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS))) {
			if (!ds.iterator().hasNext()) {
				var relativePath = repoDir.relativize(dir).toString();
				var coordinate = Utils.parse(relativePath);
				if (coordinate != null) {
					mapBuilder.add(coordinate);
				}
				return FileVisitResult.SKIP_SUBTREE;
			}
		}
		return FileVisitResult.CONTINUE;
	}

}
