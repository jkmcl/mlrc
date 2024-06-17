package jkml.mlrc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class RepoMapBuilderTests {

	@Test
	void test() {
		var builder = new RepoMapBuilder();
		builder.add(new Coordinate("a", "b", "1"));
		builder.add(new Coordinate("a", "b", "2"));
		builder.add(new Coordinate("a", "c", "3"));
		builder.add(new Coordinate("x", "y", "4"));
		var map = builder.build();

		// a:b and a:c
		var artifactVersions = map.get("a");
		assertEquals(2, artifactVersions.size());

		var versionsList = List.copyOf(artifactVersions.values());

		// a:b:1 and a:b:2
		var versions = List.copyOf(versionsList.get(0));
		assertEquals(2, versions.size());
		assertEquals("1", versions.get(0).toString());
		assertEquals("2", versions.get(1).toString());

		// a:c:3
		versions = List.copyOf(versionsList.get(1));
		assertEquals(1, versions.size());
		assertEquals("3", versions.get(0).toString());

		// x:y
		artifactVersions = map.get("x");
		assertEquals(1, artifactVersions.size());

		versionsList = List.copyOf(artifactVersions.values());

		// x:y:4
		versions = List.copyOf(versionsList.get(0));
		assertEquals(1, versions.size());
		assertEquals("4", versions.get(0).toString());
	}

}
