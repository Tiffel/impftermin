package tech.cscheer.impfen.selenium;

import static tech.cscheer.impfen.selenium.Environment.EMAIL_ON_STARTUP;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_USERNAME;
import static tech.cscheer.impfen.selenium.Environment.VACCINATION_CENTERS;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import tech.cscheer.impfen.selenium.page.AktionsauswahlPage;
import tech.cscheer.impfen.selenium.page.Impfzentrum;
import tech.cscheer.impfen.selenium.page.LandingPage;
import tech.cscheer.impfen.selenium.page.TerminfindungPage;
import tech.cscheer.impfen.selenium.page.TerminvergabePage;
import tech.cscheer.impfen.selenium.page.ZugangPage;

public class App {
    private static final long SLEEP_TIME_LEFT_LIMIT = Duration.ofMinutes(1).toMillis();
    private static final long SLEEP_TIME_RIGHT_LIMIT = Duration.ofMinutes(3).toMillis();
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Environment.init();
        if (EMAIL_ON_STARTUP) {
            log.info("send statup mail");
            Mailer.sendMail("CORONI: Info", "Hello, i am running!");
        }
        // Endlosschleife für den Restart im Fehlerfall
        while (true) {
            WebDriverManager.chromiumdriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--verbose");
            options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
            options.addArguments("--no-sandbox"); // Bypass OS security model
            WebDriver driver = new ChromeDriver(options);
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofMinutes(5))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(NoSuchElementException.class);
            Wait<WebDriver> waitLong = new FluentWait<>(driver)
                    .withTimeout(Duration.ofHours(5)) //Landingpage aktualisiert alle 30 Sekunden
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(NoSuchElementException.class);
            log.info("startup");

            try {
                LandingPage.handle(driver, waitLong);
                ZugangPage.handle(driver, wait, PORTAL_USERNAME, PORTAL_PASSWORD);
                AktionsauswahlPage.handle(driver, wait);

                boolean running = true;
                // Endlosschleife für die Impfzentren mit Exit im Fehlerfall
                while (running) {
                    try {
                        List<ZonedDateTime> datesToCheck = Downloader.downloadDatesToCheck();
                        for (Impfzentrum impfzentrum : VACCINATION_CENTERS) {
                            // Gerüchten zufolge ist die "Ab" Suche der Webseite kaputt, deswegen suchen als "ab" in den nächsten 2 Wochen
                            datesToCheck.stream()
                                    .filter(date -> date.toLocalDate()
                                            .isAfter(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).toLocalDate()))
                                    .forEach(date -> {
                                        TerminfindungPage.handle(driver, wait, impfzentrum, date);
                                        TerminvergabePage.handle(driver, wait);
                                    });
                        }

                        long sleep = randomSleepTime();
                        log.info("Schlafe für " + (sleep / 1000) / 60 + " Minuten");
                        Thread.sleep(sleep);
                    } catch (Exception e) {
                        log.error("Exception.", e);
                        e.printStackTrace();
                        Mailer.sendMail("CORONI: Fehler",
                                "Exception, someting went wrong. please check me! Restart as Fallback.");
                        reset(driver);
                        running = false;
                    }
                }
            } catch (Exception e) {
                log.error("Exception. This should never happen :)", e);
                e.printStackTrace();
                Mailer.sendMail("CORONI: Fehler",
                        "Exception, someting went wrong. please check me! Restart as Fallback.");
                reset(driver);
            }
        }
    }

    private static long randomSleepTime() {
        return SLEEP_TIME_LEFT_LIMIT + (long) (Math.random() * (SLEEP_TIME_RIGHT_LIMIT - SLEEP_TIME_LEFT_LIMIT));
    }

    private static void reset(WebDriver driver) {
        driver.manage().deleteAllCookies();
        driver.quit();
    }
}
