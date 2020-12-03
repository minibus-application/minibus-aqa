package org.minibus.aqa.main.core.pagefactory.handlers;

import org.minibus.aqa.main.core.pagefactory.ViewFieldDecorator;
import org.minibus.aqa.main.core.pagefactory.elements.base.Layout;
import org.minibus.aqa.main.core.pagefactory.factories.LayoutFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocatingLayoutListHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<? extends Layout> layoutClass;
    private final String logicalName;
    private final By by;

    public LocatingLayoutListHandler(Class<? extends Layout> layoutClass, ElementLocator locator, String logicalName, By by) {
        this.layoutClass = layoutClass;
        this.locator = locator;
        this.logicalName = logicalName;
        this.by = by;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elementsToWrap = locator.findElements();
        List<Layout> layouts = new ArrayList<>();

        for (WebElement elementToWrap : elementsToWrap) {
            Layout layout = new LayoutFactory().create(layoutClass, elementToWrap, logicalName, by);
            layouts.add(layout);
            PageFactory.initElements(new ViewFieldDecorator(elementToWrap), layout);
        }

        try {
            return method.invoke(layouts, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
