package net.rebworks.playlist.watcher.daemon.sleep;

import net.rebworks.playlist.watcher.configuration.Schedule;

import java.time.LocalDateTime;

public interface SleepStrategy {

    long calculate();

    static SleepStrategy configurable(final Schedule schedule) {
        return new ConfigurableSleepStrategy(LocalDateTime::now, schedule);
    }
}
