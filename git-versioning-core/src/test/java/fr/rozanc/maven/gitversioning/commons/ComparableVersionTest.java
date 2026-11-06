package fr.rozanc.maven.gitversioning.commons;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComparableVersionTest {

    @Test
    void compareTo_matchesMaven310ForTagVersionBoundaries() {
        List<List<String>> versionPairs = List.of(
                List.of("refs/tags/v1.0", "refs/tags/v1.0.0"),
                List.of("refs/tags/v1.10.0", "refs/tags/v1.9.0"),
                List.of("1.0-SNAPSHOT", "1.0"),
                List.of("1.0-alpha-1", "1.0-beta"),
                List.of("1.0-rc1", "1.0"),
                List.of("1.0-sp", "1.0"),
                List.of("1.1234567890", "1.999999999"),
                List.of("1.1234567890123456789", "1.999999999999999999"),
                List.of("refs/tags/release-x", "refs/tags/release-y"),
                List.of("refs/tags/", "refs/tags/v1.0"),
                List.of("refs/tags/v1.0", "refs/tags/1.0")
        );

        for (List<String> versions : versionPairs) {
            String version1 = versions.get(0);
            String version2 = versions.get(1);
            int mavenComparison = new DefaultArtifactVersion(version1)
                    .compareTo(new DefaultArtifactVersion(version2));
            int coreComparison = new ComparableVersion(version1).compareTo(new ComparableVersion(version2));

            assertThat(Integer.signum(coreComparison))
                    .as("comparison of %s and %s", version1, version2)
                    .isEqualTo(Integer.signum(mavenComparison));
        }
    }
}