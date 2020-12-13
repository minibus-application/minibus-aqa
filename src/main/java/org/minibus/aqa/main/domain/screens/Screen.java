package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.WebElement;

public interface Screen {

    boolean isOpened();

    boolean isOpened(int timeoutSec);

    String getScreenName();

    String getTitle();

    AndroidDriver<WebElement> getDriver();
}
