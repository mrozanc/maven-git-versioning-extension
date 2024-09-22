package me.qoomon.gitversioning.commons;

public class GitDescription {
    private final String commit;
    private final String tag;
    private final int distance;
    private final boolean foundTag;

    public GitDescription(String commit, String tag, int distance, boolean foundTag) {
        this.commit = commit;
        this.tag = tag;
        this.distance = distance;
        this.foundTag = foundTag;
    }

    public String getCommit() {
        return commit;
    }

    public String getTag() {
        return tag;
    }

    public int getDistance() {
        return distance;
    }

    public int getDistanceOrZero() {
        return foundTag ? distance : 0;
    }

    @Override
    public String toString() {
        return tag + "-" + distance + "-g" + commit.substring(0,7);
    }
}
