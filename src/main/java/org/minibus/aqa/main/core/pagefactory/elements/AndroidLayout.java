package org.minibus.aqa.main.core.pagefactory.elements;

import org.minibus.aqa.main.core.pagefactory.elements.base.BaseLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AndroidLayout extends BaseLayout {

    public AndroidLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }
}
