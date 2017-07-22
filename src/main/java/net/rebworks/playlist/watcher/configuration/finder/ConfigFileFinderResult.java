package net.rebworks.playlist.watcher.configuration.finder;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface ConfigFileFinderResult {

    class Success implements ConfigFileFinderResult {

        private final Path path;

        public Success(final Path path) {
            this.path = path;
        }


        @Override
        public Optional<Path> getConfigFile() {
            return Optional.of(path);
        }


    }
    class Failure implements ConfigFileFinderResult {

        private final List<String> errors;

        public Failure(final List<String> errors) {
            this.errors = errors;
        }

        @Override
        public Optional<Path> getConfigFile() {
            return Optional.empty();
        }

        @Override
        public List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }


    }
    Optional<Path> getConfigFile();

    default boolean isFailure() {
        return !getConfigFile().isPresent();
    }

    default List<String> getErrors() {
        return Collections.emptyList();
    }

    default ConfigFileFinderResult otherwise(final Supplier<ConfigFileFinderResult> configFileFinderResultSupplier) {
        if (getConfigFile().isPresent()) return this;
        else return configFileFinderResultSupplier.get();
    }

    static ConfigFileFinderResult success(final Path path) {
        return new Success(path);
    }

    static ConfigFileFinderResult failure(final String... errors) {
        return new Failure(Arrays.asList(errors));
    }

}
