package org.minibus.aqa.tests;

import org.minibus.aqa.core.common.AppiumLocalManager;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.device.*;
import org.minibus.aqa.core.common.env.AppiumConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    private BaseDevice device;
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

        device = DeviceFactory.create(deviceConfig);

        // todo with logback
        System.out.println(appiumConfig.toString());
        System.out.println(device.getCapabilities().toString());
    }

    protected BaseDevice getDevice() {
        return device;
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite () {
        getDevice().getDriver().closeApp();
        getDevice().getDriver().quit();

        if (!appiumConfig.isStandalone()) {
            AppiumLocalManager.getInstance().terminate();
        }
    }
}
