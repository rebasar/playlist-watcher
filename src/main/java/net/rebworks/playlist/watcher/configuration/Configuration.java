package net.rebworks.playlist.watcher.configuration;

import java.net.URI;

public interface Configuration {

    URI getWebhookUrl();

    String getSpotifyClientId();

    String getSpotifyClientSecret();

    String getPlaylistUsername();

    String getPlaylistId();

    Schedule getSchedule();

}
