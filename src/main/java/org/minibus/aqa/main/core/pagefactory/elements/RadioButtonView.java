package org.minibus.aqa.main.core.pagefactory.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RadioButtonView extends AndroidView {

    public RadioButtonView(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public void check() {
        super.tap();
    }

    public boolean isChecked() {
        return Boolean.parseBoolean(getWrappedElement().getAttribute("checked"));
    }
}
