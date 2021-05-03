package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasAttributeContains;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;
import static tech.cscheer.impfen.selenium.SeleniumUtils.uniqueWebElementInListCollector;

public class TerminfindungPage extends AbstractLoggedinPage {
    private static Logger log = LoggerFactory.getLogger(TerminfindungPage.class);
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void handle(WebDriver driver, Wait<WebDriver> wait, Impfzentrum impfzentrum, ZonedDateTime datum) {
        wait.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Terminfindung"));
        String datumString = datum.format(dateTimeFormatter);
        setDatum(driver, datumString);

        WebElement selectionDiv = getSelectionDiv(driver);
        Select impfzentren = new Select(selectionDiv.findElement(By.tagName("select")));
        impfzentren.selectByValue(impfzentrum.getValue());
        log.info("Suche Termine in " + impfzentrum + " ab dem " + datumString);

        getWeiterButton(driver).click();

    }

    private static void setDatum(WebDriver driver, String datum) {
        WebElement datumInput = driver.findElements(By.tagName("input")).stream()
                .filter(hasAttributeContains("class", "gwt-DateBox").and(hasAttributeContains("title", "Datumseingabe")))
                .collect(uniqueWebElementInListCollector());
        datumInput.sendKeys(Keys.CONTROL + "a");
        datumInput.sendKeys(Keys.DELETE);
        datumInput.sendKeys(datum);
    }

    private static WebElement getSelectionDiv(WebDriver driver) {
        return driver.findElements(By.tagName("div")).stream()
                .filter(hasAttributeContains("class", "input-field"))
                .filter(hasElement(By.tagName("label")).and(hasElement(By.tagName("select"))))
                .filter(div -> div.findElements(By.tagName("label")).stream()
                        .map(WebElement::getText)
                        .anyMatch(text -> text.equals("Ihr gewünschtes Impfcenter*"))
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
