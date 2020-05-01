package net.rebworks.playlist.watcher.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import net.rebworks.playlist.watcher.spotify.exceptions.TokenRefreshException;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;

public class TokenManager {
    private final SpotifyApi api;
    private LocalDateTime nextExpiry = LocalDateTime.now();

    public TokenManager(final SpotifyApi api) {
        this.api = api;
    }

    public void ensure() {
        final LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(nextExpiry)) return;
        try {
            final ClientCredentials clientCredentials = api.clientCredentials().build().execute();
            api.setAccessToken(clientCredentials.getAccessToken());
            nextExpiry = now.plusSeconds(clientCredentials.getExpiresIn());
        } catch (final IOException | SpotifyWebApiException | ParseException exception) {
            throw new TokenRefreshException(exception);
        }
    }
}
