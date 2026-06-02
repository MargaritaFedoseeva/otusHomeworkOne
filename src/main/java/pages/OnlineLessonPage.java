package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import di.Scoped;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Path("/")
@UrlTemplates(
        @Template("online/$1"))
public class OnlineLessonPage extends AbsBasePage<OnlineLessonPage> {

    @Inject
    public OnlineLessonPage(Scoped scoped) {
        super(scoped);

    }

    @FindBy(css = ".gztHyx")
    private WebElement txtPrice;

    @FindBy(css = ".jhjUZl")
    private WebElement txtTitle;

    public String getTitle(){
        return txtTitle.getText();
    }

    public String getPrice(){
        return txtPrice.getText();
    }

}
