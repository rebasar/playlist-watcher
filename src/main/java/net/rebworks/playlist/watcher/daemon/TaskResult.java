package net.rebworks.playlist.watcher.daemon;

public enum TaskResult {

    CONTINUE(false),
    TERMINATE(true);

    private final boolean terminate;

    TaskResult(final boolean terminate) {
        this.terminate = terminate;
    }

    public boolean isTerminate() {
        return terminate;
    }
}
