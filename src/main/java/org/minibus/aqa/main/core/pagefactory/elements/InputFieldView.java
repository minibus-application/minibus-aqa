package org.minibus.aqa.main.core.pagefactory.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class InputFieldView extends AndroidView {

    public InputFieldView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public void set(String text) {
        LOGGER.trace("Set '{}' text to {} view", text, getLogicalName());
        clear();
        getWrappedElement().sendKeys(text);
    }

    public void append(String text) {
        LOGGER.trace("Append '{}' text to {} view", text, getLogicalName());
        getWrappedElement().sendKeys(text);
    }

    public void clear() {
        getWrappedElement().clear();
    }
}
