package tech.cscheer.impfen.selenium;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.PORTAL_USERNAME;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.cscheer.impfen.selenium.page.AbstractLoggedinPage;
import tech.cscheer.impfen.selenium.page.AktionsauswahlPage;
import tech.cscheer.impfen.selenium.page.ZugangPage;

public class ExceptionUtils {

    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void checkFor503(WebDriver driver, Wait<WebDriver> waitInApplication, Wait<WebDriver> waitForLogin)
            throws NoSuchElementException {
        if (driver.findElements(By.tagName("div")).stream()
                .filter(SeleniumUtils.hasAttribute("class", "gwt-HTML"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector())
                .getText()
                .contains("503")) {
            log.info("Fehler mit 503 Dialog. Versuche mit Ok zu bestätigen");
            //Errorpage. Versuch, den OK Button zu klicken
            WebElement okButton = driver.findElements(By.tagName("button")).stream()
                    .filter(SeleniumUtils.hasAttribute("type", "button"))
                    .filter(SeleniumUtils.hasElement(By.tagName("span")))
                    .filter(webElement -> webElement.findElement(By.tagName("span")).getText().contains("OK"))
                    .collect(SeleniumUtils.uniqueWebElementInListCollector());
            okButton.click();
            //die frage ist, wo landen wir nun? Raten wir mal die Ationsauswahl
            AktionsauswahlPage.handle(driver, waitInApplication);
        } else {
            log.warn("Unbekannter Fehler. Versuche Logout");
            AbstractLoggedinPage.logout(driver, waitForLogin);
            ZugangPage.handle(driver, waitForLogin, PORTAL_USERNAME, PORTAL_PASSWORD);
            AktionsauswahlPage.handle(driver, waitInApplication);
        }
    }

    public static void tryToRefresh(WebDriver driver, Wait<WebDriver> waitForLogin) throws NoSuchElementException {
        //Versuch, die Seite einfach neuzuladen, um die Session zu behalten.
        log.warn("Fehler bei der Fehlerbehandlung :(. Versuche Neuladen");
        driver.get(
                "https://sachsen.impfterminvergabe.de/civ.public/start.html?oe=00.00.IM&mode=cc&cc_key=IOAktion");
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException nape) {
            //na dann halt nicht.
        }
        waitForLogin.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Zugang"));
    }
}
