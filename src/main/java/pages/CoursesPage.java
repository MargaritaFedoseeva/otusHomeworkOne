package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import di.Scoped;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Path("/")
@UrlTemplates(
        @Template("catalog/courses"))
public class CoursesPage extends AbsBasePage<CoursesPage> {

    @Inject
    public CoursesPage(Scoped scoped) {
        super(scoped);
    }

    @FindBy(css = "h6 div.jEGzDf")
    private List<WebElement> listCoursesTitle;


    @FindBy(css = "button.cXVWAS")
    private WebElement btnShowMore;


    @FindBy(css = ".ieVVRJ div.jEGzDf")
    private List<WebElement> listCoursesDateInCatalog;

    @FindBy(xpath = "//p[text()='Направление']/../following-sibling::*//input/../..")
    private List<WebElement> linkFilters;


    //реализация 1: поиск курсов, среди загруженных
    public List<WebElement> findCoursesByName(String courseName) {
        return listCoursesTitle.stream()
                .filter(course -> course.getText().contains(courseName))
                .collect(Collectors.toList());
    }

    //реализация 2: поиск курса, среди загруженных
    public WebElement findCourseByTitle(String courseName) {
        return listCoursesTitle.stream()
                .filter(course -> course.getText().contains(courseName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    // реализация 3: если курса нет на экране, то выполняем нажатие на кнопку "Показать еще", открываем первый попавшийся
    public void openFirstOneAvailableCourseTitle(String courseName) {
        List<WebElement> courses = findCoursesByName(courseName);
        if (courses.isEmpty()) {
            while (btnShowMore.isDisplayed() & courses.isEmpty()) {
                btnShowMore.click();
                courses = findCoursesByName(courseName);
            }
            if (courses.isEmpty()) {
                throw new AssertionError(String.format("Курс по заданному имени \"%s\" не найден!", courseName));
            }
        }
        courses.get(0).click();
    }

    public void openRandomCourseTitle(String courseName) {
        WebElement targetCourse;
        List<WebElement> courses = findCoursesByName(courseName);
        if (courses.isEmpty()) {
            throw new AssertionError(String.format("Курсы по заданному имени \"%s\" не найдены!", courseName));
        }

        if (courses.size() == 1) {
            targetCourse = courses.get(0);
            System.out.println(String.format("Найден один курс с названием \"%s\". Открываем найденный курс.", courseName));
        } else {
            int randomIndex = ThreadLocalRandom.current().nextInt(courses.size());
            targetCourse = courses.get(randomIndex);
            System.out.println("Найдено курсов: " + courses.size() + ". Выбран случайный под номером: " + (randomIndex + 1));
        }
        targetCourse.click();
    }

    public void checkCtgCourses(String title) {
        for (WebElement link : linkFilters) {
            String linkText = link.getText();
            String expectedTitle = title.split(" \\(")[0];
            boolean isSelected = Boolean.parseBoolean(link.getAttribute("value"));
            if (linkText.equals(expectedTitle)) {
                Assertions.assertTrue(isSelected, String.format("Ошибка: некорректно выбрана категория напрвления:  %s!", link.getText()));
            } else {
                Assertions.assertFalse(isSelected, String.format("Ошибка: некорректно выбрана категория напрвления:  %s!", link.getText()));
            }
        }
    }

    // если в метод передавать false, возврашать коллекцию из min значений
    public Map<LocalDate, List<Element>> isMax(List<Element> elements, boolean isMax) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

        Map<LocalDate, List<Element>> groupedMap = elements.stream()
                .collect(Collectors.groupingBy(element ->
                        LocalDate.parse(element.text().split(" ·")[0], formatter)
                ));
        return groupedMap.entrySet().stream()
                .reduce(getSelector(isMax))
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .orElse(Map.of());
    }

    // раскрыть все курсы на экране, с помощью кнопки "Показать еще"
    public void showAllCourseTitle() {
        By locatorShowMore = By.cssSelector("button.cXVWAS");
        List<WebElement> buttons = byWebElements(By.cssSelector("button.cXVWAS"));
        while (!(byWebElements(locatorShowMore)).isEmpty() && buttons.get(0).isDisplayed()) {
            try {
                buttons.get(0).click();
                System.out.println("Кнопка 'Показать еще' успешно нажата.");
            } catch (Exception e) {
                System.out.println("Клик не удался или DOM обновился, выходим из цикла: " + e.getMessage());
                break; // Прерываем цикл при любой непредвиденной проблеме
            }
        }
        System.out.println("Все доступные курсы успешно раскрыты.");
    }

    public Map<LocalDate, List<Element>> onOfAfterDateStartCurses(List<Element> elements, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

        Map<LocalDate, List<Element>> groupedMap = elements.stream()
                .collect(Collectors.groupingBy(element ->
                        LocalDate.parse(element.text().split(" ·")[0], formatter)
                ));
        return groupedMap.entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(date))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
