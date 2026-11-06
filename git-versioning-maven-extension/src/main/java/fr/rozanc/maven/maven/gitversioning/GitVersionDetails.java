package fr.rozanc.maven.maven.gitversioning;

import fr.rozanc.maven.gitversioning.commons.GitRefType;
import fr.rozanc.maven.maven.gitversioning.Configuration.RefPatchDescription;

import static java.util.Objects.requireNonNull;

public record GitVersionDetails(
        String commit,
        GitRefType refType,
        String refName,
        RefPatchDescription patchDescription
) {
    public GitVersionDetails(String commit, GitRefType refType, String refName, RefPatchDescription patchDescription) {
        this.commit = requireNonNull(commit);
        this.refType = requireNonNull(refType);
        this.refName = requireNonNull(refName);
        this.patchDescription = requireNonNull(patchDescription);
    }
}
