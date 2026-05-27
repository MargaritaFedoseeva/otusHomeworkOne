package factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.google.inject.name.Named;
import factory.settings.IDriverSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import listeners.StyleUpdateListener;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.openqa.selenium.support.events.EventFiringDecorator;

@Singleton
public class DriverFactory {

    private WebDriverManager webDriverManager;
    private IDriverSettings<?> browserSettings;
    private StyleUpdateListener styleUpdateListener;

    @Inject
    public DriverFactory(IDriverSettings<?> settings, WebDriverManager manager, StyleUpdateListener listener) {
        this.browserSettings = settings;
        this.webDriverManager = manager;
        this.styleUpdateListener = listener;
    }

    public WebDriver create() {
        WebDriver driver = webDriverManager.capabilities(browserSettings.settings()).create();
        return new EventFiringDecorator<>(styleUpdateListener).decorate(driver);
    }

    public void quit(WebDriver driver) {
        WebDriver original = ((Decorated<WebDriver>) driver).getOriginal();
        webDriverManager.quit(original);
    }
}
