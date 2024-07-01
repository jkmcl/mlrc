package jkml.mlrc.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import jkml.mlrc.Core;

class MainTests {

	private static final String REPO_DIR = "target/mock_repo";

	private static final Path REPO_DIR_PATH = Path.of(REPO_DIR);

	private static final String GROUP_ID = "myGroup";

	private static final String ARTIFACT_ID = "myArtifact";

	@Test
	void testMain_noArg() {
		assertEquals(Main.FAILURE, new Main().run());
	}

	@Test
	void testMain_exception() {
		assertEquals(Main.FAILURE, new Main().run("no such directory"));
	}

	@Test
	void testMain_list() throws IOException {
		var core = mock(Core.class);
		new Main().run(core, REPO_DIR);
		verify(core).list(REPO_DIR_PATH, null, null);

		core = mock(Core.class);
		new Main().run(core, "-g", GROUP_ID, REPO_DIR);
		verify(core).list(REPO_DIR_PATH, GROUP_ID, null);

		core = mock(Core.class);
		new Main().run(core, "-g", GROUP_ID, "-a", ARTIFACT_ID, REPO_DIR);
		verify(core).list(REPO_DIR_PATH, GROUP_ID, ARTIFACT_ID);
	}

	@Test
	void testMain_delete() throws IOException {
		var core = mock(Core.class);
		new Main().run(core, "-d", "3", REPO_DIR);
		verify(core).purge(REPO_DIR_PATH, 3, null, null);

		core = mock(Core.class);
		new Main().run(core, "-d", "3", "-g", GROUP_ID, REPO_DIR);
		verify(core).purge(REPO_DIR_PATH, 3, GROUP_ID, null);

		core = mock(Core.class);
		new Main().run(core, "-d", "3", "-g", GROUP_ID, "-a", ARTIFACT_ID, REPO_DIR);
		verify(core).purge(REPO_DIR_PATH, 3, GROUP_ID, ARTIFACT_ID);
	}

}
