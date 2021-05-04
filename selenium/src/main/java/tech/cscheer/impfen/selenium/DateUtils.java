package tech.cscheer.impfen.selenium;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DateUtils {
    public static List<ZonedDateTime> getNextTwoWeeksToCheck() {
        List<ZonedDateTime> nextTwoWeeks = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        IntStream.range(1, 15).forEach(day -> {
            ZonedDateTime datum = now.plusDays(day);
            nextTwoWeeks.add(datum);
        });

        return nextTwoWeeks;
    }
}
