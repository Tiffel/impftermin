package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasAttribute;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;

public class AktionsauswahlPage extends AbstractLoggedinPage {
    private static Logger log = LoggerFactory.getLogger(AktionsauswahlPage.class);

    public static void handle(WebDriver driver, Wait<WebDriver> wait) {
        wait.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Aktionsauswahl"));
        log.info("Wähle Aktion Terminfindung");
        getRadiobuttonByLabel(driver, "Termin zur Coronaschutzimpfung vereinbaren oder ändern").click();
        getWeiterButton(driver).click();
        log.info("Lade Terminfindungspage");
    }

    private static WebElement getRadiobuttonByLabel(WebDriver driver, String label) {
        return driver.findElements(By.tagName("div")).stream()
                .filter(hasAttribute("role", "radiogroup").and(SeleniumUtils.hasElement(By.tagName("span"))))
                .flatMap(webElement -> webElement.findElements(By.tagName("span")).stream())
                .filter(SeleniumUtils.hasElement(By.tagName("label")).and(SeleniumUtils.hasElement(By.tagName("input"))))
                .filter(div -> div.findElement(By.tagName("label")).getText().contains(label))
                .map(div -> div.findElement(By.tagName("label")))
                .distinct().collect(SeleniumUtils.uniqueWebElementInListCollector());
    }

    private static WebElement getWeiterButton(WebDriver driver) {
        return driver.findElements(By.tagName("button"))
                .stream()
                .filter(hasElement(By.tagName("span")))
                .filter(button -> button.findElement(By.tagName("span")).getText().equals("WEITER"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector());
    }
}
