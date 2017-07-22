package net.rebworks.playlist.watcher.configuration

import spock.lang.Specification
import spock.lang.Unroll

import java.time.DayOfWeek
import java.time.LocalDateTime


class RuleTest extends Specification {

    final DayOfWeek dayOfWeek = DayOfWeek.FRIDAY
    final int start = 8
    final int end = 13

    @Unroll
    def "The result for #localDateTime is #expected"(final LocalDateTime localDateTime, final boolean expected) {
        given: "A rule created with test conditions"
        final rule = createRule()
        when: "The rule is matched against the input date"
        final result = rule.matches(localDateTime)
        then: "It should result with the expected result"
        result == expected
        where:
        localDateTime | expected
        LocalDateTime.of(2017, 7, 20, 7, 59)  | false
        LocalDateTime.of(2017, 7, 20, 8, 0)   | false
        LocalDateTime.of(2017, 7, 20, 8, 1)   | false
        LocalDateTime.of(2017, 7, 20, 10, 0)  | false
        LocalDateTime.of(2017, 7, 20, 12, 59) | false
        LocalDateTime.of(2017, 7, 20, 13, 0)  | false
        LocalDateTime.of(2017, 7, 20, 13, 1)  | false
        LocalDateTime.of(2017, 7, 21, 7, 59)  | false
        LocalDateTime.of(2017, 7, 21, 8, 0)   | true
        LocalDateTime.of(2017, 7, 21, 8, 1)   | true
        LocalDateTime.of(2017, 7, 21, 10, 0)  | true
        LocalDateTime.of(2017, 7, 21, 12, 59) | true
        LocalDateTime.of(2017, 7, 21, 13, 0)  | false
        LocalDateTime.of(2017, 7, 21, 13, 1)  | false
        LocalDateTime.of(2017, 7, 22, 7, 59)  | false
        LocalDateTime.of(2017, 7, 22, 8, 0)   | false
        LocalDateTime.of(2017, 7, 22, 8, 1)   | false
        LocalDateTime.of(2017, 7, 22, 10, 0)  | false
        LocalDateTime.of(2017, 7, 22, 12, 59) | false
        LocalDateTime.of(2017, 7, 22, 13, 0)  | false
        LocalDateTime.of(2017, 7, 22, 13, 1)  | false
    }

    private Rule createRule() {
        return new Rule() {
            @Override
            DayOfWeek getDay() {
                return RuleTest.this.dayOfWeek
            }

            @Override
            int getStart() {
                return RuleTest.this.start
            }

            @Override
            int getEnd() {
                return RuleTest.this.end
            }

            @Override
            int getInterval() {
                return 0
            }
        }
    }
}
