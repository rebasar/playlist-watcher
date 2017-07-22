package net.rebworks.playlist.watcher.configuration.finder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultConfigFileFinder implements ConfigFileFinder {

    private final FileChecker fileChecker;
    private final String defaultConfigFileName;

    public DefaultConfigFileFinder(final FileChecker fileChecker, final String defaultConfigFileName) {
        this.fileChecker = fileChecker;
        this.defaultConfigFileName = defaultConfigFileName;
    }

    @Override
    public ConfigFileFinderResult getConfigFile() {
        final Path path = Paths.get(defaultConfigFileName);
        return fileChecker.check(path)
                          .map(ConfigFileFinderResult::failure)
                          .orElseGet(() -> ConfigFileFinderResult.success(path));
    }
}
