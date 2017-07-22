package net.rebworks.playlist.watcher;

import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;
import com.wrapper.spotify.models.User;
import net.rebworks.playlist.watcher.ImmutableTrackInfo.Builder;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

import static java.util.stream.Collectors.joining;

@Value.Immutable
public interface TrackInfo extends Comparable<TrackInfo> {

    Comparator<TrackInfo> TRACK_INFO_COMPARATOR = Comparator.comparing(TrackInfo::getAddedAt)
                                                            .thenComparing(TrackInfo::getUserName)
                                                            .thenComparing(TrackInfo::getTitle)
                                                            .thenComparing(TrackInfo::getArtists)
                                                            .thenComparing(TrackInfo::isExplicit);

    LocalDateTime getAddedAt();

    String getUserName();

    String getTitle();

    String getArtists();

    boolean isExplicit();

    static TrackInfo from(final PlaylistTrack playlistTrack) {
        final User addedBy = playlistTrack.getAddedBy();
        final Track track = playlistTrack.getTrack();
        final String userName = addedBy.getDisplayName() == null ? addedBy.getId() : addedBy.getDisplayName();
        final String title = track.getName();
        final String artists = track.getArtists().stream().map(SimpleArtist::getName).collect(joining(", "));
        final boolean explicit = track.isExplicit();
        final LocalDateTime addedAt = LocalDateTime.ofInstant(playlistTrack.getAddedAt().toInstant(),
                                                              ZoneId.systemDefault());
        return builder().addedAt(addedAt).userName(userName).title(title).artists(artists).isExplicit(explicit).build();
    }

    static Builder builder() {
        return ImmutableTrackInfo.builder();
    }

    @Override
    default int compareTo(final TrackInfo other) {
        return TRACK_INFO_COMPARATOR.compare(this, other);
    }
}
