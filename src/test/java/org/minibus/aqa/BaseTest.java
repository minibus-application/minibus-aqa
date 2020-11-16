package org.minibus.aqa;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.minibus.aqa.core.env.AppEnvironment;
import org.minibus.aqa.core.env.AppiumLocalManager;
import org.minibus.aqa.core.env.Device;
import org.minibus.aqa.core.handlers.TestAssertion;
import org.minibus.aqa.core.handlers.TestInterceptor;
import org.minibus.aqa.core.env.config.ConfigManager;
import org.minibus.aqa.core.handlers.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestRunner;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.minibus.aqa.core.env.Device.getDriver;


@Listeners(TestInterceptor.class)
public abstract class BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
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

        String env = ConfigManager.getGeneralConfig().environment();
        RestAssured.baseURI = AppEnvironment.STAGE.toString().equals(env) ? "https://minibus-app.herokuapp.com/" : "http://localhost:3000";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, ITestResult iTestResult) {
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

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method, ITestResult iTestResult) {
        getDriver().closeApp();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        Device.quit();

        if (AppiumLocalManager.isRunning()) {
            AppiumLocalManager.stop();
        }
    }

    private boolean isAppOpened(String appPackage, int timeout) {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(timeout);
        LOGGER.info(String.format("Wait for the app '%s' to be opened (timeout=%s)", appPackage, timeout));
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
