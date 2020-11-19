package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.pagefactory.Widget;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseWidget extends Widget {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseWidget.class);

    public BaseWidget(WebElement element) {
        super(element);
    }

    protected void click() {
        getWrappedElement().click();
    }
}
