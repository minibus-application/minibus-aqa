package org.minibus.aqa.tests;

import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.core.common.env.AppiumLocalManager;
import org.minibus.aqa.core.common.handlers.TestInterceptor;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.device.*;
import org.minibus.aqa.core.common.env.AppiumConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.minibus.aqa.core.common.handlers.TestListener;
import org.minibus.aqa.core.common.handlers.TestLogger;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;


@Listeners(TestInterceptor.class)
public abstract class BaseTest {

    private Environment environment;
    private AppiumConfig appiumConfig;
    private DeviceConfig deviceConfig;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        ((TestRunner) context).getSuite().getXmlSuite().setThreadCount(1);
        ((TestRunner) context).addListener(TestListener.getInstance());

        TestLogger.get().startTests(context.getSuite().getXmlSuite().getTests().size());

        environment = Environment.getInstance();
        deviceConfig = Environment.getInstance().getDeviceConfig();
        appiumConfig = Environment.getInstance().getAppiumConfig();

        if (!appiumConfig.isStandalone()) {
            AppiumLocalManager.getService(new AppiumServiceBuilder().usingAnyFreePort()).start();
        }

        new Device(deviceConfig).initDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        Device.quit();

        if (!appiumConfig.isStandalone()) {
            AppiumLocalManager.getService().stop();
        }

        TestLogger.get().finishTests(context.getFailedTests().size(), context.getPassedTests().size());
    }
}
