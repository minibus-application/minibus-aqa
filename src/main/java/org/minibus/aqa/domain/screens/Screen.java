package org.minibus.aqa.domain.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public interface Screen {

    boolean isOpened();
    boolean isOpened(int timeout);
    String getName();
    String getTitle();

    AppiumDriver<MobileElement> getDriver();
}
