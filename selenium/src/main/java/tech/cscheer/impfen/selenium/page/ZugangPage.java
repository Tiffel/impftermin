package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.Log;
import tech.cscheer.impfen.selenium.SeleniumUtils;

import static tech.cscheer.impfen.selenium.SeleniumUtils.hasElement;

public class ZugangPage {

    public static void handle(WebDriver driver, Wait<WebDriver> wait, String username, String password) {
        wait.until(ZugangPage::zugangsdatenPresent);
        Log.info("Einloggen");
        getInputByLabel(driver, "Vorgangskennung*").sendKeys(username);
        getInputByLabel(driver, "Passwort*").sendKeys(password);
        getWeiterButton(driver).click();
        wait.until(d -> !zugangsdatenPresent(d));
        Log.info("Eingeloggt");
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
