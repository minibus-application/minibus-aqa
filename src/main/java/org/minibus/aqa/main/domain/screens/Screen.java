package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public interface Screen {

    boolean isOpened();

    boolean isOpened(int timeoutSec);

    String getScreenName();

    String getTitle();

    AndroidDriver<AndroidElement> getDriver();
}
