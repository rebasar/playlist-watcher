package net.rebworks.playlist.watcher.spotify;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.ClientCredentials;
import net.rebworks.playlist.watcher.spotify.exceptions.TokenRefreshException;

import java.io.IOException;
import java.time.LocalDateTime;

public class TokenManager {
    private final Api api;
    private LocalDateTime nextExpiry = LocalDateTime.now();

    public TokenManager(final Api api) {
        this.api = api;
    }

    public void ensure() {
        final LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(nextExpiry)) return;
        try {
            final ClientCredentials clientCredentials = api.clientCredentialsGrant().build().get();
            api.setAccessToken(clientCredentials.getAccessToken());
            nextExpiry = now.plusSeconds(clientCredentials.getExpiresIn());
        } catch (final IOException | WebApiException exception) {
            throw new TokenRefreshException(exception);
        }
    }
}
