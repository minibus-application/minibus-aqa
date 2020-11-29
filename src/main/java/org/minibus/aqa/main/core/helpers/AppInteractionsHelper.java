package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.appmanagement.ApplicationState;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.config.ConfigManager;

final public class AppInteractionsHelper {

    @Step("Close the app")
    public static void closeAppUnderTest(AndroidDriver<AndroidElement> driver) {
        driver.closeApp();
    }

    @Step("Open the app")
    public static void openAppUnderTest(AndroidDriver<AndroidElement> driver) {
        driver.activateApp(ConfigManager.getDeviceGeneralConfig().appPackage());
    }

    @Step("Reset the app")
    public static void resetAppUnderTest(AndroidDriver<AndroidElement> driver) {
        driver.resetApp();
    }

    @Step("Install and open the app")
    public static void installAndOpenAppUnderTest(AndroidDriver<AndroidElement> driver) {
        driver.launchApp();
    }

    public static boolean isAppUnderTestOpened(AndroidDriver<AndroidElement> driver) {
        return driver.queryAppState(ConfigManager.getDeviceGeneralConfig().appPackage()).equals(ApplicationState.RUNNING_IN_FOREGROUND);
    }
}
