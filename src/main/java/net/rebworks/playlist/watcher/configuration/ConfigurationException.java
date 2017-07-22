package net.rebworks.playlist.watcher.configuration;

class ConfigurationException extends RuntimeException {
    public ConfigurationException(final Exception exception) {
        super(exception);
    }
}
