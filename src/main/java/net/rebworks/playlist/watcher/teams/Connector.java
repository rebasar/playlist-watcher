package net.rebworks.playlist.watcher.teams;

import net.rebworks.playlist.watcher.teams.http.HTTPResult;
import net.rebworks.playlist.watcher.teams.http.HttpClient;

import java.net.URI;
import java.util.function.Function;

public class Connector implements Function<Card, HTTPResult> {

    private final URI uri;
    private final Serializer serializer;
    private final HttpClient httpClient;

    public Connector(final URI uri, final Serializer serializer, final HttpClient httpClient) {
        this.uri = uri;
        this.serializer = serializer;
        this.httpClient = httpClient;
    }

    @Override
    public HTTPResult apply(final Card card) {
        final byte[] serializedCard = serializer.serialize(card);
        return httpClient.post(uri, serializedCard);
    }
}
