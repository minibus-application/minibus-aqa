package org.minibus.aqa.main.core.pagefactory.locators;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class ViewLocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;

    public ViewLocatorFactory(SearchContext context) {
        this.searchContext = context;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        return new ViewLocator(searchContext, field);
    }
}
