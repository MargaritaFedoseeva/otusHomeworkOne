package auto.steps;

import com.google.inject.Inject;
import io.cucumber.java.en.Then;
import pages.CoursesPage;
import pages.LessonPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LessonPageSteps {
    @Inject
    private LessonPage lessonPage;

    @Then("наименование курса равно {string}")
    public void checkTitleLesson(String string) {
        assertEquals(lessonPage.getLessonTitle(), string);
    }
}
