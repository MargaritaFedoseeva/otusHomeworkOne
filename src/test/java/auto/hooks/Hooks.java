package auto.hooks;

import com.google.inject.Inject;
import di.Scoped;
import factory.DriverFactory;
import io.cucumber.java.After;

public class Hooks {
    private Scoped scoped;
    private final DriverFactory driverFactory;

    @Inject
    public Hooks(DriverFactory driverFactory, Scoped scoped) {
        this.driverFactory = driverFactory;
        this.scoped = scoped;
    }

    @After
    public void tearDown() {
        if (scoped.driverFactory != null) {
            driverFactory.quit(scoped.driver);
        }
    }
}
