package org.minibus.aqa.domain.screens;

import io.appium.java_client.MobileElement;
import org.minibus.aqa.core.pagefactory.elements.BaseView;
import org.openqa.selenium.WebElement;

public class Button extends BaseView {

    private String name;

    public Button(WebElement webElement) {
        super(webElement);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
