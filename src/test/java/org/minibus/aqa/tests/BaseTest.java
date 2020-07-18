package org.minibus.aqa.tests;

import org.minibus.aqa.core.common.env.AppiumLocalManager;
import org.minibus.aqa.core.common.handlers.TestInterceptor;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.device.*;
import org.minibus.aqa.core.common.env.AppiumConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestInterceptor.class)
public abstract class BaseTest {

    private Environment environment;
    private AppiumConfig appiumConfig;
    private DeviceConfig deviceConfig;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        environment = Environment.getInstance();
        deviceConfig = Environment.getInstance().getDeviceConfig();
        appiumConfig = Environment.getInstance().getAppiumConfig();

        if (!appiumConfig.isStandalone()) {
            AppiumLocalManager.getInstance().createServiceDefault().start();
        }

        new Device(deviceConfig).initDriver();

        // todo with logback
        // System.out.println(appiumConfig.toString());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite () {
        Device.getDriver().closeApp();
        Device.getDriver().quit();

        if (!appiumConfig.isStandalone()) {
            if (AppiumLocalManager.getInstance().isRunning()) {
                AppiumLocalManager.getInstance().terminate();
            }
        }
    }
}
