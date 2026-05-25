package waiters;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonWaiter {
    private final WebDriverWait wait;

    public CommonWaiter(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public <T> T waitForCondition(ExpectedCondition<T> condition) {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            return null;
        }
    }

}
