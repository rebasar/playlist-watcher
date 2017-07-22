package net.rebworks.playlist.watcher.daemon.sleep;

import net.rebworks.playlist.watcher.configuration.Schedule;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class ConfigurableSleepStrategy implements SleepStrategy {

    private final Supplier<LocalDateTime> localDateTimeSupplier;
    private final Schedule schedule;

    ConfigurableSleepStrategy(final Supplier<LocalDateTime> localDateTimeSupplier, final Schedule schedule) {
        this.localDateTimeSupplier = localDateTimeSupplier;
        this.schedule = schedule;
    }

    @Override
    public long calculate() {
        final LocalDateTime now = localDateTimeSupplier.get();
        return schedule.getSleepDuration(now) * 1000;
    }
}
