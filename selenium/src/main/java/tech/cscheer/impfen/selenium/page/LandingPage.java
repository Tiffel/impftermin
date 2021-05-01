package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static tech.cscheer.impfen.selenium.SeleniumUtils.uniqueWebElementInListCollector;

public class LandingPage {

    public static void handle(WebDriver driver, Wait<WebDriver> wait) {
        driver.get("https://sachsen.impfterminvergabe.de/");
        String originalWindow = driver.getWindowHandle();
        WebElement termin = getTerminButton(driver);
        termin.click();

        wait.until(numberOfWindowsToBe(2));
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        //eigentliche Anwendung, handelt auch die wait page!
        System.out.println("Warte auf Zugangsseite. Ggf Länger wegen Warteseite");
        wait.until(d -> d.getTitle().contains("Serviceportal zur Impfung gegen das Corona Virus in Sachsen - Zugang"));
    }

    private static WebElement getTerminButton(WebDriver driver) {
        return driver.findElements(By.tagName("a")).stream().filter(a -> a.getAttribute("class").contains("btn"))
                .filter(buttons -> {
                    WebElement span = buttons.findElement(By.tagName("span"));
                    return span.getText().equals("TERMIN");
                }).collect(uniqueWebElementInListCollector());
    }
}
