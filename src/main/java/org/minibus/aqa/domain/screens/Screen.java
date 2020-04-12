package org.minibus.aqa.domain.screens;

import io.appium.java_client.AppiumDriver;
import org.minibus.aqa.core.common.env.device.Device;

public interface Screen {

    boolean isOpened();
    boolean isOpened(int timeout);
    String getName();

    AppiumDriver getDriver();
    Device getDevice();
}
