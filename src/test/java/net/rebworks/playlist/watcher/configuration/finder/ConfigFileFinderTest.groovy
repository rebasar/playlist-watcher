package net.rebworks.playlist.watcher.configuration.finder

import spock.lang.Specification

import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicBoolean


class ConfigFileFinderTest extends Specification {
    def "otherwise composes the results so that the second finder is only evaluated if the first fails"() {
        given: "Two config finders, where the first one is guaranteed to succeed"
        final invoked = new AtomicBoolean(false)
        final primary = { -> ConfigFileFinderResult.success(Paths.get("/config.file")) } as ConfigFileFinder
        final secondary = { ->
            invoked.set(true)
            ConfigFileFinderResult.failure("Should not be invoked!")
        } as ConfigFileFinder
        when: "They are composed"
        final configFileFinder = primary.otherwise(secondary)
        and: "invoked"
        def configFile = configFileFinder.configFile
        then: "The result should be a success"
        !configFile.failure
        and: "The second finder should not be invoked"
        !invoked.get()
    }

    def "The secondary finder is invoked, if the first one fails"(){
        given: "Two config finders, where the first one is guaranteed to fail"
        final invoked = new AtomicBoolean(false)
        final primary = { -> ConfigFileFinderResult.failure("Nope1") } as ConfigFileFinder
        final secondary = { ->
            invoked.set(true)
            ConfigFileFinderResult.success(Paths.get("/config.file"))
        } as ConfigFileFinder
        when: "They are composed"
        final configFileFinder = primary.otherwise(secondary)
        and: "invoked"
        def configFile = configFileFinder.configFile
        then: "The result should be a success"
        !configFile.failure
        and: "The second finder should be invoked"
        invoked.get()
    }
}
