package org.minibus.aqa.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.core.common.env.AppiumLocalManager;
import org.minibus.aqa.core.common.env.Device;
import org.minibus.aqa.core.common.handlers.TestAssertion;
import org.minibus.aqa.core.common.handlers.TestInterceptor;
import org.minibus.aqa.core.common.env.config.ConfigManager;
import org.minibus.aqa.core.common.handlers.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;


@Listeners(TestInterceptor.class)
public abstract class BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected static TestAssertion test = new TestAssertion();
    protected AppiumDriver<MobileElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        ((TestRunner) context).getSuite().getXmlSuite().setThreadCount(1);
        ((TestRunner) context).addListener(TestListener.getInstance());

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

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method, ITestResult iTestResult) {

    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        Device.quit();

        if (AppiumLocalManager.isRunning()) {
            AppiumLocalManager.stop();
        }
    }
}
