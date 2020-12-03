package org.minibus.aqa.main.core.pagefactory.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ButtonView extends AndroidView {

    public ButtonView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }
}
