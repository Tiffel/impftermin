package tech.cscheer.impfen.selenium;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtils {
    public static List<ZonedDateTime> getNextTwoWeeksToCheck() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        return IntStream.range(1, 15)
                .mapToObj(now::plusDays)
                .collect(Collectors.toList());
    }
}
