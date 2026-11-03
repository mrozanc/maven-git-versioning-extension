package me.qoomon.gitversioning.commons;

import org.jspecify.annotations.NonNull;

public record GitDescription(String commit, String tag, int distance, boolean tagFound) {

    public int getDistanceOrZero() {
        return tagFound ? distance : 0;
    }

    @Override
    public @NonNull String toString() {
        return tag + "-" + distance + "-g" + commit.substring(0, 7);
    }
}
