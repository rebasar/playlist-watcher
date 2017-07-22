package net.rebworks.playlist.watcher.spotify;

public class PlaylistId {
    private final String userId;
    private final String playlistId;

    public PlaylistId(final String userId, final String playlistId) {
        this.userId = userId;
        this.playlistId = playlistId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaylistId() {
        return playlistId;
    }
}
