package auto.steps;

import com.google.inject.Inject;
import di.Scoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Course;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import pages.CoursesPage;
import pages.LessonPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CoursesPageSteps {

    @Inject
    private CoursesPage coursesPage;

    @Inject
    private LessonPage lessonPage;

    private Scoped scoped;

    @Inject
    public CoursesPageSteps(Scoped scoped) {
        this.scoped = scoped;
    }

    @When("открыта страница курсов")
    public void openCoursesPage() {
        coursesPage.open("");
    }

    @When("открыть первый попавшийся курс с названием {string}")
    public void clickFirstOneAvailableCourseWithTitle(String string) {
        coursesPage.openFirstOneAvailableCourseTitle(string);
    }

    @When("открыт случайный курс с названием {string}")
    public void clickCourseWithTitle(String string) {
        coursesPage.openRandomCourseTitle(string);
    }

    @When("открыть все курсы")
    public void clickCourseWithTitle() {
        coursesPage.showAllCourseTitle();
    }

    @Then("найти курсы, которые стартуют раньше всех")
    public void filterCoursesMinDate() {
        Document doc = coursesPage.getPage("");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'г.'");

        List<Element> coursesDate = doc.select("div.bwGwUO>a .ieVVRJ div");

        Map<LocalDate, List<Element>> coursesMinDate = coursesPage.isMax(coursesDate, false);
        scoped.courses = new ArrayList<>();

        System.out.println("Курсы, которые стартуют раньше всех:");
        coursesMinDate.forEach((date, elements) -> {
            elements.forEach(element -> {
                String courseName = element.parent().parent().parent().selectFirst("h6").text();
                System.out.println(String.format("название \"%s\", дата старта %s",
                        courseName, date.format(fullFormatter)));
                scoped.courses.add(Course.builder()
                        .title(courseName)
                        .date(date)
                        .build());

            });
        });
    }

    @Then("найти курсы, которые стартуют позже всех")
    public void filterCoursesMaxDate() {
        Document doc = coursesPage.getPage("");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'г.'");

        List<Element> coursesDate = doc.select("div.bwGwUO>a .ieVVRJ div");

        Map<LocalDate, List<Element>> coursesMaxDate = coursesPage.isMax(coursesDate, true);
        scoped.courses = new ArrayList<>();

        System.out.println("Курсы, которые стартуют позже всех:");
        coursesMaxDate.forEach((date, elements) -> {
            elements.forEach(element -> {
                String courseName = element.parent().parent().parent().selectFirst("h6").text();
                System.out.println(String.format("название \"%s\", дата старта %s",
                        courseName, date.format(fullFormatter)));
                scoped.courses.add(Course.builder()
                        .title(courseName)
                        .date(date)
                        .build());

            });
        });
    }

    @Then("проверить информацию по отфильтрованным курсам")
    public void checkCourse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
        for (Course course : scoped.courses) {
            coursesPage.openFirstOneAvailableCourseTitle(course.getTitle()); //открываем курс
            assertEquals(course.getTitle(), lessonPage.getLessonTitle()); //проверяем название
            assertEquals(course.getDate().format(formatter), lessonPage.getLessonStartDate()); //проверяем дату старта курса
            coursesPage.open("");
        }
    }

    @Then("найти курсы, стартующие в указанную дату {string} или позже")
    public void filterCoursesOnOfAfterDateStart(String value) {
        Document doc = coursesPage.getPage("");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
        LocalDate dateValue = LocalDate.parse(value, fullFormatter);

        List<Element> coursesDate = doc.select("div.bwGwUO>a .ieVVRJ div");

        Map<LocalDate, List<Element>> coursesFiltered = coursesPage.onOfAfterDateStartCurses(coursesDate, dateValue);

        System.out.println(String.format("Курсы, стартующие в указанную дату \"%s\" г. или позже указанной даты:",
                value));
        if (!coursesFiltered.isEmpty()) {
            coursesFiltered.forEach((date, elements) -> {
                elements.forEach(element -> {
                    String courseName = element.parent().parent().parent().selectFirst("h6").text();
                    System.out.println(String.format("название \"%s\", дата старта %s",
                            courseName, date.format(fullFormatter)));

                });
            });
        } else {
            System.out.println("курсов не найдено");
        }
    }

}
