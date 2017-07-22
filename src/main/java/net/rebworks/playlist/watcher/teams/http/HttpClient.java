package net.rebworks.playlist.watcher.teams.http;

import java.net.URI;

public interface HttpClient {

    HTTPResult post(final URI uri, final byte[] body);

}
