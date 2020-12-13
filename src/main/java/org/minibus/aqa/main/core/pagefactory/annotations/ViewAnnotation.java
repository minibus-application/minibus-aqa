package org.minibus.aqa.main.core.pagefactory.annotations;

import org.openqa.selenium.By;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ViewAnnotation extends Annotations {
    private Field field;

    public ViewAnnotation(Field field) {
        super(field);
        this.field = field;
    }

    @Override
    public By buildBy() {
        assertValidAnnotations();

        By ans = null;

        for (Annotation annotation : field.getDeclaredAnnotations()) {
            AbstractFindByBuilder builder = null;
            Class<? extends Annotation> annotationType = annotation.annotationType();

            if (annotationType.equals(ViewInfo.class)) {
                ViewInfo viewInfo = field.getAnnotation(ViewInfo.class);
                FindBy findBy = viewInfo.findBy();
                annotation = findBy;
                annotationType = findBy.annotationType();
            }

            if (annotationType.isAnnotationPresent(PageFactoryFinder.class)) {
                try {
                    builder = annotationType.getAnnotation(PageFactoryFinder.class).value().newInstance();
                } catch (ReflectiveOperationException e) {
                    // Fall through.
                }
            }

            if (builder != null) {
                ans = builder.buildIt(annotation, field);
                break;
            }
        }

        if (ans == null) {
            ans = buildByFromDefault();
        }

        if (ans == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }

        return ans;
    }

    protected void assertValidAnnotations() {
        FindBys findBys;
        FindAll findAll;
        FindBy findBy;

        if (field.isAnnotationPresent(ViewInfo.class)) {
            ViewInfo viewInfo = field.getAnnotation(ViewInfo.class);
            findBys = viewInfo.annotationType().getAnnotation(FindBys.class);
            findAll = viewInfo.annotationType().getAnnotation(FindAll.class);
            findBy = viewInfo.annotationType().getAnnotation(FindBy.class);
        } else {
            findBys = field.getAnnotation(FindBys.class);
            findAll = field.getAnnotation(FindAll.class);
            findBy = field.getAnnotation(FindBy.class);
        }

        if (findBys != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindBys' annotation, " +
                    "you must not also use a '@FindBy' annotation");
        }
        if (findAll != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
                    "you must not also use a '@FindBy' annotation");
        }
        if (findAll != null && findBys != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
                    "you must not also use a '@FindBys' annotation");
        }
    }
}
