package org.minibus.aqa.main.core.pagefactory.elements.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class BaseLayout implements Layout {
    private final WebElement wrappedElement;
    private final String logicalName;
    private final By by;

    protected BaseLayout(final WebElement wrappedElement, final String logicalName, final By by) {
        this.wrappedElement = wrappedElement;
        this.logicalName = logicalName;
        this.by = by;
    }

    @Override
    public WebElement getWrappedElement() {
        return wrappedElement;
    }

    @Override
    public String getLogicalName() {
        return logicalName;
    }

    @Override
    public By getBy() {
        return by;
    }
}
