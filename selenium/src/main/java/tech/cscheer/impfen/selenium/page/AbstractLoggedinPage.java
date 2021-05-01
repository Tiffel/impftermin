package tech.cscheer.impfen.selenium.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractLoggedinPage {
    public static void logout(WebDriver driver, Wait<WebDriver> wait) {
        try {
            List<WebElement> logoutButton = driver.findElements(By.tagName("a")).stream().filter(a -> a.getText().contains("Logout")).collect(Collectors.toList());
            if (logoutButton.isEmpty()) {
                System.out.println("logout fehlgeschlagen, button nicht gefunden. Das passiert in 50% der Fälle einfach");
            } else {
                logoutButton.get(0).click();
                wait.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                alert.accept();
            }

        } catch (Exception e) {
            //doof, wenn noch was schief geht. aber irgendwie auch egal.
            System.out.println("logout fehlgeschlagen, warum auch immer");
        }

    }

}
