package jkml.mlrc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MainTests {

	private static final String REPO_DIR = "target/mock_repo";

	@BeforeAll
	static void beforeAll() throws IOException {
		var repoDir = Path.of(REPO_DIR);
		FileUtils.deleteDirectory(repoDir.toFile());
		Files.createDirectories(repoDir);
		Files.createDirectories(Path.of(REPO_DIR, ".cache"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.0"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.1"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.2"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.3"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.3"));
		Files.createDirectories(Path.of(REPO_DIR, "x"));
		Files.createDirectories(Path.of(REPO_DIR, "y", "z"));
	}

	@Test
	void testList() {
		var main = new Main();
		assertDoesNotThrow(() -> main.list(Path.of(REPO_DIR)));
	}

	@Test
	void testDelete() {
		var main = new Main();
		assertDoesNotThrow(() -> main.delete(Path.of(REPO_DIR), 3));
	}

}
