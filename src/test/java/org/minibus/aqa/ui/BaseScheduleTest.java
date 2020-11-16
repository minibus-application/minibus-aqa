package org.minibus.aqa.ui;

import org.minibus.aqa.BaseTest;
import org.minibus.aqa.core.env.config.ConfigManager;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static org.minibus.aqa.core.env.Device.getDriver;

public class BaseScheduleTest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, ITestResult iTestResult) {
        super.beforeMethod(method, iTestResult);
        if (!ConfigManager.getDeviceGeneralConfig().fullReset()) {
            getDriver().resetApp();
        }
    }
}
