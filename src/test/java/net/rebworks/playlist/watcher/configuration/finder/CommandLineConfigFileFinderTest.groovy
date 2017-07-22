package net.rebworks.playlist.watcher.configuration.finder

import spock.lang.Specification


class CommandLineConfigFileFinderTest extends Specification {
    def "getConfigFile returns failure if no command line arguments are given"() {
        given: "A command line config finder with empty arguments"
        final configFileFinder = new CommandLineConfigFileFinder(new FileCheckerImpl(), new String[0])
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result is a failure"
        configFile.failure
        and: "There is an error message"
        !configFile.errors.empty
    }

    def "getConfigFile returns failure if file checker returns an error"() {
        given: "A command line config finder with a failing file checker"
        final checker = {_ -> Optional.of("Error message")} as FileChecker
        final String[] argv = ["someFile.yaml"]
        final configFileFinder = new CommandLineConfigFileFinder(checker, argv)
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result is a failure"
        configFile.failure
        and: "Errors contains the message from the checker"
        configFile.errors.contains("Error message")
    }

    def "getConfigFile returns success if there are command line arguments and file checker returns no error"(){
        given: "A command line config finder with a failing file checker"
        final checker = {_ -> Optional.empty()} as FileChecker
        final String[] argv = ["someFile.yaml"]
        final configFileFinder = new CommandLineConfigFileFinder(checker, argv)
        when: "The config file is retrieved"
        final configFile = configFileFinder.configFile
        then: "The result is a failure"
        !configFile.failure
        and: "Errors contains the message from the checker"
        configFile.errors.empty
    }
}
