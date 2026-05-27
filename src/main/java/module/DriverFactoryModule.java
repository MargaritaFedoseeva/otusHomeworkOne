package module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import di.Scoped;
import factory.DriverFactory;
import factory.settings.ChromeSettings;
import factory.settings.FirefoxSettings;
import factory.settings.IDriverSettings;
import io.cucumber.guice.ScenarioScoped;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactoryModule extends AbstractModule {
    private final Browser browser = Browser.valueOf(System.getProperty("browser").toUpperCase());

    @Override
    protected void configure() {
        bind(Scoped.class).in(ScenarioScoped.class);
        bind(DriverFactory.class).in(ScenarioScoped.class);
    }

    @Provides
    @ScenarioScoped
    public WebDriverManager webDriverManager() {
        return switch (browser) {
            case CHROME -> WebDriverManager.chromedriver();
            case FIREFOX -> WebDriverManager.firefoxdriver();
        };
    }

    @Provides
    @ScenarioScoped
    public IDriverSettings<?> browserSettings() {
        return switch (browser) {
            case CHROME -> new ChromeSettings();
            case FIREFOX -> new FirefoxSettings();
        };
    }

    private enum Browser {
        CHROME,
        FIREFOX
    }
}
