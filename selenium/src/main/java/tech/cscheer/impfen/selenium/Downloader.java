package tech.cscheer.impfen.selenium;


import static tech.cscheer.impfen.selenium.Environment.LINK_DATES_TO_CHECK;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader {
    private static final Logger log = LoggerFactory.getLogger(Downloader.class);

    public static List<ZonedDateTime> downloadDatesToCheck() {
        if (StringUtils.isBlank(LINK_DATES_TO_CHECK)) {
            return DateUtils.getNextTwoWeeksToCheck();
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(LINK_DATES_TO_CHECK))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return parseResponseAndSetDatesToCheck(response.body());
        } catch (Exception e) {
            log.error("Exception beim Download der Daten");
            e.printStackTrace();
            Mailer.sendMail("CORONI: Fehler",
                    "Beim Abholen der Dateliste gab es einen Fehler. Fallback wird angewandt.");
        }

        return DateUtils.getNextTwoWeeksToCheck();
    }

    private static List<ZonedDateTime> parseResponseAndSetDatesToCheck(String body) {
        if (StringUtils.isBlank(body)) {
            return DateUtils.getNextTwoWeeksToCheck();
        }

        try {
            return Arrays.stream(body.split(","))
                    .map(date -> {
                        date = date.trim();
                        LocalDate localDate = LocalDate.parse(date);
                        return ZonedDateTime.of(localDate, LocalTime.now(), ZoneId.of("Europe/Berlin"));
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Exception beim Parsen der Daten");
            e.printStackTrace();
            Mailer.sendMail("CORONI: Fehler",
                    "Beim Parsen der Dateliste gab es einen Fehler. Fallback wird angewandt.");
        }

        return DateUtils.getNextTwoWeeksToCheck();
    }
}


