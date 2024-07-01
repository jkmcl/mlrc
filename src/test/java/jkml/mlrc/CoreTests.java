package jkml.mlrc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CoreTests {

	private static final String REPO_DIR = "target/mock_repo";

	private static final Path REPO_DIR_PATH = Path.of(REPO_DIR);

	@BeforeAll
	static void beforeAll() throws IOException {
		FileUtils.deleteDirectory(Path.of(REPO_DIR).toFile());

		Files.createDirectories(Path.of(REPO_DIR, ".cache"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.0"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.1"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.2"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.3"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "c", "1.3"));
		Files.createDirectories(Path.of(REPO_DIR, "a", "b", "d", "1.0"));
		Files.createDirectories(Path.of(REPO_DIR, "x"));
		Files.createDirectories(Path.of(REPO_DIR, "y", "z"));
		Files.createDirectories(Path.of(REPO_DIR, "x", "y", "z", "2.0"));
	}

	@Test
	void testList() {
		assertDoesNotThrow(() -> new Core().list(REPO_DIR_PATH));
		assertDoesNotThrow(() -> new Core().list(REPO_DIR_PATH, "a.b", "c"));
	}

	@Test
	void testPurge() {
		assertDoesNotThrow(() -> new Core().purge(REPO_DIR_PATH, 3));
		assertDoesNotThrow(() -> new Core().purge(REPO_DIR_PATH, 3, "a.b", "c"));
	}

	@Test
	void testPurgeNegativeN() {
		assertDoesNotThrow(() -> new Core().purge(REPO_DIR_PATH, -1));
	}

}
