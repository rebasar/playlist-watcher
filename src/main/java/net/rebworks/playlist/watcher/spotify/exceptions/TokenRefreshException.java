package net.rebworks.playlist.watcher.spotify.exceptions;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(final Throwable cause) {
        super(cause);
    }
}
