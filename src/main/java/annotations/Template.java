package annotations;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(UrlTemplate.class)
public @interface Template {
    String name() default "default";

    String value();
}
