package module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import factory.DriverFactory;
import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;

public class DriverModule extends AbstractModule {

    @Provides
    @ScenarioScoped
    private WebDriver getDriver(DriverFactory factory, String browserName) {
        return factory.create();
    }

}
