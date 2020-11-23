package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.pagefactory.Widget;
import org.apache.logging.log4j.LogManager;
import org.minibus.aqa.main.core.handlers.TestListener;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.Logger;


public abstract class BaseWidget extends Widget {
    protected static final Logger LOGGER = LogManager.getLogger(BaseWidget.class);

    public BaseWidget(WebElement element) {
        super(element);
    }

    protected void click() {
        getWrappedElement().click();
    }
}
