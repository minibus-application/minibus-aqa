package org.minibus.aqa.test.ui;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.main.core.env.AppiumLocalManager;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.test.BaseTest;
import org.minibus.aqa.test.TestGroup;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.LocalDateTime;

import static org.minibus.aqa.main.core.env.Device.getDriver;

public abstract class BaseUiTest extends BaseTest {
    protected AndroidDriver<MobileElement> driver;

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
    }

    @BeforeMethod(groups = {TestGroup.UI})
    public void beforeMethod() {
        String appPackage = ConfigManager.getDeviceGeneralConfig().appPackage();

        boolean isAppOpened = ConfigManager.getDeviceGeneralConfig().autoLaunch()
                && isAppOpened(appPackage, ConfigManager.getGeneralConfig().elementTimeout());

        if (!isAppOpened) {
            if (ConfigManager.getDeviceGeneralConfig().fullReset()) {
                LOGGER.debug(String.format("%s is not installed", appPackage));
                LOGGER.debug(String.format("Launching %s", appPackage));
                getDriver().launchApp();
            } else {
                LOGGER.debug(String.format("Opening %s", appPackage));
                getDriver().activateApp(appPackage);
            }
        }
    }

    @AfterMethod(groups = {TestGroup.UI})
    public void afterMethod() {
        getDriver().closeApp();
    }

    @AfterSuite(groups = {TestGroup.UI})
    public void afterSuite(ITestContext context) {
        Device.getDriver().quit();

        if (AppiumLocalManager.isRunning()) {
            AppiumLocalManager.stop();
        }
    }

    private boolean isAppOpened(String appPackage, int timeoutSec) {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(timeoutSec);
        LOGGER.debug(String.format("Waiting for %s to open (%s sec)", appPackage, timeoutSec));

        do {
            if (getDriver().queryAppState(appPackage).equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
                LOGGER.debug(String.format("%s is opened", appPackage));
                return true;
            }
        }
        while (LocalDateTime.now().isBefore(endTime));

        LOGGER.debug(String.format("%s is not opened", appPackage));
        return false;
    }
}
