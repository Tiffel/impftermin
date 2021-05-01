package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import static tech.cscheer.impfen.selenium.SeleniumUtils.uniqueWebElementInListCollector;

public abstract class AbstractLoggedinPage {
    public static void logout(WebDriver driver, Wait<WebDriver> wait) {
        try {
            driver.findElements(By.tagName("a")).stream().filter(a -> a.getText().contains("Logout")).collect(uniqueWebElementInListCollector()).click();
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //doof, wenn der logout failed. aber irgendwie auch egal.
        }

    }

}
