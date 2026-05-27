package di;

import com.google.inject.Inject;
import factory.DriverFactory;
import io.cucumber.guice.ScenarioScoped;
import models.Course;
import org.openqa.selenium.WebDriver;

import java.util.List;

@ScenarioScoped
public class Scoped {
    public WebDriver driver;
    public DriverFactory driverFactory;
    public List<Course> courses;
}
