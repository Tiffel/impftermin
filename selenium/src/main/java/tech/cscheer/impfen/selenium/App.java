package tech.cscheer.impfen.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.page.AktionsauswahlPage;
import tech.cscheer.impfen.selenium.page.Impfzentrum;
import tech.cscheer.impfen.selenium.page.LandingPage;
import tech.cscheer.impfen.selenium.page.TerminfindungPage;
import tech.cscheer.impfen.selenium.page.TerminvergabePage;
import tech.cscheer.impfen.selenium.page.ZugangPage;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import static tech.cscheer.impfen.selenium.Environment.EMAIL_ON_STARTUP;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_USERNAME;
import static tech.cscheer.impfen.selenium.Environment.VACCINATION_CENTERS;

public class App {
    private static final long SLEEP_TIME_LEFT_LIMIT = Duration.ofMinutes(1).toMillis();
    private static final long SLEEP_TIME_RIGHT_LIMIT = Duration.ofMinutes(3).toMillis();

    public static void main(String[] args) {
        Environment.init();
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--verbose");
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        WebDriver driver = new ChromeDriver(options);
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofHours(10)) //Landingpage aktualisiert alle 30 Sekunden
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        if (EMAIL_ON_STARTUP) {
            Mailer.sendMail("Hello, i am running!");
        }

        try {
            LandingPage.handle(driver, wait);
            ZugangPage.handle(driver, wait, PORTAL_USERNAME, PORTAL_PASSWORD);
            AktionsauswahlPage.handle(driver, wait);

            while (true) {
                for (Impfzentrum impfzentrum : VACCINATION_CENTERS) {
                    // Gerüchten zufolge ist die "Ab" Suche der Webseite kaputt, deswegen suchen als "ab" in den nächsten 2 Wochen
                    IntStream.range(1, 15).forEach(day -> {
                        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
                        ZonedDateTime datum = zonedDateTime.plusDays(day);
                        TerminfindungPage.handle(driver, wait, impfzentrum, datum);
                        TerminvergabePage.handle(driver, wait);
                    });
                }
                try {
                    Thread.sleep(randomSleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            Mailer.sendMail("someting went wrong. please check me!");
        }


    }

    private static long randomSleepTime() {
        return SLEEP_TIME_LEFT_LIMIT + (long) (Math.random() * (SLEEP_TIME_RIGHT_LIMIT - SLEEP_TIME_LEFT_LIMIT));
    }
}
