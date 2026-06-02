package pages;

import annotations.Name;
import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import common.AbsCommon;
import di.Scoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

public abstract class AbsBasePage<T> extends AbsCommon {
    private final String baseUrl = System.getProperty("base.url");

    private Scoped scoped;

    public AbsBasePage(Scoped scoped) {
        super(scoped);

//        this.scoped=scoped;
    }

//    @SuppressWarnings("unchecked")
//    public <T extends AbsBasePage> T getCurrentPage() {
//        if (scoped. == null) {
//            throw new IllegalStateException("Текущая страница не задана! Вызовите setCurrentPage перед поиском элементов.");
//        }
//        return (T) scoped.;
//    }


    private String getPath() {
        Class<T> clazz = (Class<T>) getClass();
        if (clazz.isAnnotationPresent(Path.class)) {
            Path path = clazz.getDeclaredAnnotation(Path.class);
            return path.value();
        }
        return "";
    }

    private String getPathByTemplate(String... data) {
        Class<T> clazz = (Class<T>) getClass();
        if (clazz.isAnnotationPresent(UrlTemplates.class)) {
            UrlTemplates urlTemplates = clazz.getDeclaredAnnotation(UrlTemplates.class);
            Template template = urlTemplates.value();

            String templateStr = template.value();
            for (int i = 0; i < data.length; i++) {
                templateStr = templateStr.replace("$" + (i + 1), data[i]);
            }
            return templateStr;
        }
        return "";
    }

    public T open() {
        driver.get(baseUrl + getPath());
        return (T) this;
    }

    public T open(String... data) {
        String url = getPath().isEmpty() ? baseUrl + getPathByTemplate(data) : baseUrl + getPath() + getPathByTemplate(data);
        driver.get(url);
        return (T) this;
    }

    public Document getPage(String... data) {
        String url = getPath().isEmpty() ? baseUrl + getPathByTemplate(data) : baseUrl + getPath() + getPathByTemplate(data);
        driver.get(url);
        String pageSource = driver.getPageSource();
        return Jsoup.parse(pageSource);
    }

    public Document getPage() {
        String pageSource = driver.getPageSource();
        return Jsoup.parse(pageSource);
    }

    @SuppressWarnings("unchecked")
    public List<WebElement> getCollection(String name) {
        Class<T> clazz = (Class<T>) getClass();
        // Перебираем все поля класса страницы
        for (Field field : clazz.getClass().getDeclaredFields()) {
            // Проверяем, есть ли у поля аннотация @Name
            if (field.isAnnotationPresent(Name.class)) {
                Name annotation = field.getAnnotation(Name.class);
                // Если имя совпало с текстом из шага Cucumber
                if (annotation.value().equals(name)) {
                    try {
                        field.setAccessible(true);
                        return (List<WebElement>) field.get(clazz);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Ошибка доступа к полю: " + name, e);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Список с именем '" + name + "' не найден на странице!");
    }

    public void navigateBack() {
        driver.navigate().back();
    }
}
