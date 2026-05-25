package factory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import pages.CoursesPage;

public class DriverModule extends AbstractModule {

    @Provides
    @Singleton
    private WebDriver getDriver(DriverFactory factory) {
        return factory.create();
    }

}
