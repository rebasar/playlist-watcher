package net.rebworks.playlist.watcher;

import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Episode;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;
import net.rebworks.playlist.watcher.ImmutableTrackInfo.Builder;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.stream.Stream;

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
        final String userName = addedBy.getDisplayName() == null ? addedBy.getId() : addedBy.getDisplayName();
        final IPlaylistItem track = playlistTrack.getTrack();
        final LocalDateTime addedAt = LocalDateTime.ofInstant(playlistTrack.getAddedAt().toInstant(),
                                                              ZoneId.systemDefault());
        final Builder builder = builder().userName(userName).addedAt(addedAt);
        if (track instanceof Track) {
            return populateTrackInfo((Track) track, builder);
        } else if (track instanceof Episode){
            return populateTrackInfo((Episode) track, builder);
        } else {
            return builder.title("Unknown Title").artists("Unknown Artist").isExplicit(false).build();
        }
    }

    static TrackInfo populateTrackInfo(final Track track, final Builder builder) {
        final String title = track.getName();
        final String artists = Stream.of(track.getArtists()).map(ArtistSimplified::getName).collect(joining(", "));
        final boolean explicit = track.getIsExplicit();
        return builder.title(title).artists(artists).isExplicit(explicit).build();
    }

    static TrackInfo populateTrackInfo(final Episode episode, final Builder builder) {
        final String title = episode.getName();
        final String artists = episode.getShow().getName();
        final boolean explicit = episode.getExplicit();
        return builder.title(title).artists(artists).isExplicit(explicit).build();
    }

    static Builder builder() {
        return ImmutableTrackInfo.builder();
    }

    @Override
    default int compareTo(final TrackInfo other) {
        return TRACK_INFO_COMPARATOR.compare(this, other);
    }
}
