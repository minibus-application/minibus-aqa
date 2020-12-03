package org.minibus.aqa.main.core.page_factory.factories;

import org.minibus.aqa.main.core.page_factory.elements.Layout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;

public class LayoutFactory {

    public <T extends Layout> T create(final Class<T> viewGroupClass, final WebElement wrappedElement, final String logicalName, final By by) {
        try {
            return viewGroupClass
                    .getDeclaredConstructor(WebElement.class, String.class, By.class)
                    .newInstance(wrappedElement, logicalName, by);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
