package net.rebworks.playlist.watcher.configuration.finder;

@FunctionalInterface
public interface ConfigFileFinder {

    ConfigFileFinderResult getConfigFile();

    static ConfigFileFinder commandLineFinder(final String[] argv, final FileChecker fileChecker) {
        return new CommandLineConfigFileFinder(fileChecker, argv);
    }

    static ConfigFileFinder defaultFinder(final String defaultConfigFileName, final FileChecker fileChecker) {
        return new DefaultConfigFileFinder(fileChecker, defaultConfigFileName);
    }

    default ConfigFileFinder otherwise(final ConfigFileFinder configFileFinder) {
        return () -> getConfigFile().otherwise(configFileFinder::getConfigFile);
    }

}
