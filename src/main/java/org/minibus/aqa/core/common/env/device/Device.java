package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.AppiumDriver;
import org.minibus.aqa.core.common.env.Config;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public interface Device {

    AppiumDriver initDriver(URL serverUrl, DesiredCapabilities capabilities);

    AppiumDriver getDriver();

    Config getConfig();

    DesiredCapabilities getCapabilities();

    default boolean installApp(String appPath) {
        getDriver().installApp(appPath);
        return isAppInstalled(appPath);
    }

    default boolean isAppInstalled(String appPath) {
        return getDriver().isAppInstalled(appPath);
    }

    default void openTestApp(String appPath) {
        getDriver().launchApp();
    }

    void closeApp();
    void sendAppBackground();
    void resetApp();
    void removeApp();
    void terminateApp();
    void getAppState();
    void getCurrentActivity();
    void getCurrentPackage();
    void startActivity();
    void setClipboard();
    void getClipboard();

//    void powerAc();
//    void powerCapacity();

    void pushFile();
    void pullFile();
    void pullFolder();

    void lock();
    void unlock();
    void isLocked();
    void rotate();

    void sendKeycode();
    void sendLongKeycode();
    void hideKeyboard();
    void isKeyboardShown();

    void toggleAirplaneMode();
    void toggleData();
    void toggleWifi();
    void toggleLocationServices();

    void startScreenRecording();
    void stopScreenRecording();

//    void setNetworkSpeed();
}
