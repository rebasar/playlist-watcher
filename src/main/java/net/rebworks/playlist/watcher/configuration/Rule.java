package net.rebworks.playlist.watcher.configuration;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

interface Rule {

    DayOfWeek getDay();

    int getStart();

    int getEnd();

    int getInterval();

    default boolean matches(final LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().equals(getDay())
                && localDateTime.getHour() >= getStart()
                && localDateTime.getHour() < getEnd();
    }

}
