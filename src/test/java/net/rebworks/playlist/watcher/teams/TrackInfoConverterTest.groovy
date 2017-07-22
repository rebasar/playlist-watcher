package net.rebworks.playlist.watcher.teams

import net.rebworks.playlist.watcher.TrackInfo
import spock.lang.Specification

import java.time.LocalDateTime


class TrackInfoConverterTest extends Specification {
    def "Converter converts non-explicit songs normally"() {
        given: "A a non-explicit track"
        final trackInfo = TrackInfo.builder().
                userName("user").
                title("Ordinary Song").
                isExplicit(false).
                artists("John Doe").
                addedAt(LocalDateTime.now()).
                build()
        when: "The the track is converted to a card"
        final converter = new TrackInfoConverter()
        final card = converter.apply(trackInfo)
        then: "The card should be formatted correctly"
        card.title == Optional.of("user added a song")
        card.text == "user added Ordinary Song by John Doe."
    }

    def "Converter adds naughtiness to explicit songs"() {
        given: "A an explicit track"
        final trackInfo = TrackInfo.builder().
                userName("user").
                title("Naughty Song").
                isExplicit(true).
                artists("Jane Doe").
                addedAt(LocalDateTime.now()).
                build()
        when: "The the track is converted to a card"
        final converter = new TrackInfoConverter()
        final card = converter.apply(trackInfo)
        then: "The card should include naughtiness info"
        card.title == Optional.of("user added a naughty song")
        card.text == "user added Naughty Song by Jane Doe. Which is a very naughty song."
    }
}
