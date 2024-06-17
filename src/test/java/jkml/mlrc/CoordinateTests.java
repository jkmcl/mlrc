package jkml.mlrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class CoordinateTests {

	@Test
	void test() {
		var coordinate = Coordinate.fromPath(Path.of(""));
		assertNull(coordinate);

		coordinate = Coordinate.fromPath(Path.of("1.2.3"));
		assertNull(coordinate);

		coordinate = Coordinate.fromPath(Path.of("a", "1.2.3"));
		assertNull(coordinate);

		var path = Path.of("a", "b", "1.2.3");
		coordinate = Coordinate.fromPath(path);
		assertEquals("a:b:1.2.3", coordinate.toString());
		assertEquals("a", coordinate.getGroupId());
		assertEquals("b", coordinate.getArtifactId());
		assertEquals("1.2.3", coordinate.getVersion());
		assertEquals(path, coordinate.toPath());

		path = Path.of("a", "b", "c", "1.2.3");
		coordinate = Coordinate.fromPath(path);
		assertEquals("a.b:c:1.2.3", coordinate.toString());
		assertEquals("a.b", coordinate.getGroupId());
		assertEquals("c", coordinate.getArtifactId());
		assertEquals("1.2.3", coordinate.getVersion());
		assertEquals(path, coordinate.toPath());
	}

}
