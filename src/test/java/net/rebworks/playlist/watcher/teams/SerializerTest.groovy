package net.rebworks.playlist.watcher.teams

import spock.lang.Specification


class SerializerTest extends Specification {

    /**
     * Brittle test, it might break whenever Jackson changes its mind about field processing order
     */
    def "Serialize serializes according to spec and without whitespace"() {
        given: "A Card with a title and a serializer"
        final card = Card.builder().text("Lorem ipsum").title("Lorem title").build()
        final serializer = new Serializer()
        when: "The card is serialized"
        final bytes = serializer.serialize(card)
        then: "The output should be compact"
        and: "It should conform to the MessageCard spec"
        final expectedText = '{"title":"Lorem title","text":"Lorem ipsum","themeColor":"1ed65f","@context":"http://schema.org/extensions","@type":"MessageCard"}'
        new String(bytes) == expectedText
    }
}
