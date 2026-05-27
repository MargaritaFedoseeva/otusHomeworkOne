package auto.steps;

import com.google.inject.Inject;
import di.Scoped;
import factory.DriverFactory;
import io.cucumber.java.ru.Допустим;

public class BrowserSteps {
    private Scoped scoped;
    private final DriverFactory driverFactory;

    @Inject
    public BrowserSteps (DriverFactory driverFactory, Scoped scoped) {
        this.driverFactory = driverFactory;
        this.scoped = scoped;
    }

    @Допустим("я открываю браузер Chrome")
    public void openBrowserChrome() {
        scoped.driver = driverFactory.create();
        scoped.driverFactory = driverFactory;
    }

}
