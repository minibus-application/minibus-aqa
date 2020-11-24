package org.minibus.aqa.main.core.env;

import io.appium.java_client.MobileElement;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.config.AppiumConfig;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.env.config.DeviceConfig;
import org.minibus.aqa.main.core.env.config.DeviceGeneralConfig;
import org.minibus.aqa.main.core.helpers.ResourceHelper;
import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.cli.AdbCommandExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.*;

public class Device {
    private static final Logger LOGGER = LogManager.getLogger(Device.class);
    private static ThreadLocal<AndroidDriver<MobileElement>> driver = new ThreadLocal<>();
    private static List<Device> devices = new ArrayList<>();

    private static DesiredCapabilities resolveCapabilities(DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        AppiumConfig appiumConfig = ConfigManager.getAppiumConfig();
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (config.emulated()) {
            capabilities.setCapability(AVD_LAUNCH_TIMEOUT, deviceConfig.avdLaunchTimeout());
            capabilities.setCapability(AVD_READY_TIMEOUT, deviceConfig.avdReadyTimeout());
            capabilities.setCapability(AVD_ARGS, deviceConfig.avdArgs());
            capabilities.setCapability(AVD, deviceConfig.avdName());
            capabilities.setCapability(ORIENTATION, deviceConfig.avdOrientation());
        } else {
            capabilities.setCapability(UDID, deviceConfig.udid());
        }

        capabilities.setCapability(NEW_COMMAND_TIMEOUT, appiumConfig.commandTimeout());
        capabilities.setCapability(EVENT_TIMINGS, appiumConfig.eventTimings());
        capabilities.setCapability(ENABLE_PERFORMANCE_LOGGING, appiumConfig.logPerformance());
        capabilities.setCapability(FULL_RESET, config.fullReset());
        capabilities.setCapability(NO_RESET, config.noReset());
        capabilities.setCapability("autoLaunch", config.autoLaunch());

        capabilities.setCapability(DEVICE_NAME, config.emulated() ? DeviceType.EMULATOR.toString() : DeviceType.PHYSICAL.toString());
        capabilities.setCapability(PLATFORM_NAME, config.platform());
        capabilities.setCapability(AUTOMATION_NAME, config.engine());
        capabilities.setCapability(APP, resolveAppPath(config.appPath()));

        return capabilities;
    }

    private static String resolveAppPath(String path) {
        if (new File(path).exists()) {
            return path;
        } else {
            if (path.contains(File.separator)) {
                String[] pathParts = path.split(File.separator);
                String[] precedingDirs = Arrays.copyOf(pathParts, pathParts.length - 1);
                path = ResourceHelper.getInstance().getResourcePath(pathParts[pathParts.length - 1], precedingDirs).toString();
            } else {
                path = ResourceHelper.getInstance().getResourcePath(path).toString();
            }
        }

        return path;
    }

    public synchronized AndroidDriver<MobileElement> create(String serverUrl, DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        try {
            return create(new URL(serverUrl), deviceConfig, config);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized AndroidDriver<MobileElement> create(URL serverUrl, DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        LOGGER.info("Starting driver session: {}", serverUrl);

        AndroidDriver<MobileElement> initializedDriver = new AndroidDriver<>(serverUrl, resolveCapabilities(deviceConfig, config));
        initializedDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        initializedDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 50);
        initializedDriver.setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 50);

        driver.set(initializedDriver);
        devices.add(this);

        LOGGER.info("Driver session id: {}", initializedDriver.getSessionId().toString());
        LOGGER.info("Appium desired capabilities: {}", initializedDriver.getCapabilities().asMap());
        return getDriver();
    }

    public static AndroidDriver<MobileElement> getDriver() {
        return driver.get();
    }

    public boolean quit() {
        if (driver.get() != null) {
            if (driver.get().getSessionId() != null) {
                LOGGER.info("Closing driver session: {}", driver.get().getSessionId().toString());

                driver.get().closeApp();
                driver.get().quit();
                driver.remove();
            }
            return true;
        }
        return false;
    }

    public static String getUdid() {
        try {
            return (String) getDriver().getSessionDetails().get("deviceUDID");
        } catch (NullPointerException e) {
            return StringUtils.EMPTY;
        }
    }

    public static synchronized byte[] getScreenshot() {
        byte[] imageBytes = new byte[0];

        try {
            imageBytes = getDriver().getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            String localFileDestination = Constants.PROJECT_TEMP_FOLDER;
            String udid = (String) getDriver().getSessionDetails().get("deviceUDID");
            String deviceFilePath = AdbCommandExecutor.takeScreenshot(udid).getAbsolutePath();
            File pulledFile = AdbCommandExecutor.pull(deviceFilePath, localFileDestination);

            try {
                imageBytes = FileUtils.readFileToByteArray(pulledFile);
            } catch (Exception e1) {
                LOGGER.error("Exception while reading the screenshot file to bytes: {}", pulledFile.getAbsolutePath());
            }
            return imageBytes;
        } catch (NullPointerException e) {
            LOGGER.error("Exception while taking the screenshot. The driver is null or hasn't initialised yet");
        }

        return imageBytes;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (getDriver() != null) {
                try {
                    LOGGER.info("Shutdown driver session: {}", getDriver().getSessionId().toString());
                    devices.forEach(Device::quit);
                } catch (Exception e) {
                    LOGGER.warn("Can not shutdown driver session:\n{}", e.getMessage());
                }
            }
        }));
    }
}
