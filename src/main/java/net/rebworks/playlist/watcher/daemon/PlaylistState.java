package net.rebworks.playlist.watcher.daemon;

import net.rebworks.playlist.watcher.TrackInfo;

import java.util.SortedSet;
import java.util.TreeSet;

public class PlaylistState {

    private final SortedSet<TrackInfo> current = new TreeSet<>();

    public PlaylistState(final SortedSet<TrackInfo> trackInfos) {
        current.addAll(trackInfos);
    }

    public SortedSet<TrackInfo> update(final SortedSet<TrackInfo> updated){
        final SortedSet<TrackInfo> difference = new TreeSet<>(updated);
        difference.removeAll(current);
        current.clear();
        current.addAll(updated);
        return difference;
    }

}
