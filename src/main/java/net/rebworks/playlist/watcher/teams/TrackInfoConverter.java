package net.rebworks.playlist.watcher.teams;

import net.rebworks.playlist.watcher.TrackInfo;

import java.util.function.Function;

public class TrackInfoConverter implements Function<TrackInfo, Card> {

    @Override
    public Card apply(final TrackInfo trackInfo){
        final String naughty = trackInfo.isExplicit() ? " naughty" : "";
        final String title = String.format("%s added a%s song", trackInfo.getUserName(), naughty);
        final String body = String.format("%s added %s by %s.%s",
                                          trackInfo.getUserName(),
                                          trackInfo.getTitle(),
                                          trackInfo.getArtists(),
                                          trackInfo.isExplicit() ? " Which is a very naughty song." : "");
        return Card.builder().title(title).text(body).build();
    }
}
