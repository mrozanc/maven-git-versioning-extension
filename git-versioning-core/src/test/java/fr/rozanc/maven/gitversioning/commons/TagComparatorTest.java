package fr.rozanc.maven.gitversioning.commons;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class TagComparatorTest {

    @TempDir
    Path tempDir;

    @Test
    void tagsPointingAtCommit_areOrderedByAnnotationDateThenVersion() throws GitAPIException, IOException {
        Git git = Git.init().setDirectory(tempDir.toFile()).call();
        RevCommit commit = git.commit().setMessage("initial commit").setAllowEmpty(true).call();
        git.tag().setName("v1.2.0").setObjectId(commit).setAnnotated(false).call();
        git.tag().setName("v1.10.0").setObjectId(commit).setAnnotated(false).call();
        git.tag().setName("v1.9.0").setObjectId(commit).setAnnotated(false).call();
        git.tag()
                .setName("annotated-older")
                .setObjectId(commit)
                .setAnnotated(true)
                .setTagger(tagger("2020-01-01T00:00:00Z"))
                .call();
        git.tag()
                .setName("annotated-newer")
                .setObjectId(commit)
                .setAnnotated(true)
                .setTagger(tagger("2021-01-01T00:00:00Z"))
                .call();

        ObjectId commitId = commit.copy();

        assertThat(GitUtil.tagsPointAt(commitId, git.getRepository()))
                .containsExactly("annotated-newer", "annotated-older", "v1.10.0", "v1.9.0", "v1.2.0");
    }

    private static PersonIdent tagger(String timestamp) {
        return new PersonIdent(
                "Test Tagger",
                "test@example.com",
                Date.from(Instant.parse(timestamp)),
                TimeZone.getTimeZone("UTC")
        );
    }
}