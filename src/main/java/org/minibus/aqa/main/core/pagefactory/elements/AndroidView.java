package org.minibus.aqa.main.core.pagefactory.elements;

import org.minibus.aqa.main.core.pagefactory.elements.base.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AndroidView extends BaseView {

    public AndroidView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }
}
