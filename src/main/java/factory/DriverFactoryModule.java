package factory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import factory.settings.ChromeSettings;
import factory.settings.IDriverSettings;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactoryModule extends AbstractModule {
    private final Browser browser = Browser.valueOf(System.getProperty("browser").toUpperCase());

    @Provides
    @Singleton
    private WebDriverManager webDriverManager() {
        return switch (browser) {
            case CHROME -> WebDriverManager.chromedriver();
        };
    }

    @Provides
    @Singleton
    private IDriverSettings<?> browserSettings() {
        return switch (browser) {
            case CHROME -> new ChromeSettings();
        };
    }

    private enum Browser {
        CHROME
    }


}
