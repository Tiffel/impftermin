package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;

public class ZugangPage {
    static Logger log = LoggerFactory.getLogger(LandingPage.class);

    public static void handle(WebDriver driver, Wait<WebDriver> wait, String username, String password) {
        log.info("Zugangsseite wird geladen");
        wait.until(ZugangPage::zugangsdatenPresent);
        log.info("Zugangsseite geladen");
        log.info("Einloggen");
        getInputByLabel(driver, "Vorgangskennung*").sendKeys(username);
        getInputByLabel(driver, "Passwort*").sendKeys(password);
        getWeiterButton(driver).click();
        wait.until(d -> !zugangsdatenPresent(d));
        log.info("Eingeloggt");
    }

    private static WebElement getInputByLabel(WebDriver driver, String label) {
        return driver.findElements(By.tagName("div")).stream()
                .filter(SeleniumUtils.hasElement(By.tagName("label")).and(SeleniumUtils.hasElement(By.tagName("input"))))
                .filter(div -> div.findElement(By.tagName("label")).getText().equals(label))
                .map(div -> div.findElement(By.tagName("input")))
                .distinct().collect(SeleniumUtils.uniqueWebElementInListCollector());
    }

    private static WebElement getWeiterButton(WebDriver driver) {
        return driver.findElements(By.tagName("button"))
                .stream()
                .filter(hasElement(By.tagName("span")))
                .filter(button -> button.findElement(By.tagName("span")).getText().equals("WEITER"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector());
    }

    private static Boolean zugangsdatenPresent(WebDriver d) {
        return d.findElements(By.tagName("h3")).stream().anyMatch(webElement -> webElement.getAttribute("title").equals("Zugangsdaten"));
    }
}
