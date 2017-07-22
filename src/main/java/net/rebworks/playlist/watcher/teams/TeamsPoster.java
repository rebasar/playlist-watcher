package net.rebworks.playlist.watcher.teams;

import net.rebworks.playlist.watcher.TrackInfo;
import net.rebworks.playlist.watcher.teams.http.HTTPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class TeamsPoster implements Consumer<TrackInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamsPoster.class);
    private final Function<Card, HTTPResult> connector;
    private final Function<TrackInfo, Card> converter;

    public TeamsPoster(final Function<Card, HTTPResult> connector, final Function<TrackInfo, Card> converter) {
        this.connector = connector;
        this.converter = converter;
    }

    @Override
    public void accept(final TrackInfo trackInfo) {
        final HTTPResult result = converter.andThen(connector).apply(trackInfo);
        processResult(result);
    }

    private void processResult(final HTTPResult result) {
        if (!result.isSuccess()){
            LOGGER.warn("Posting to Microsoft Teams failed with message: {}\n", result.getMessage());
            result.getException().ifPresent(e -> LOGGER.error("Exception occurred while posting to Microsoft Teams", e));
        }
    }
}
