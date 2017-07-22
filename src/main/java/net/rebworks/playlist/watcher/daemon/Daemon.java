package net.rebworks.playlist.watcher.daemon;

import net.rebworks.playlist.watcher.TrackInfo;
import net.rebworks.playlist.watcher.daemon.sleep.SleepStrategy;
import net.rebworks.playlist.watcher.spotify.PlaylistFetcher;
import net.rebworks.playlist.watcher.spotify.PlaylistId;
import net.rebworks.playlist.watcher.spotify.exceptions.PlaylistFetchException;
import net.rebworks.playlist.watcher.spotify.exceptions.TokenRefreshException;
import net.rebworks.playlist.watcher.teams.exceptions.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

public class Daemon implements Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(Daemon.class);

    private final PlaylistFetcher fetcher;
    private final PlaylistId playlistId;
    private final PlaylistState state;
    private final Consumer<TrackInfo> consumer;
    private final SleepStrategy sleepStrategy;

    public Daemon(final PlaylistFetcher fetcher, final PlaylistId playlistId, final PlaylistState state, final Consumer<TrackInfo> consumer, final SleepStrategy sleepStrategy) {
        this.fetcher = fetcher;
        this.playlistId = playlistId;
        this.state = state;
        this.consumer = consumer;
        this.sleepStrategy = sleepStrategy;
    }

    public TaskResult run() {

        try {
            LOGGER.debug("Start fetching current playlist");
            final SortedSet<TrackInfo> fetched = fetcher.fetch(playlistId).collect(toCollection(TreeSet::new));
            final SortedSet<TrackInfo> difference = state.update(fetched);
            logDifference(difference);
            difference.forEach(consumer);
        } catch (final PlaylistFetchException e) {
            handleException(e, "Unable to fetch the playlist. Will retry in %s milliseconds");
        } catch (final TokenRefreshException e) {
            handleException(e, "Unable to refresh the authentication token. Will retry in %s milliseconds");
        } catch (final SerializationException e) {
            LOGGER.error("Cannot serialize Microsoft Teams message", e);
            return TaskResult.TERMINATE;
        }
        return TaskResult.CONTINUE;
    }

    private void logDifference(final SortedSet<TrackInfo> difference) {
        if (LOGGER.isDebugEnabled()) {
            final String diff = difference.isEmpty() ? "none" : difference.stream()
                                                                          .map(TrackInfo::toString)
                                                                          .collect(joining(", "));
            LOGGER.debug("Difference: {}", diff);
        }
    }

    private void handleException(final Exception exception, final String messageTemplate) {
        final long sleepDuration = sleepStrategy.calculate();
        final String infoMessage = String.format(
                messageTemplate,
                sleepDuration);
        LOGGER.info(infoMessage, exception);
    }
}
