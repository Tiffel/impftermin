package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.SeleniumUtils;

public class ZugangPage {

    public static void handle(WebDriver driver, Wait<WebDriver> wait, String username, String password) {
        System.out.println("Waiting for Zugangsdatenelement");
        wait.until(ZugangPage::zugangsdatenPresent);
        System.out.println("Logging in");
        getInputByLabel(driver, "Vorgangskennung*").sendKeys(username);
        getInputByLabel(driver, "Passwort*").sendKeys(password);
        getWeiterButton(driver).click();
        wait.until(d -> !zugangsdatenPresent(d));
        System.out.println("Leaving Zugangpage");
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
                .stream().filter(button -> button.findElement(By.tagName("span")).getText().equals("WEITER"))
                .collect(SeleniumUtils.uniqueWebElementInListCollector());
    }

    private static Boolean zugangsdatenPresent(WebDriver d) {
        return d.findElements(By.tagName("h3")).stream().anyMatch(webElement -> webElement.getAttribute("title").equals("Zugangsdaten"));
    }
}
