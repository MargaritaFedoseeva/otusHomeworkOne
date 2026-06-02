package auto.steps;

import com.google.inject.Inject;
import di.Scoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Course;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.CoursesPage;
import pages.LessonPage;
import pages.OnlineLessonPage;
import pages.PageManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CoursesPageSteps {
    @Inject
    private BaseSteps browserSteps;
    @Inject
    private CoursesPage coursesPage;

    @Inject
    private LessonPage lessonPage;

    @Inject
    private OnlineLessonPage onlineLessonPage;

    private Scoped scoped;

    @Inject
    public CoursesPageSteps(Scoped scoped) {
        this.scoped = scoped;
    }

    @When("открыта страница курсов")
    public void openCoursesPage() {
        coursesPage.open("");
        scoped.pageManager = new PageManager();
        scoped.pageManager.setCurrentPage(coursesPage);
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

        Map<LocalDate, List<Element>> coursesFiltered = coursesPage.onOfAfterDateStartCourses(coursesDate, dateValue);

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

    @Then("сохранить информацию по всем курсам")
    public void saveCoursesInfoOpenCardCourse() {
        Document docCoursesPage = coursesPage.getPage();

        scoped.courses = new ArrayList<>();

        List<Element> courses = docCoursesPage.select(".bwGwUO h6 div.jEGzDf");
        for (Element course : courses) {
            String hrefOnlineLesson = course.parent().parent().attr("href").replace("/online/", "");
            Document doc = onlineLessonPage.getPage(hrefOnlineLesson.replace("/", ""));
            scoped.courses.add(Course.builder()
                    .title(course.text())
                    .price(doc.selectXpath("//div[contains(., '₽') and (contains(@class, 'gztHyx') or contains(@class, 'tn-atom'))]").first().text())
                    .build());
        }
    }

    @Then("найти самый дорогой курс")
    public void maxPriceCourse() {
        double maxPrice = scoped.courses.stream()
                .mapToDouble(c -> parsePrice(c.getPrice()))
                .max()
                .orElse(0.0);

        List<Course> expensiveCourseCard = scoped.courses.stream().filter(c -> parsePrice(c.getPrice()) == maxPrice).toList();

        System.out.println("\nИнформация по самым дорогим курсам:");
        for (Course course : expensiveCourseCard) {
            System.out.println(String.format("Название \"%s\", цена %s руб.",
                    course.getTitle(), course.getPrice()));
        }
    }

    @Then("найти самый дешевый курс")
    public void minPriceCourse() {
        double minPrice = scoped.courses.stream()
                .mapToDouble(c -> parsePrice(c.getPrice()))
                .min()
                .orElse(0.0);

        List<Course> cheapestCourseCard = scoped.courses.stream().filter(c -> parsePrice(c.getPrice()) == minPrice).toList();

        System.out.println("\nИнформация по самым дешевым курсам:");
        for (Course course : cheapestCourseCard) {
            System.out.println(String.format("Название \"%s\", цена %s руб.",
                    course.getTitle(), course.getPrice()));
        }
    }

    @Then("открыт каталог курсов верной категории")
    public void courseCategorySelected() {
        coursesPage.checkCtgCourses(scoped.ctgCourse);
    }

    private double parsePrice(String priceText) {
        String cleanPrice = priceText.replaceAll("[^0-9.,]", "").replace(",", ".");
        return cleanPrice.isEmpty() ? 0.0 : Double.parseDouble(cleanPrice);
    }

}
