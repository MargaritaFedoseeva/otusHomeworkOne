package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import common.AbsCommon;
import di.Scoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public abstract class AbsBasePage<T> extends AbsCommon {
    private final String baseUrl = System.getProperty("base.url");

    public AbsBasePage(Scoped scoped) {
        super(scoped);
    }


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
                templateStr = templateStr.replace("$" + i + 1, data[i]);
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
}
