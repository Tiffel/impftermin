package tech.cscheer.impfen.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SeleniumUtils {
    public static Predicate<WebElement> hasElement(By by) {
        return div -> {
            try {
                div.findElement(by);
            } catch (NoSuchElementException e) {
                return false;
            }
            return true;
        };
    }

    public static Collector<WebElement, Object, WebElement> uniqueWebElementInListCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException("Liste hat " + list.size() + " Elemente, statt erwartet 1");
                    }
                    return list.get(0);
                }
        );
    }
}
