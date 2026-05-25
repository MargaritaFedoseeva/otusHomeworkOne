package extensions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import factory.DriverFactory;
import factory.DriverFactoryModule;
import factory.DriverModule;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.openqa.selenium.WebDriver;

public class UIExtension implements TestInstancePostProcessor, AfterEachCallback {
    //    private WebDriver driver;
    private static final Injector PARENT_INJECTOR = Guice.createInjector(new DriverFactoryModule());
    private static final ThreadLocal<Injector> INJECTOR = new ThreadLocal<>();

    @Override
    public void postProcessTestInstance(Object instance, ExtensionContext context) {
        Injector currentInjector = PARENT_INJECTOR.createChildInjector(new DriverModule());
        INJECTOR.set(currentInjector);
        currentInjector.injectMembers(instance);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Injector currentInjector = INJECTOR.get();
        if (currentInjector != null) {
            WebDriver driver = currentInjector.getInstance(WebDriver.class);
            DriverFactory driverFactory = currentInjector.getInstance(DriverFactory.class);
            driverFactory.quit(driver);
        }
    }
}
