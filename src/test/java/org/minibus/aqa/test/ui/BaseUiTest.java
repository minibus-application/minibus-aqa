package org.minibus.aqa.test.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
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
    protected AppiumDriver<MobileElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        super.beforeSuite(context);

        if (ConfigManager.getAppiumConfig().host() != null && ConfigManager.getAppiumConfig().port() != null) {
            // assuming the appium server is standalone
            driver = Device.create(ConfigManager.getAppiumConfig().url(),
                    ConfigManager.getDeviceConfig(), ConfigManager.getDeviceGeneralConfig());
        } else {
            AppiumLocalManager.getService(new AppiumServiceBuilder().usingAnyFreePort()).start();
            driver = Device.create(AppiumLocalManager.getService().getServiceUrl(),
                    ConfigManager.getDeviceConfig(), ConfigManager.getDeviceGeneralConfig());
        }
    }

    @BeforeMethod(groups = {TestGroup.UI})
    public void beforeMethod() {
        String appPackage = ConfigManager.getDeviceGeneralConfig().appPackage();

        if (!isAppOpened(appPackage, ConfigManager.getGeneralConfig().elementTimeout())) {
            if (ConfigManager.getDeviceGeneralConfig().fullReset()) {
                LOGGER.info("The app isn't installed");
                LOGGER.info("Launch the app (install and open)");
                getDriver().launchApp();
            } else {
                LOGGER.info("Open the app " + appPackage);
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
        Device.quit();

        if (AppiumLocalManager.isRunning()) {
            AppiumLocalManager.stop();
        }
    }

    private boolean isAppOpened(String appPackage, int timeout) {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(timeout);
        LOGGER.info(String.format("Wait for the app '%s' to be opened (%s sec)", appPackage, timeout));
        do {
            if (getDriver().queryAppState(appPackage).equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
                LOGGER.info("The app is opened");
                return true;
            }
        }
        while (LocalDateTime.now().isBefore(endTime));

        LOGGER.info("The app isn't opened");
        return false;
    }
}
