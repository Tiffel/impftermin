package tech.cscheer.impfen.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.page.AktionsauswahlPage;
import tech.cscheer.impfen.selenium.page.Impfzentrum;
import tech.cscheer.impfen.selenium.page.LandingPage;
import tech.cscheer.impfen.selenium.page.TerminfindungPage;
import tech.cscheer.impfen.selenium.page.TerminvergabePage;
import tech.cscheer.impfen.selenium.page.ZugangPage;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        WebDriverManager.chromiumdriver().setup();
        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        Wait<WebDriver> waitLong = new FluentWait<>(driver)
                .withTimeout(Duration.ofHours(1)) //Landingpage aktualisiert alle 30 Sekunden
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        ConfigProperties configProperties = new ConfigProperties();


        LandingPage.handle(driver, waitLong);
        ZugangPage.handle(driver, wait, configProperties.getUsername(), configProperties.getPassword());
        AktionsauswahlPage.handle(driver, wait);

        for (int i = 0; i < configProperties.getImpfzentren().size(); i++) {
            Impfzentrum impfzentrum = configProperties.getImpfzentren().get(i);
            TerminfindungPage.handle(driver, wait, impfzentrum);
            TerminvergabePage.handle(driver, wait);

            // Endlosschleife
            if (i == configProperties.getImpfzentren().size() - 1) {
                i = -1;
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
            }
        }
    }
}
