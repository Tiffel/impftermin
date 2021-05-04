package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.Mailer;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_ENABLED;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;

public class TerminvergabePage extends AbstractLoggedinPage {
    private static Logger log = LoggerFactory.getLogger(TerminvergabePage.class);

    public static void handle(WebDriver driver, Wait<WebDriver> wait) {
        wait.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Terminvergabe"));
        if (isKeineTermineFrei(driver)) {
            //schade
            log.info("Keine Termine Frei :(");
            getZurueckButton(driver).click();
        } else {
            if (EMAIL_ENABLED) {
                Mailer.sendMail("DEIN TERMIN IS DA");
            }
            //Glückwunsch? Keine Ahnung wie die Seite nun aussieht.
            log.info("wohoooo. Impftermin mgwl. verfügbar. Beenden");

            //Java Beenden, Browser bleibt offen
            System.exit(0);
        }
    }

    private static boolean isKeineTermineFrei(WebDriver driver) {
        return driver.findElements(By.tagName("div"))
                .stream().filter(hasElement(By.tagName("h2")).and(hasElement(By.tagName("div"))))
                .flatMap(div -> div.findElements(By.tagName("div")).stream())
                .map(WebElement::getText)
                .anyMatch(text -> text.contains("können wir Ihnen leider keinen Termin anbieten"));
    }

    private static WebElement getZurueckButton(WebDriver driver) {
        return driver.findElements(By.tagName("button"))
                .stream()
                .filter(hasElement(By.tagName("span")))
                .filter(button -> button.findElement(By.tagName("span")).getText().equals("ZURÜCK"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector());
    }
}
