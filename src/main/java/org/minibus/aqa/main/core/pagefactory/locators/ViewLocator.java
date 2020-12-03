package org.minibus.aqa.main.core.pagefactory.locators;

import org.minibus.aqa.main.core.pagefactory.annotations.ViewAnnotation;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;

public class ViewLocator extends DefaultElementLocator {

    public ViewLocator(SearchContext searchContext, Field field) {
        this(searchContext, new ViewAnnotation(field));
    }

    public ViewLocator(SearchContext searchContext, AbstractAnnotations annotations) {
        super(searchContext, annotations);
    }
}
