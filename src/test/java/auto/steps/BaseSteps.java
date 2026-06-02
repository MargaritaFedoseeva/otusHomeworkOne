package auto.steps;

import com.google.inject.Inject;
import di.Scoped;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import pages.CoursesPage;

import java.util.List;

public class BaseSteps {
    @Inject
    private CoursesPage coursesPage;

    private Scoped scoped;

    @Inject
    public BaseSteps(Scoped scoped) {
        this.scoped = scoped;
    }

    @ParameterType("\"([^\"]*)\"")
    public List<WebElement> elements(String elementName) {
        return scoped.pageManager.getCollection(elementName);
    }

    @When("выполнено нажатие на элемент списка {elements} с текстом {string}")
    public void clickWebElementsWithText(List<WebElement> nameWebElement, String value) {
        WebElement element = nameWebElement.stream().filter(e -> value.equals(e.getText())).findFirst().orElse(null);
        element.click();
    }
}
