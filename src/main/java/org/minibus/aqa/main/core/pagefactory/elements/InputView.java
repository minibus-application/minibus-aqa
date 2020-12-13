package org.minibus.aqa.main.core.pagefactory.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class InputView extends AndroidView {

    public InputView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public void set(String text) {
        LOGGER.trace("Set '{}' text to view: {}", text, getName());
        clear();
        getWrappedElement().sendKeys(text);
    }

    public void append(String text) {
        LOGGER.trace("Append '{}' text to view: {}", text, getName());
        getWrappedElement().sendKeys(text);
    }

    public void clear() {
        getWrappedElement().clear();
    }
}
