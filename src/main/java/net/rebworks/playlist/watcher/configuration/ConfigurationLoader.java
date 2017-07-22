package net.rebworks.playlist.watcher.configuration;

import com.yacl4j.core.ConfigurationBuilder;

import java.nio.file.Path;

public class ConfigurationLoader {

    public Configuration load(final Path path) {
        return ConfigurationBuilder.newBuilder()
                                   .source()
                                   .fromFile(path.toAbsolutePath().toFile())
                                   .build(Configuration.class);
    }

}
