package net.rebworks.playlist.watcher.configuration

import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean


class ScheduleTest extends Specification {
    def "getSleepDuration returns the result of a matching rule, if there is any"() {
        given: "A schedule with a matching and a non-matching rule"
        final schedule = createSchedule(1800,
                [createRule(DayOfWeek.FRIDAY, 8, 13, 600),
                 createRule(DayOfWeek.MONDAY, 14, 18, 3600)])
        when: "The sleep time is calculated"
        final duration = schedule.getSleepDuration(LocalDateTime.of(2017, 7, 21, 10, 0))
        then: "The result of the matching rule should be returned"
        duration == 600
    }

    def "getSleepDuration does not invoke a rule, if a previous one matches"() {
        given: "A schedule with a matching and a non-matching rule"
        final AtomicBoolean invoked = new AtomicBoolean(false)
        final schedule = createSchedule(1800,
                [createRule(DayOfWeek.FRIDAY, 8, 13, 600),
                 createRuleWithTracking(invoked, DayOfWeek.MONDAY, 14, 18, 3600)])
        when: "The sleep time is calculated"
        final duration = schedule.getSleepDuration(LocalDateTime.of(2017, 7, 21, 10, 0))
        then: "The result of the matching rule should be returned"
        duration == 600
        and: "The second rule should not be invoked"
        !invoked.get()
    }

    def "getSleepDuration returns the default value if no rules match"(){
        given: "A schedule two non-matching rules"
        final schedule = createSchedule(1800,
                [createRule(DayOfWeek.WEDNESDAY, 8, 13, 600),
                 createRule(DayOfWeek.MONDAY, 14, 18, 3600)])
        when: "The sleep time is calculated"
        final duration = schedule.getSleepDuration(LocalDateTime.of(2017, 7, 21, 10, 0))
        then: "The default duration should be returned"
        duration == 1800
    }

    private static Schedule createSchedule(final int defaultSleep, final List<Rule> rules) {
        return new Schedule() {
            @Override
            int getDefault() {
                return defaultSleep
            }

            @Override
            List<Rule> getRules() {
                return rules
            }
        }
    }

    private static Rule createRule(final DayOfWeek dayOfWeek, final int start, final int end, final int duration) {
        return new Rule() {
            @Override
            DayOfWeek getDay() {
                return dayOfWeek
            }

            @Override
            int getStart() {
                return start
            }

            @Override
            int getEnd() {
                return end
            }

            @Override
            int getInterval() {
                return duration
            }
        }
    }

    private Rule createRuleWithTracking(final AtomicBoolean flag, final DayOfWeek dayOfWeek, final int start, final int end, final int duration) {
        return new Rule() {
            @Override
            DayOfWeek getDay() {
                return dayOfWeek
            }

            @Override
            int getStart() {
                return start
            }

            @Override
            int getEnd() {
                return end
            }

            @Override
            int getInterval() {
                return duration
            }

            @Override
            boolean matches(final LocalDateTime localDateTime) {
                flag.set(true)
                return super.matches(localDateTime)
            }
        }
    }
}
