package org.minibus.aqa.main.core.pagefactory.factories;

import org.minibus.aqa.main.core.pagefactory.elements.base.View;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;

public class ViewFactory {

    public <T extends View> T createInstance(final Class<T> clazz,
                                     final WebElement wrappedElement,
                                     final String logicalName,
                                     final By by) {
        try {
            return clazz
                    .getDeclaredConstructor(WebElement.class, String.class, By.class)
                    .newInstance(wrappedElement, logicalName, by);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
