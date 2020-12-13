package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.appmanagement.ApplicationState;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.openqa.selenium.WebElement;

final public class AppInteractionsHelper {

    @Step("Close the app")
    public static void closeAppUnderTest(AndroidDriver<WebElement> driver) {
        driver.closeApp();
    }

    @Step("Open the app")
    public static void openAppUnderTest(AndroidDriver<WebElement> driver) {
        driver.activateApp(ConfigManager.getDeviceGeneralConfig().appPackage());
    }

    @Step("Reset the app")
    public static void resetAppUnderTest(AndroidDriver<WebElement> driver) {
        driver.resetApp();
    }

    @Step("Install and open the app")
    public static void installAndOpenAppUnderTest(AndroidDriver<WebElement> driver) {
        driver.launchApp();
    }

    public static boolean isAppUnderTestOpened(AndroidDriver<WebElement> driver) {
        return driver.queryAppState(ConfigManager.getDeviceGeneralConfig().appPackage()).equals(ApplicationState.RUNNING_IN_FOREGROUND);
    }
}
