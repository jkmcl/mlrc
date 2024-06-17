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
		FileUtils.deleteDirectory(Path.of(REPO_DIR).toFile());

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
	void testMain() {
		assertDoesNotThrow(() -> Main.main());
	}

	@Test
	void testMain_list() {
		assertDoesNotThrow(() -> Main.main("list", REPO_DIR));
	}

	@Test
	void testMain_purge() {
		assertDoesNotThrow(() -> Main.main("purge", "3", REPO_DIR));
	}

	@Test
	void testMain_purge_negativeN() {
		assertDoesNotThrow(() -> Main.main("purge", "0", REPO_DIR));
	}

	@Test
	void testMain_purge_missingN() {
		assertDoesNotThrow(() -> Main.main("purge", REPO_DIR));
	}

	@Test
	void testMain_unknown() {
		assertDoesNotThrow(() -> Main.main("unknown", "a", REPO_DIR));
	}

}
