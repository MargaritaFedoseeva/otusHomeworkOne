package pages;

import annotations.Name;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

public class PageManager {

    // Храним текущую страницу как универсальный BasePage
    private AbsBasePage currentPage;

    // Метод для установки текущей активной страницы (вызывается в тестах при переходе)
    public void setCurrentPage(AbsBasePage page) {
        this.currentPage = page;
    }

    // Возвращает текущую страницу, автоматически приводя её к нужному классу тестов
    @SuppressWarnings("unchecked")
    public <T extends AbsBasePage> T getCurrentPage() {
        if (currentPage == null) {
            throw new IllegalStateException("Текущая страница не задана! Вызовите setCurrentPage перед поиском элементов.");
        }
        return (T) currentPage;
    }

    // Универсальный метод поиска коллекции на ЛЮБОЙ текущей странице
    @SuppressWarnings("unchecked")
    public List<WebElement> getCollection(String name) {
        // Запрашиваем класс именно текущей активной страницы (MainPage, CatalogPage и т.д.)
        Class<?> pageClass = getCurrentPage().getClass();

        // Проходим по всей иерархии классов (на случай, если элементы унаследованы от BasePage)
        while (pageClass != null && pageClass != Object.class) {
            for (Field field : pageClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Name.class)) {
                    Name annotation = field.getAnnotation(Name.class);

                    if (annotation.value().equals(name)) {
                        try {
                            field.setAccessible(true);
                            // Извлекаем коллекцию из текущего объекта страницы
                            return (List<WebElement>) field.get(currentPage);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Ошибка доступа к полю: " + name, e);
                        }
                    }
                }
            }
            // Поднимаемся выше по иерархии классов, если элемент не найден в текущем
            pageClass = pageClass.getSuperclass();
        }

        throw new IllegalArgumentException("Список с именем '" + name + "' не найден на странице " + currentPage.getClass().getSimpleName());
    }
}
