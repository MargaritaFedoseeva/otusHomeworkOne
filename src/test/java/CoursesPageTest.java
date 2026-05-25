import extensions.UIExtension;
import jakarta.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CoursesPage;
import pages.LessonPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(UIExtension.class)
public class CoursesPageTest {

    @Inject
    private CoursesPage coursesPage;
    @Inject
    private LessonPage lessonPage;

    @Test
    public void openCourseCheckTitle() {
        coursesPage.open("");
        coursesPage.openCourseTitle("Управление AI/ML-продуктом");
        assertEquals(lessonPage.getLessonTitle(), "Управление AI/ML-продуктом");
    }

    @Test
    public void checkCoursesTitleMinDateAndMaxDate() {
        coursesPage.open("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
        Document doc = coursesPage.getPage("");

        List<Element> coursesDate = doc.select("div.sc-18q05a6-1.bwGwUO>a .ieVVRJ div");

        Map<LocalDate, List<Element>> coursesMinDate = coursesPage.isMax(coursesDate, false);
        Map<LocalDate, List<Element>> coursesMaxDate = coursesPage.isMax(coursesDate, true);

        Map<LocalDate, List<Element>> coursesFiltered = Stream.of(coursesMinDate, coursesMaxDate)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.flatMapping(entry -> entry.getValue().stream(), Collectors.toList())
                ));

        coursesFiltered.forEach((date, elements) -> {
            elements.forEach(element -> {
                String courseName = element.parent().parent().parent().selectFirst("h6").text();
                coursesPage.openCourseTitle(courseName); //открываем курс
                assertEquals(courseName, lessonPage.getLessonTitle()); //проверяем название
                assertEquals(date.format(formatter), lessonPage.getLessonStartDate()); //проверяем дату старта курса
                coursesPage.open("");
            });
        });
    }
}
