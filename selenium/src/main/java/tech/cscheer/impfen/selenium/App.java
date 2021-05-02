package tech.cscheer.impfen.selenium;

import static tech.cscheer.impfen.selenium.Environment.PORTAL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_USERNAME;
import static tech.cscheer.impfen.selenium.Environment.VACCINATION_CENTERS;

import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.IntStream;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import io.github.bonigarcia.wdm.WebDriverManager;
import tech.cscheer.impfen.selenium.page.AktionsauswahlPage;
import tech.cscheer.impfen.selenium.page.Impfzentrum;
import tech.cscheer.impfen.selenium.page.LandingPage;
import tech.cscheer.impfen.selenium.page.TerminfindungPage;
import tech.cscheer.impfen.selenium.page.TerminvergabePage;
import tech.cscheer.impfen.selenium.page.ZugangPage;

public class App {
    private static final long SLEEP_TIME_LEFT_LIMIT = Duration.ofMinutes(4).toSeconds();
    private static final long SLEEP_TIME_RIGHT_LIMIT = Duration.ofMinutes(6).toSeconds();

    public static void main(String[] args) throws InterruptedException {
        Environment.init();
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--verbose");
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        WebDriver driver = new ChromeDriver(options);
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        Wait<WebDriver> waitLong = new FluentWait<>(driver)
                .withTimeout(Duration.ofHours(1)) //Landingpage aktualisiert alle 30 Sekunden
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);


        LandingPage.handle(driver, waitLong);
        ZugangPage.handle(driver, wait, PORTAL_USERNAME, PORTAL_PASSWORD);
        AktionsauswahlPage.handle(driver, wait);

        for (int i = 0; i < VACCINATION_CENTERS.size(); i++) {
            //Gerüchten zufolge ist die "Ab" Suche der Webseite kaputt, deswegen suchen als "ab" in den nächsten 2 Wochen
            LocalDate today = LocalDate.now();
            Impfzentrum impfzentrum = VACCINATION_CENTERS.get(i);
            IntStream.range(1, 15).forEach(day -> {
                LocalDate datum = today.plusDays(day);
                TerminfindungPage.handle(driver, wait, impfzentrum, datum);
                TerminvergabePage.handle(driver, wait);
            });

            // Endlosschleife
            if (i == VACCINATION_CENTERS.size() - 1) {
                i = -1;
                Thread.sleep(randomSleepTime());
            }
        }
    }

    private static long randomSleepTime() {
        return (SLEEP_TIME_LEFT_LIMIT + (long) (Math.random() * (SLEEP_TIME_RIGHT_LIMIT - SLEEP_TIME_LEFT_LIMIT)))
                * 1000L;
    }
}
