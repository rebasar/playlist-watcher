package net.rebworks.playlist.watcher.daemon

import net.rebworks.playlist.watcher.TrackInfo
import spock.lang.Specification

import java.time.LocalDateTime

class PlaylistStateTest extends Specification {
    def "Update returns the difference between old and new sets"() {
        given: "A playlist state containing tracks A and B"
        final now = LocalDateTime.now()
        final trackA = createTrack(now, "Art", false, "A", "alice")
        final trackB = createTrack(now.minusMinutes(2), "Bill", true, "B", "bob")
        final playlistState = new PlaylistState(new TreeSet<TrackInfo>([trackA, trackB]))
        when: "The state is updated with tracks B and C"
        final trackC = createTrack(now.plusMinutes(2), "Clyde", false, "C", "eve")
        final addedTracks = playlistState.update(new TreeSet<TrackInfo>([trackB, trackC]))
        then: "The result should only include track C"
        addedTracks.size() == 1
        addedTracks.contains(trackC)
    }

    private static TrackInfo createTrack(LocalDateTime addedAt, String artists, boolean isExplicit, String title, String userName) {
        return TrackInfo.builder().
                addedAt(addedAt).
                artists(artists).
                isExplicit(isExplicit).
                title(title).
                userName(userName).
                build()
    }
}
