package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import di.Scoped;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Path("")
public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    public MainPage(Scoped scoped) {
        super(scoped);

    }

    @FindBy(css = "span.cMNIlZ")
    private List<WebElement> listMenuHeader;

    @FindBy(css = "div.cgYLnJ a.dZepSJ")
    private List<WebElement> listCtgCourses;


    public void moveToMenuHeader(String title) {
        moveElementByPredicate(listMenuHeader, e -> title.equals(e.getText()));
    }

    public String randomClickCtgCourseGetText() {
        return randomClickWebElementGetText(listCtgCourses);
    }


}
