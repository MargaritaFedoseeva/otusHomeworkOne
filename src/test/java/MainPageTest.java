import extensions.UIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CoursesPage;
import pages.MainPage;

@ExtendWith(UIExtension.class)
public class MainPageTest {

    @Inject
    private MainPage mainPage;

    @Inject
    private CoursesPage coursesPage;

    @Test
    public void openCourseCheckTitleRandom() {
        mainPage.open();
        mainPage.moveToMenuItemWithText("Обучение");
        String courseOpenTitleRandom = mainPage.randomClickCtgCourseGetText();
        coursesPage.checkCtgCourses(courseOpenTitleRandom);

    }

}
