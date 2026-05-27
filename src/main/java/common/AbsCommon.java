package common;

import di.Scoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import waiters.CommonWaiter;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public abstract class AbsCommon {

    protected WebDriver driver;
    protected Actions action;
    protected CommonWaiter waiter;

    public AbsCommon(Scoped scoped) {
        this.driver = scoped.driver;
        this.action = new Actions(scoped.driver);
        waiter = new CommonWaiter(scoped.driver);

        PageFactory.initElements(scoped.driver, this);
    }

    public List<WebElement> byWebElements(By selector) {
        return driver.findElements(selector);
    }

    public WebElement by(By selector) {
        return driver.findElement(selector);
    }

    protected void clickElementByPredicate(List<WebElement> elements, Predicate<WebElement> filter) {
        for (WebElement element : elements) {
            if (filter.test(element)) {
                waiter.waitForCondition(ExpectedConditions.stalenessOf(element));
                element.click();
            }
        }
        throw new AssertionError("Элемент по фильтру не найден на странице");
    }

    protected void moveElement(WebElement element) {
        action.moveToElement(element).perform();
    }

    protected void moveElementByPredicate(List<WebElement> elements, Predicate<WebElement> filter) {
        for (WebElement element : elements) {
            if (filter.test(element)) {
                waiter.waitForCondition(ExpectedConditions.stalenessOf(element));
                action.moveToElement(element).perform();
                break;
            }
        }
    }

    protected String randomClickWebElementGetText(List<WebElement> element) {
        Random random = new Random();
        int randomIndex = random.nextInt(element.size());
        WebElement randomElement = element.get(randomIndex);
        String textRandomElement = randomElement.getText();
        randomElement.click();
        return textRandomElement;
    }

    protected <K extends Comparable<? super K>, V> BinaryOperator<Map.Entry<K, V>> getSelector(boolean isMax) {
        return (e1, e2) -> {
            int cmp = e1.getKey().compareTo(e2.getKey());
            return (isMax ? cmp > 0 : cmp < 0) ? e1 : e2;
        };
    }
}
