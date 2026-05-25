package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Path("/")
@UrlTemplates(
        @Template("lesson/$1"))
public class LessonPage extends AbsBasePage<LessonPage> {

    @Inject
    public LessonPage(WebDriver driver) {
        super(driver);

    }

    @FindBy(css = "h1.diGrSa")
    private WebElement txtLessonTitle;

    @FindBy(css = "p.dgWykw")
    private List<WebElement> txtLessonInfo;

    public String getLessonTitle(){
        return txtLessonTitle.getText();
    }

    public String getLessonStartDate(){
        return txtLessonInfo.get(0).getText();
    }

}
