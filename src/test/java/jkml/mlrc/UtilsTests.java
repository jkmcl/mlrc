package jkml.mlrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class UtilsTests {

	private static String pathString(String first, String... more) {
		return Path.of(first, more).toString();
	}

	@Test
	void test() {
		var gav = Utils.parse(pathString(""));
		assertNull(gav);

		gav = Utils.parse(pathString("1.2.3"));
		assertNull(gav);

		gav = Utils.parse(pathString("a", "1.2.3"));
		assertNull(gav);

		var str = pathString("a", "b", "1.2.3");
		gav = Utils.parse(str);
		assertEquals("a", gav.getGroupId());
		assertEquals("b", gav.getArtifactId());
		assertEquals("1.2.3", gav.getVersion());
		assertEquals(str, Utils.format(gav));

		str = pathString("a", "b", "c", "1.2.3");
		gav = Utils.parse(str);
		assertEquals("a.b", gav.getGroupId());
		assertEquals("c", gav.getArtifactId());
		assertEquals("1.2.3", gav.getVersion());
		assertEquals(str, Utils.format(gav));
	}

}
