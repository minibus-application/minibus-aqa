package org.minibus.aqa.main.core.pagefactory.handlers;

import org.minibus.aqa.main.core.pagefactory.elements.base.View;
import org.minibus.aqa.main.core.pagefactory.factories.ViewFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocatingViewListHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<? extends View> viewClass;
    private final String logicalName;
    private final By by;

    public LocatingViewListHandler(Class<? extends View> viewClass, ElementLocator locator, String logicalName, By by) {
        this.viewClass = viewClass;
        this.locator = locator;
        this.logicalName = logicalName;
        this.by = by;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elementsToWrap = locator.findElements();
        List<View> views = new ArrayList<>();

        for (WebElement elementToWrap : elementsToWrap) {
            views.add(new ViewFactory().createInstance(viewClass, elementToWrap, logicalName, by));
        }

        try {
            return method.invoke(views, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
