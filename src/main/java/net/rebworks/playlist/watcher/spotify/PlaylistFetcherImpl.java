package net.rebworks.playlist.watcher.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import net.rebworks.playlist.watcher.TrackInfo;
import net.rebworks.playlist.watcher.spotify.exceptions.PlaylistFetchException;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.stream.Stream;

public class PlaylistFetcherImpl implements PlaylistFetcher {
    private final SpotifyApi api;
    private final TokenManager tokenManager;

    public PlaylistFetcherImpl(final SpotifyApi api, final TokenManager tokenManager) {
        this.api = api;
        this.tokenManager = tokenManager;
    }

    @Override
    public Stream<TrackInfo> fetch(final PlaylistId playlist) {
        try {
            tokenManager.ensure();
            final GetPlaylistsItemsRequest playlistTracksRequest = api.getPlaylistsItems(
                    playlist.getPlaylistId()).build();
            final Paging<PlaylistTrack> playlistTrackPage = playlistTracksRequest
                    .execute();
            final PlaylistTrack[] tracks = playlistTrackPage.getItems();
            return Stream.of(tracks).map(TrackInfo::from);
        } catch (IOException | SpotifyWebApiException | ParseException exception) {
            throw new PlaylistFetchException(exception);
        }
    }
}
