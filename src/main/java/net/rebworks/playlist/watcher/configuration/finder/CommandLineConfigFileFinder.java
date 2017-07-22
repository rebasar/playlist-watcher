package net.rebworks.playlist.watcher.configuration.finder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineConfigFileFinder implements ConfigFileFinder {
    private final FileChecker fileChecker;
    private final String[] argv;

    public CommandLineConfigFileFinder(final FileChecker fileChecker, final String[] argv) {
        this.fileChecker = fileChecker;
        this.argv = argv;
    }

    @Override
    public ConfigFileFinderResult getConfigFile() {
        if (argv.length < 1) return ConfigFileFinderResult.failure("A config file name should be specified");
        final Path path = Paths.get(argv[0]);
        return fileChecker.check(path)
                          .map(ConfigFileFinderResult::failure)
                          .orElseGet(() -> ConfigFileFinderResult.success(path));
    }
}
