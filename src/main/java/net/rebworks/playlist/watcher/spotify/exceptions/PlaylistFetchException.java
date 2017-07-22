package net.rebworks.playlist.watcher.spotify.exceptions;

public class PlaylistFetchException extends RuntimeException {
    public PlaylistFetchException(final Exception exception) {
        super(exception);
    }
}
