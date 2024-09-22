package me.qoomon.maven.gitversioning;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by qoomon on 30/11/2016.
 */
class BuildPropertiesTest {

    @Test
    void value() {
        // GIVEN

        // WHEN
        String projectGroupId = BuildProperties.projectGroupId();
        String projectArtifactId = BuildProperties.projectArtifactId();
        String projectVersion = BuildProperties.projectVersion();

        // THEN
        assertThat(projectGroupId).isEqualTo("fr.rozanc.maven");
        assertThat(projectArtifactId).isEqualTo("maven-git-versioning-extension");
        assertThat(projectVersion).isNotEmpty();
    }

}