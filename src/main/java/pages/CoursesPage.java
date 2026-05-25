package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import com.google.inject.Inject;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Path("/")
@UrlTemplates(
        @Template("catalog/courses"))
public class CoursesPage extends AbsBasePage<CoursesPage> {

    @Inject
    public CoursesPage(WebDriver driver) {
        super(driver);

    }

    @FindBy(css = "h6 div.jEGzDf")
    private List<WebElement> listCoursesTitle;


    @FindBy(css = "button.cXVWAS")
//    @FindBy(xpath = "//button[contains(text(), 'Показать еще')]")
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

    //реализация 3: если курса нет на экране, то выполняем нажатие на кнопку "Показать еще", если есть открываем
    public void openCourseTitle(String courseName) {
        List<WebElement> courses = findCoursesByName(courseName);
        while (btnShowMore.isDisplayed() & courses.isEmpty()) {
            btnShowMore.click();
            courses = findCoursesByName(courseName);
        }
        courses.get(0).click();
    }

    //        если в метод передавать false, возврашать коллекцию из min значений
//    public Map<LocalDate, List<Element>> isMax(List<Element> elements, boolean isMax) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
//
//        Map<LocalDate, List<Element>> groupedMap = elements.stream()
//                .collect(Collectors.groupingBy(element ->
//                        LocalDate.parse(element.text().split(" ·")[0], formatter)
//                ));
//        return (isMax
//                ? groupedMap.entrySet().stream().max(Map.Entry.comparingByKey())
//                : groupedMap.entrySet().stream().min(Map.Entry.comparingByKey()))
//                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
//                .orElse(Map.of());
//    }

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
}
