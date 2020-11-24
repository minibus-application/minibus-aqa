package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.pagefactory.Widget;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.Logger;


public abstract class BaseWidget extends Widget {
    protected static final Logger LOGGER = LogManager.getLogger(BaseWidget.class);

    public BaseWidget(WebElement element) {
        super(element);
    }

    @Step("Click on the element")
    protected void click() {
        getWrappedElement().click();
    }
}
