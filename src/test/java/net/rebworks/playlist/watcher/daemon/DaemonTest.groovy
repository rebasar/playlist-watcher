package net.rebworks.playlist.watcher.daemon

import net.rebworks.playlist.watcher.TrackInfo
import net.rebworks.playlist.watcher.daemon.sleep.SleepStrategy
import net.rebworks.playlist.watcher.spotify.PlaylistFetcher
import net.rebworks.playlist.watcher.spotify.PlaylistId
import net.rebworks.playlist.watcher.spotify.exceptions.PlaylistFetchException
import net.rebworks.playlist.watcher.spotify.exceptions.TokenRefreshException
import net.rebworks.playlist.watcher.teams.exceptions.SerializationException
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.function.Consumer
import java.util.stream.Stream

class DaemonTest extends Specification {
    def "run returns TERMINATE upon serialization exception"() {
        given: "A daemon where the consumer throws SerializationException"
        final exception = new SerializationException(new Exception())
        final daemon = new Daemon(createPlaylistFetcher(Stream.of(createTrackInfo())),
                new PlaylistId("", ""),
                new PlaylistState(new TreeSet([])),
                createConsumer(exception),
                createSleepStrategy(1))
        when: "The daemon is run"
        final result = daemon.run()
        then: "The result is TERMINATE"
        result == TaskResult.TERMINATE
    }

    def "run returns CONTINUE upon playlist fetch exception"() {
        given: "A daemon where the fetcher throws throws PlaylistFetchException"
        final exception = new PlaylistFetchException(new Exception())
        final daemon = new Daemon(createPlaylistFetcher(exception),
                new PlaylistId("", ""),
                new PlaylistState(new TreeSet([])),
                createConsumer(),
                createSleepStrategy(1L))
        when: "The daemon is run"
        final result = daemon.run()
        then: "The result is CONTINUE"
        result == TaskResult.CONTINUE
    }

    def "run returns CONTINUE upon token refresh exception"() {
        given: "A daemon where the fetcher throws throws TokenRefreshException"
        final exception = new TokenRefreshException(new Exception())
        final daemon = new Daemon(createPlaylistFetcher(exception),
                new PlaylistId("", ""),
                new PlaylistState(new TreeSet([])),
                createConsumer(),
                createSleepStrategy(1L))
        when: "The daemon is run"
        final result = daemon.run()
        then: "The result is CONTINUE"
        result == TaskResult.CONTINUE
    }

    private static TrackInfo createTrackInfo() {
        return TrackInfo.builder().
                addedAt(LocalDateTime.now()).
                artists("Jane Doe").
                isExplicit(false).
                title("Title").
                userName("Joe User").
                build()
    }

    private static SleepStrategy createSleepStrategy(final long duration) {
        return { -> duration }
    }

    private static PlaylistFetcher createPlaylistFetcher(final Stream<TrackInfo> stream) {
        return { _ -> stream } as PlaylistFetcher
    }

    private static PlaylistFetcher createPlaylistFetcher(final Exception exception) {
        return { _ -> throw exception } as PlaylistFetcher
    }

    private static Consumer<TrackInfo> createConsumer(final SerializationException exception) {
        {_ -> throw exception } as Consumer<TrackInfo>
    }

    private static Consumer<TrackInfo> createConsumer() {
        {_ -> } as Consumer<TrackInfo>
    }
}
