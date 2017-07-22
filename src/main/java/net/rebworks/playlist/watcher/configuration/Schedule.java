package net.rebworks.playlist.watcher.configuration;

import java.time.LocalDateTime;
import java.util.List;

public interface Schedule {

    int getDefault();

    List<Rule> getRules();

    default int getSleepDuration(final LocalDateTime localDateTime) {
        return getRules().stream()
                         .filter(rule -> rule.matches(localDateTime))
                         .findFirst()
                         .map(Rule::getInterval)
                         .orElseGet(this::getDefault);
    }
}
