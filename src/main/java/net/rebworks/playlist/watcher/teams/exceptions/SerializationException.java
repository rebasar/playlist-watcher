package net.rebworks.playlist.watcher.teams.exceptions;

public class SerializationException extends RuntimeException {
    public SerializationException(final Exception exception) {
        super(exception);
    }
}
