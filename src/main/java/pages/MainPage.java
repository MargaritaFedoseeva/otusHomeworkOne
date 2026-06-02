package pages;

import annotations.Name;
import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import di.Scoped;
import org.junit.jupiter.api.Assertions;
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
    @Name("Раздел меню")
    private List<WebElement> listMenuHeader;

    @FindBy(css = "div.iTRnIG div.cgYLnJ a")
    private List<WebElement> listCtgCourses;

    public void moveToMenuItemWithText(String text) {
       WebElement element = listMenuHeader.stream().filter(e -> e.getText().equals(text)).findFirst().orElse(null);
       moveElement(element);
    }

    public String randomClickCtgCourseGetText() {
        return randomClickWebElementGetText(listCtgCourses);
    }


}
