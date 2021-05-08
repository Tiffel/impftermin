package tech.cscheer.impfen.selenium;

import static tech.cscheer.impfen.selenium.Environment.EMAIL_ON_STARTUP;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_USERNAME;
import static tech.cscheer.impfen.selenium.Environment.RESTART_ON_ERROR;
import static tech.cscheer.impfen.selenium.Environment.SLEEP_MILLIS_MAX;
import static tech.cscheer.impfen.selenium.Environment.SLEEP_MILLIS_MIN;
import static tech.cscheer.impfen.selenium.Environment.VACCINATION_CENTERS;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static WebDriver driver;
    private static Wait<WebDriver> waitInApplication;
    private static Wait<WebDriver> waitForLogin;
    private static Wait<WebDriver> waitOnLandingpage;

    public static void main(String[] args) {
        Environment.init();
        log.info("startup");
        if (EMAIL_ON_STARTUP) {
            log.info("send statup mail");
            Mailer.sendMail("CORONI: Info", "Hello, i am running!");
        }
        startWebdriver();

        LandingPage.handle(driver, waitOnLandingpage);
        ZugangPage.handle(driver, waitForLogin, PORTAL_USERNAME, PORTAL_PASSWORD);
        AktionsauswahlPage.handle(driver, waitForLogin);

        // Endlosschleife für den Restart im Fehlerfall
        while (true) {
            try {
                // Endlosschleife für die Impfzentren mit Exit im Fehlerfall
                for (Impfzentrum impfzentrum : VACCINATION_CENTERS) {
                    TerminfindungPage.handle(driver, waitInApplication, impfzentrum);
                    TerminvergabePage.handle(driver, waitInApplication);
                }

                long sleep = randomSleepTime();
                log.info("Schlafe für " + (sleep / 1000) / 60 + " Minuten");
                Thread.sleep(sleep);
            } catch (Exception e) {
                log.error("Exception. This should never happen :)", e);
                e.printStackTrace();
                try {
                    ExceptionUtils.checkFor503(driver, waitInApplication, waitForLogin);
                } catch (Exception no503) {
                    try {
                        ExceptionUtils.tryToRefresh(driver, waitForLogin);
                    } catch (Exception refreshFailed) {
                        if (RESTART_ON_ERROR) {
                            Mailer.sendMail("CORONI: Fehler",
                                    "Exception, someting went wrong. please check me! Restart as Fallback.");
                            log.warn("restarting");
                            resetWebdriver(driver);
                            startWebdriver();
                            LandingPage.handle(driver, waitOnLandingpage);
                            ZugangPage.handle(driver, waitForLogin, PORTAL_USERNAME, PORTAL_PASSWORD);
                            AktionsauswahlPage.handle(driver, waitForLogin);
                        } else {
                            log.warn("Alles kaputt");
                            Mailer.sendMail("CORONI: Fehler",
                                    "Exception, someting went wrong. please check me! No Restart");
                        }
                    }
                }

            }
        }
    }

    private static void startWebdriver() {
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--verbose");
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        driver = new ChromeDriver(options);
        waitInApplication = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(5))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        waitForLogin = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(2))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        waitOnLandingpage = new FluentWait<>(driver)
                .withTimeout(Duration.ofHours(
                        5)) // Das kann lange dauert, ist aber nur ein http-equip Refresh. Da geht nix schief
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        log.info("webdriver startup");
    }

    private static long randomSleepTime() {
        return SLEEP_MILLIS_MIN + (long) (Math.random() * (SLEEP_MILLIS_MAX - SLEEP_MILLIS_MIN));
    }

    private static void resetWebdriver(WebDriver driver) {
        driver.manage().deleteAllCookies();
        driver.quit();
    }
}
