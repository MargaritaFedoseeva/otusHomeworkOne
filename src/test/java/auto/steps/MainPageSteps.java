package auto.steps;

import com.google.inject.Inject;
import di.Scoped;
import io.cucumber.java.en.When;
import pages.*;


public class MainPageSteps {

    @Inject
    private MainPage mainPage;

    private Scoped scoped;

    @Inject
    public MainPageSteps(Scoped scoped) {
        this.scoped = scoped;
    }

    @When("открыта главная страница")
    public void openCoursesPage() {
        mainPage.open("");
        scoped.pageManager = new PageManager();
        scoped.pageManager.setCurrentPage(mainPage);
    }

    @When("навести курсор мыши на заголовк меню с текстом {string}")
    public void openItemMenuWithValue(String value) {
        mainPage.moveToMenuItemWithText(value);
    }

    @When("открыть случайную категорию курсов")
    public void openRandomCtgCourses() {
        scoped.ctgCourse = mainPage.randomClickCtgCourseGetText();
        System.out.println(String.format("Открыта категорию курсов \"%s\"", scoped.ctgCourse));
    }

}
