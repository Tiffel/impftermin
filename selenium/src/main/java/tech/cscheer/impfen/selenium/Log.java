package tech.cscheer.impfen.selenium;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    public static void info(String text) {
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()) + " " + text);
    }
}
