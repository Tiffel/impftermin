package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import tech.cscheer.impfen.selenium.Log;
import tech.cscheer.impfen.selenium.SeleniumUtils;

public abstract class AbstractLoggedinPage {
    public static void logout(WebDriver driver, Wait<WebDriver> wait) {
        wait.until(AbstractLoggedinPage::logoutPresent);
        Log.info("logout");
        try {
            WebElement logoutButton = driver.findElements(By.tagName("a")).stream().filter(a -> a.getText().contains("Logout")).collect(SeleniumUtils.uniqueWebElementInListCollector());
            logoutButton.click();

            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //doof, wenn noch was schief geht. aber irgendwie auch egal.
            Log.info("logout fehlgeschlagen, warum auch immer");
        }
        driver.quit();
    }

    private static Boolean logoutPresent(WebDriver d) {
        return d.findElements(By.tagName("a")).stream().anyMatch(a -> a.getText().contains("Logout"));
    }

}
