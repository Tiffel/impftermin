package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.Log;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;

public class TerminvergabePage extends AbstractLoggedinPage {

    public static void handle(WebDriver driver, Wait<WebDriver> wait) {
        wait.until(d -> d.getTitle().contains("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Terminvergabe"));
        if (isKeineTermineFrei(driver)) {
            //schade
            Log.info("Keine Termine Frei :(");
            getZurueckButton(driver).click();
        } else {
            //Glückwunsch? Keine Ahnung wie die Seite nun aussieht.
            Log.info("wohoooo. Impftermin mgwl. verfügbar.");
            //einfach mal ne exception werfen, damit der browser offen bleibt :)
            throw new RuntimeException("Impftermin mgwl. verfügbar.");
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
