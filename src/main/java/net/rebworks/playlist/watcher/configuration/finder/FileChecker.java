package net.rebworks.playlist.watcher.configuration.finder;

import java.nio.file.Path;
import java.util.Optional;

@FunctionalInterface
public interface FileChecker {
    Optional<String> check(Path path);
}
