package net.rebworks.playlist.watcher.configuration.finder

import spock.lang.Specification


class DefaultConfigFileFinderTest extends Specification {
    def "getConfigFile returns failure if the checker returns an error"() {
        given: "A default config finder with a failing checker"
        final checker = { _ -> Optional.of("Error message") } as FileChecker
        final configFileFinder = new DefaultConfigFileFinder(checker, "test-file")
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result should be a failure"
        configFile.failure
        and: "Errors should contain the error message"
        configFile.errors.contains("Error message")
    }

    def "getConfigFile returns success if the checker returns no error"() {
        given: "A default config finder with a successful checker"
        final checker = { _ -> Optional.empty() } as FileChecker
        final configFileFinder = new DefaultConfigFileFinder(checker, "test-file")
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result should be success"
        !configFile.failure
        and: "There should be no errors"
        configFile.errors.empty
    }

    def "getConfigFile passes the given path to the checker"() {
        given: "A default config finder with a test checker"
        final checker = { path ->
            path.toString() == "test-file" ? Optional.empty() : Optional.of("Error!")
        } as FileChecker
        final configFileFinder = new DefaultConfigFileFinder(checker, "test-file")
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result should be success"
        !configFile.failure
    }
}
