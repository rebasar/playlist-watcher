package net.rebworks.playlist.watcher.configuration.finder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;

public class FileCheckerImpl implements FileChecker {
    @Override
    public Optional<String> check(final Path path){
        if (!Files.exists(path))
            return Optional.of(MessageFormat.format("File {0} does not exist", path));
        if (!Files.isRegularFile(path))
            return Optional.of(MessageFormat.format("{0} is not a regular file", path));
        return Optional.empty();
    }
}
