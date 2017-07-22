package net.rebworks.playlist.watcher.spotify;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import net.rebworks.playlist.watcher.spotify.exceptions.PlaylistFetchException;
import net.rebworks.playlist.watcher.TrackInfo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class PlaylistFetcherImpl implements PlaylistFetcher {
    private final Api api;
    private final TokenManager tokenManager;

    public PlaylistFetcherImpl(final Api api, final TokenManager tokenManager) {
        this.api = api;
        this.tokenManager = tokenManager;
    }

    @Override
    public Stream<TrackInfo> fetch(final PlaylistId playlist) {
        try {
            tokenManager.ensure();
            final PlaylistTracksRequest playlistTracksRequest = api.getPlaylistTracks(playlist.getUserId(),
                                                                                      playlist.getPlaylistId()).build();
            final Page<PlaylistTrack> playlistTrackPage = playlistTracksRequest.get();
            final List<PlaylistTrack> tracks = playlistTrackPage.getItems();
            return tracks.stream().map(TrackInfo::from);
        } catch (IOException | WebApiException exception) {
            throw new PlaylistFetchException(exception);
        }
    }
}
