package org.minibus.aqa.test.ui;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.main.core.env.AppiumLocalManager;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.helpers.AppInteractionsHelper;
import org.minibus.aqa.main.core.helpers.MobileCommandHelper;
import org.minibus.aqa.test.BaseTest;
import org.minibus.aqa.test.TestGroup;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.LocalDateTime;

public abstract class BaseUiTest extends BaseTest {
    private AndroidDriver<AndroidElement> driver;

    @BeforeSuite(groups = {TestGroup.UI})
    public void beforeSuite(ITestContext context) {
        super.beforeSuite(context);

        if (ConfigManager.getAppiumConfig().host() != null && ConfigManager.getAppiumConfig().port() != null) {
            // assuming the appium server is standalone
            driver = new Device().create(ConfigManager.getAppiumConfig().url(),
                    ConfigManager.getDeviceConfig(), ConfigManager.getDeviceGeneralConfig());
        } else {
            AppiumLocalManager.getService(new AppiumServiceBuilder().usingAnyFreePort()).start();
            driver = new Device().create(AppiumLocalManager.getService().getServiceUrl(),
                    ConfigManager.getDeviceConfig(), ConfigManager.getDeviceGeneralConfig());
        }

        // disable device animations to increase performance and speed
        MobileCommandHelper.shell("settings", "put", "global", "window_animation_scale", "0");
        MobileCommandHelper.shell("settings", "put", "global", "transition_animation_scale", "0");
        MobileCommandHelper.shell("settings", "put", "global", "animator_duration_scale", "0");
    }

    @BeforeMethod(groups = {TestGroup.UI})
    public void beforeMethod() {
        boolean isAppOpened = ConfigManager.getDeviceGeneralConfig().autoLaunch()
                && waitUntilAppOpened(ConfigManager.getGeneralConfig().elementTimeout());

        if (!isAppOpened) {
            if (ConfigManager.getDeviceGeneralConfig().fullReset()) {
                AppInteractionsHelper.installAndOpenAppUnderTest(driver);
            } else {
                AppInteractionsHelper.openAppUnderTest(driver);
            }
        }
    }

    @AfterMethod(groups = {TestGroup.UI})
    public void afterMethod() {
        AppInteractionsHelper.closeAppUnderTest(driver);
    }

    @AfterSuite(groups = {TestGroup.UI})
    public void afterSuite(ITestContext context) {
        MobileCommandHelper.shell("settings", "put", "global", "window_animation_scale", "1");
        MobileCommandHelper.shell("settings", "put", "global", "transition_animation_scale", "1");
        MobileCommandHelper.shell("settings", "put", "global", "animator_duration_scale", "1");

        Device.quit();

        if (AppiumLocalManager.isRunning()) AppiumLocalManager.stop();
    }

    protected AndroidDriver<AndroidElement> getDriver() {
        return driver;
    }

    private boolean waitUntilAppOpened(int timeoutSec) {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(timeoutSec);
        LOGGER.debug("Waiting for the app to open ({} sec)", timeoutSec);

        do {
            if (AppInteractionsHelper.isAppUnderTestOpened(driver)) {
                LOGGER.debug("The app is opened");
                return true;
            }
        }
        while (LocalDateTime.now().isBefore(endTime));

        LOGGER.debug("The app is not opened");
        return false;
    }
}
