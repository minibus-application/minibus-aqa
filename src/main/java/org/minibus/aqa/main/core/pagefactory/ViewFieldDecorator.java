package org.minibus.aqa.main.core.pagefactory;

import org.minibus.aqa.main.core.pagefactory.elements.base.BaseLayout;
import org.minibus.aqa.main.core.pagefactory.elements.base.BaseView;
import org.minibus.aqa.main.core.pagefactory.elements.base.Layout;
import org.minibus.aqa.main.core.pagefactory.elements.base.View;
import org.minibus.aqa.main.core.pagefactory.factories.ViewFactory;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.locators.ViewLocatorFactory;
import org.minibus.aqa.main.core.pagefactory.handlers.LocatingLayoutListHandler;
import org.minibus.aqa.main.core.pagefactory.handlers.LocatingViewListHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.*;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import java.lang.reflect.*;
import java.util.List;

public class ViewFieldDecorator implements FieldDecorator {
    private final ViewFactory viewFactory;
    private final ViewLocatorFactory locatorFactory;

    public ViewFieldDecorator(SearchContext searchContext) {
        this.locatorFactory = new ViewLocatorFactory(searchContext);
        this.viewFactory = new ViewFactory();
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        Class<?> fieldType = field.getType();
        ElementLocator locator = createLocator(field);

        if (loader == null) {
            return null;
        }

        if (List.class.isAssignableFrom(fieldType)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericClass = (Class<?>) genericType.getActualTypeArguments()[0];

            if (WebElement.class.equals(genericClass)) {
                return proxyForListLocator(loader, locator);
            } else if (BaseView.class.isAssignableFrom(genericClass)) {
                return decorateViewList(loader, field, genericClass, locator);
            } else if (BaseLayout.class.isAssignableFrom(genericClass)) {
                return decorateLayoutList(loader, field, genericClass, locator);
            } else {
                return null;
            }
        }

        if (BaseLayout.class.isAssignableFrom(field.getType())) {
            return decorateLayout(loader, field, locator);
        }

        if (BaseView.class.isAssignableFrom(field.getType())) {
            return decorateView(loader, field, locator);
        }

        if (WebElement.class.isAssignableFrom(field.getType()) && !field.getName().equals("wrappedElement")) {
            return proxyForLocator(loader, locator);
        }

        return null;
    }

    private ElementLocator createLocator(final Field field) {
        return locatorFactory.createLocator(field);
    }

    @SuppressWarnings("unchecked")
    private Object decorateView(final ClassLoader loader, final Field field, ElementLocator locator) {
        final WebElement wrappedElement = proxyForLocator(loader, locator);
        return viewFactory.createInstance(
                (Class<? extends View>) field.getType(), wrappedElement, getFieldLogicalName(field), getFieldBy(field)
        );
    }

    @SuppressWarnings("unchecked")
    private Object decorateLayout(final ClassLoader loader, final Field field, ElementLocator locator) {
        final WebElement wrappedElement = proxyForLocator(loader, locator);
        final Layout layout = viewFactory.createInstance(
                (Class<? extends Layout>) field.getType(), wrappedElement, getFieldLogicalName(field), getFieldBy(field)
        );

        PageFactory.initElements(new ViewFieldDecorator(wrappedElement), layout);
        return layout;
    }

    @SuppressWarnings("unchecked")
    private List<View> decorateViewList(final ClassLoader loader, Field field, Class<?> genericType, ElementLocator locator) {
        InvocationHandler handler = new LocatingViewListHandler(
                (Class<? extends View>) genericType, locator, getFieldLogicalName(field), getFieldBy(field)
        );
        return (List<View>) Proxy.newProxyInstance(loader, new Class[] {List.class}, handler);
    }

    @SuppressWarnings("unchecked")
    private List<Layout> decorateLayoutList(final ClassLoader loader, Field field, Class<?> genericType, ElementLocator locator) {
        InvocationHandler handler = new LocatingLayoutListHandler(
                (Class<? extends Layout>) genericType, locator, getFieldLogicalName(field), getFieldBy(field)
        );
        return (List<Layout>) Proxy.newProxyInstance(loader, new Class[] {List.class}, handler);
    }

    private WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementHandler(locator);
        return (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
    }

    @SuppressWarnings("unchecked")
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementListHandler(locator);
        return (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }

    private String getFieldLogicalName(Field field) {
        ViewInfo viewAnnotation = field.getAnnotation(ViewInfo.class);
        return viewAnnotation != null ? viewAnnotation.name() : "";
    }

    private By getFieldBy(Field field) {
        ViewInfo viewAnnotation = field.getAnnotation(ViewInfo.class);
        return viewAnnotation != null ? ViewHelper.resolveFindBy(viewAnnotation.findBy()) : null;
    }
}
