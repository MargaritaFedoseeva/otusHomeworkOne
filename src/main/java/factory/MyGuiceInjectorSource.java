package factory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.InjectorSource;
import module.DriverFactoryModule;

public class MyGuiceInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        return Guice.createInjector(
                CucumberModules.createScenarioModule(),
                new DriverFactoryModule()
        );
    }
}
