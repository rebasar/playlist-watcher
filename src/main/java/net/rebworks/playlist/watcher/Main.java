package net.rebworks.playlist.watcher;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import net.rebworks.playlist.watcher.configuration.finder.ConfigFileFinderResult;
import net.rebworks.playlist.watcher.configuration.Configuration;
import net.rebworks.playlist.watcher.configuration.ConfigurationLoader;
import net.rebworks.playlist.watcher.configuration.finder.FileChecker;
import net.rebworks.playlist.watcher.configuration.finder.FileCheckerImpl;
import net.rebworks.playlist.watcher.daemon.Daemon;
import net.rebworks.playlist.watcher.daemon.PlaylistState;
import net.rebworks.playlist.watcher.daemon.Runner;
import net.rebworks.playlist.watcher.daemon.sleep.SleepStrategy;
import net.rebworks.playlist.watcher.signal.SignalHandler;
import net.rebworks.playlist.watcher.spotify.PlaylistFetcher;
import net.rebworks.playlist.watcher.spotify.PlaylistFetcherImpl;
import net.rebworks.playlist.watcher.spotify.PlaylistId;
import net.rebworks.playlist.watcher.spotify.TokenManager;
import net.rebworks.playlist.watcher.teams.Connector;
import net.rebworks.playlist.watcher.teams.Serializer;
import net.rebworks.playlist.watcher.teams.TeamsPoster;
import net.rebworks.playlist.watcher.teams.TrackInfoConverter;
import net.rebworks.playlist.watcher.teams.http.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;
import static net.rebworks.playlist.watcher.configuration.finder.ConfigFileFinder.commandLineFinder;
import static net.rebworks.playlist.watcher.configuration.finder.ConfigFileFinder.defaultFinder;

class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] argv) throws IOException, SpotifyWebApiException, InterruptedException {
        final SignalHandler signalHandler = new SignalHandler();
        signalHandler.install();
        final FileChecker fileChecker = new FileCheckerImpl();
        final ConfigFileFinderResult finderResult = commandLineFinder(argv, fileChecker)
                .otherwise(defaultFinder("playlist-watcher.yaml", fileChecker))
                .getConfigFile();
        if(finderResult.isFailure()){
            LOGGER.error("Unable to start due to errors");
            finderResult.getErrors().forEach(LOGGER::error);
            return;
        }
        final Configuration configuration = new ConfigurationLoader().load(finderResult.getConfigFile().get());
        final PlaylistId playlistId = new PlaylistId(configuration.getPlaylistUsername(),
                                                     configuration.getPlaylistId());
        final PlaylistFetcher fetcher = initializeFetcher(configuration);
        final TeamsPoster poster = initializePoster(configuration);
        final PlaylistState state = initializeState(playlistId, fetcher);
        final SleepStrategy sleepStrategy = SleepStrategy.configurable(configuration.getSchedule());
        final Daemon daemon = new Daemon(fetcher, playlistId, state, poster, sleepStrategy);
        final Runner runner = new Runner(daemon, sleepStrategy);
        runner.run();
    }

    private static PlaylistState initializeState(final PlaylistId playlistId, final PlaylistFetcher fetcher) {
        final SortedSet<TrackInfo> trackInfos = fetcher.fetch(playlistId).collect(toCollection(TreeSet::new));
        return new PlaylistState(trackInfos);
    }

    private static TeamsPoster initializePoster(final Configuration configuration) {
        final Connector connector = new Connector(configuration.getWebhookUrl(),
                                                  new Serializer(),
                                                  new DefaultHttpClient());
        return new TeamsPoster(connector, new TrackInfoConverter());
    }

    private static PlaylistFetcher initializeFetcher(final Configuration configuration) {
        final SpotifyApi api = SpotifyApi.builder()
                           .setClientId(configuration.getSpotifyClientId())
                           .setClientSecret(configuration.getSpotifyClientSecret())
                           .build();
        final TokenManager tokenManager = new TokenManager(api);
        return new PlaylistFetcherImpl(api, tokenManager);
    }

}
