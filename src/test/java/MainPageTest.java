import extensions.UIExtension;
import jakarta.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import pages.CoursesPage;
import pages.LessonPage;
import pages.MainPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(UIExtension.class)
public class MainPageTest {

    @Inject
    private MainPage mainPage;

    @Inject
    private CoursesPage coursesPage;

    @Test
    public void openCourseCheckTitleRandom() {
        mainPage.open();
        Document doc = mainPage.getPage("");
        mainPage.moveToMenuHeader("Обучение");
        String courseOpenTitleRandom = mainPage.randomClickCtgCourseGetText();
        coursesPage.checkCtgCourses(courseOpenTitleRandom);

    }

}
