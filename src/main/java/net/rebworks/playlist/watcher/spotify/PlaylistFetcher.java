package net.rebworks.playlist.watcher.spotify;

import net.rebworks.playlist.watcher.TrackInfo;

import java.util.stream.Stream;

@FunctionalInterface
public interface PlaylistFetcher {
    Stream<TrackInfo> fetch(PlaylistId playlist);
}
