package tech.cscheer.impfen.selenium.page;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static tech.cscheer.impfen.selenium.Environment.DAY_TIME_PREF;
import static tech.cscheer.impfen.selenium.Environment.WEEKDAY_PREF;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasAttributeContains;
import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;
import static tech.cscheer.impfen.selenium.SeleniumUtils.uniqueWebElementInListCollector;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.cscheer.impfen.selenium.SeleniumUtils;

public class TerminfindungPage extends AbstractLoggedinPage {
    private static Logger log = LoggerFactory.getLogger(TerminfindungPage.class);
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void handle(WebDriver driver, Wait<WebDriver> wait, Impfzentrum impfzentrum) {
        wait.until(titleIs("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Terminfindung"));
        log.info("Suche Termine in " + impfzentrum);
        setSelection(driver, "Ihr gewünschtes Impfcenter*", impfzentrum.getValue());
        if (!Wochentag.UNDEFINED.equals(WEEKDAY_PREF)) {
            setSelection(driver, "Wochentag oder Wochenende*", WEEKDAY_PREF.getValue());
        }
        if(!Tageszeitraum.UNDEFINED.equals(DAY_TIME_PREF)) {
            setSelection(driver, "Tageszeitraum*", DAY_TIME_PREF.getValue());
        }

        // set date at the end to prevent errors between 59 and 01.
        // mb this isn't necessary, the form sets the date
        // other solution: check for the date error on timeout, get back and forth, date gets resetted by the page itself
        String date = ZonedDateTime.now(ZoneId.of("Europe/Berlin")).plusDays(1).format(dateTimeFormatter);
        setDatum(driver, date);

        getWeiterButton(driver).click();
    }

    private static void setSelection(WebDriver driver, String selectionTitle, String partialCssIdTag) {
        clickSelectionArrow(driver, selectionTitle);
        WebElement suggestion = driver.findElements(By.cssSelector(String.format("[id$=%s]", partialCssIdTag))).stream()
                .collect(uniqueWebElementInListCollector());
        suggestion.click();
    }

    private static void clickSelectionArrow(WebDriver driver, String selectionTitle) {
        WebElement arrowToClick = getSelectionDiv(driver, selectionTitle)
                .findElements(By.tagName("span")).stream()
                .filter(hasAttributeContains("class", "select2-selection__arrow")
                        .and(hasAttributeContains("role", "presentation")))
                .collect(uniqueWebElementInListCollector());
        arrowToClick.click();
    }

    private static WebElement getSelectionDiv(WebDriver driver, String divTile) {
        return driver.findElements(By.tagName("div")).stream()
                .filter(hasAttributeContains("class", "input-field"))
                .filter(hasElement(By.tagName("label")).and(hasElement(By.tagName("select"))))
                .filter(div -> div.findElements(By.tagName("label")).stream()
                        .map(WebElement::getText)
                        .anyMatch(text -> text.equals(divTile))
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

    private static void setDatum(WebDriver driver, String datum) {
        WebElement datumInput = driver.findElements(By.tagName("input")).stream()
                .filter(hasAttributeContains("class", "gwt-DateBox")
                        .and(hasAttributeContains("title", "Datumseingabe")))
                .collect(uniqueWebElementInListCollector());
        datumInput.sendKeys(Keys.CONTROL + "a");
        datumInput.sendKeys(Keys.DELETE);
        datumInput.sendKeys(datum);
    }

    //the hard way, replaced by css
    private static void setImpfzentrumWithSuggetion(WebDriver driver, Impfzentrum impfzentrum) {
        clickSelectionArrow(driver, "Ihr gewünschtes Impfcenter*");
        WebElement inputbox = driver.findElements(By.tagName("input")).stream()
                .filter(hasAttributeContains("type", "search")
                        .and(hasAttributeContains("role", "searchbox")))
                .collect(uniqueWebElementInListCollector());
        inputbox.sendKeys(impfzentrum.name());
        WebElement suggestion = driver.findElements(By.tagName("ul")).stream()
                .filter(hasAttributeContains("role", "listbox"))
                .filter(hasElement(By.tagName("li")))
                .map(webElement -> webElement.findElement(By.tagName("li")))
                .collect(uniqueWebElementInListCollector());
        suggestion.click();
    }
}
