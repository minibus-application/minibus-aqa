package org.minibus.aqa.main.core.pagefactory.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TextView extends AndroidView {

    public TextView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }
}
