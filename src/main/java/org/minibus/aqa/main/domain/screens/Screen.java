package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public interface Screen {

    boolean isOpened();

    boolean isOpened(int timeoutSec);

    String getName();

    String getTitle();

    AppiumDriver<MobileElement> getDriver();
}
