package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasAttributeContains;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;
import static tech.cscheer.impfen.selenium.SeleniumUtils.uniqueWebElementInListCollector;

public class TerminfindungPage extends AbstractLoggedinPage {
    static Logger log = LoggerFactory.getLogger(LandingPage.class);

    public static void handle(WebDriver driver, Wait<WebDriver> wait, Impfzentrum impfzentrum) {
        wait.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Terminfindung"));
        WebElement selectionDiv = getSelectionDiv(driver);
        Select impfzentren = new Select(selectionDiv.findElement(By.tagName("select")));
        impfzentren.selectByValue(impfzentrum.getValue());
        log.info("Suche Termine in " + impfzentrum);
        getWeiterButton(driver).click();

    }

    private static WebElement getSelectionDiv(WebDriver driver) {
        return driver.findElements(By.tagName("div")).stream()
                .filter(hasAttributeContains("class", "input-field"))
                .filter(hasElement(By.tagName("label")).and(hasElement(By.tagName("select"))))
                .filter(div -> div.findElements(By.tagName("label"))
                        .stream().map(WebElement::getText).anyMatch(text -> text.equals("Ihr gewünschtes Impfcenter*"))
                )
                .collect(uniqueWebElementInListCollector());
    }

    private static WebElement getWeiterButton(WebDriver driver) {
        return driver.findElements(By.tagName("button"))
                .stream()
                .filter(hasElement(By.tagName("span")))
                .filter(button -> button.findElement(By.tagName("span")).getText().equals("WEITER"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector());
    }

}
