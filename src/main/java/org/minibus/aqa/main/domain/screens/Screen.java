package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public interface Screen {

    boolean isOpened();

    boolean isOpened(int timeoutSec);

    String getScreenName();

    String getTitle();

    AndroidDriver<MobileElement> getDriver();
}
