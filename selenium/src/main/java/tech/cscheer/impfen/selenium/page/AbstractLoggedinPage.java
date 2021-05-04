package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static tech.cscheer.impfen.selenium.SeleniumUtils.hasAttributeContains;

public abstract class AbstractLoggedinPage {
    private static final Logger log = LoggerFactory.getLogger(AbstractLoggedinPage.class);

    public static void logout(WebDriver driver, Wait<WebDriver> wait) {
        wait.until(AbstractLoggedinPage::logoutPresent);
        log.info("logout");
        try {
            WebElement logoutButton = driver.findElements(By.tagName("a")).stream().filter(a -> a.getText().contains("Logout")).collect(SeleniumUtils.uniqueWebElementInListCollector());
            logoutButton.click();

            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //doof, wenn noch was schief geht. aber irgendwie auch egal.
            log.info("logout fehlgeschlagen, warum auch immer");
        }
        driver.quit();
    }

    private static Boolean logoutPresent(WebDriver d) {
        return d.findElements(By.tagName("a")).stream().anyMatch(a -> a.getText().contains("Logout"));
    }

    public static void handleErrorModal(WebDriver driver) {
        driver.findElements(By.tagName("div")).stream()
                .filter(hasAttributeContains("role", "dialog"))
                .filter(div ->
                        div.findElements(By.tagName("div"))
                                .stream().map(WebElement::getText)
                                .anyMatch(subdivText -> subdivText.contains("Bei der Verarbeitung der Anfrage ist ein Fehler aufgetreten."))
                )
                .map(div -> div.findElement(By.tagName("button")))
                .findAny()
                .filter(WebElement::isDisplayed).filter(WebElement::isEnabled)
                .ifPresent(WebElement::click);
    }

}
