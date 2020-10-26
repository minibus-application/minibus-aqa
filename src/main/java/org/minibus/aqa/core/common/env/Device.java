package org.minibus.aqa.core.common.env;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.config.AppiumConfig;
import org.minibus.aqa.core.common.env.config.ConfigManager;
import org.minibus.aqa.core.common.env.config.DeviceConfig;
import org.minibus.aqa.core.common.env.config.DeviceGeneralConfig;
import org.minibus.aqa.core.common.handlers.DeviceType;
import org.minibus.aqa.core.helpers.RandomHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.ENABLE_PERFORMANCE_LOGGING;

public class Device {
    private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);
    private static ThreadLocal<AppiumDriver<MobileElement>> driver = new ThreadLocal<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (getDriver() != null) {
                try {
                    LOGGER.info(String.format("Terminating driver session: %s", getDriver().getSessionId().toString()));
                    getDriver().quit();
                } catch (Exception ignore) {
                    // ignore
                }
            }
        }));
    }

    private static DesiredCapabilities resolveCapabilities(DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        AppiumConfig appiumConfig = ConfigManager.getAppiumConfig();
        DesiredCapabilities capabilities = new DesiredCapabilities();

//        String deviceVersion, deviceInfo, deviceName;
//        String deviceUdid = config.emulated() ? AdbCommandExecutor.getConnectedDeviceSerial(deviceConfig.avdName()) : deviceConfig.udid();

//        LOGGER.info("The following desired capabilities has passed:");
//        Arrays.stream(config.getClass().getMethods()).forEach(method -> {
//            LOGGER.info(String.format("%s:%s", method.getName(), method.getDefaultValue()));
//
//        });
//        assert deviceUdid != null;
//
//        LOGGER.info(String.format("Finding out whether the device with udid=%s is connected or not", deviceUdid));
//
//        if (AdbCommandExecutor.isDeviceConnected(deviceUdid)) {
//            deviceVersion = AdbCommandExecutor.getDeviceVersion(deviceUdid);
//            deviceInfo = AdbCommandExecutor.getDeviceInfo(deviceUdid);
//            deviceName = AdbCommandExecutor.getDeviceInfo(deviceUdid, AdbCommand.DeviceInfo.MODEL);
//
//            if (deviceName.isEmpty()) {
//                deviceName = config.emulated() ? DeviceType.EMULATOR.toString() : DeviceType.PHYSICAL.toString();
//            }
//
//            LOGGER.info(String.format("Device found: udid:%s version:%s %s", deviceUdid, deviceVersion, deviceInfo));
//        } else {
//            throw new UnsupportedCommandException(String.format("Device with '%s' serial isn't in the list of connected devices", deviceUdid));
//        }

        if (config.emulated()) {
            capabilities.setCapability(AVD_LAUNCH_TIMEOUT, deviceConfig.avdLaunchTimeout());
            capabilities.setCapability(AVD_READY_TIMEOUT, deviceConfig.avdReadyTimeout());
            capabilities.setCapability(AVD_ARGS, deviceConfig.avdArgs());
            capabilities.setCapability(AVD, deviceConfig.avdName());
            capabilities.setCapability(IS_HEADLESS, deviceConfig.avdHeadless());
            capabilities.setCapability(ORIENTATION, deviceConfig.avdOrientation());
        } else {
            capabilities.setCapability(UDID, deviceConfig.udid());
        }

        capabilities.setCapability(NEW_COMMAND_TIMEOUT, appiumConfig.commandTimeout());
        capabilities.setCapability(EVENT_TIMINGS, appiumConfig.eventTimings());
        capabilities.setCapability(ENABLE_PERFORMANCE_LOGGING, appiumConfig.logPerformance());
        capabilities.setCapability(FULL_RESET, config.fullReset());
        capabilities.setCapability(NO_RESET, config.noReset());

        capabilities.setCapability(DEVICE_NAME, config.emulated() ? DeviceType.EMULATOR.toString() : DeviceType.PHYSICAL.toString());
        capabilities.setCapability(PLATFORM_NAME, config.platform());
        capabilities.setCapability(AUTOMATION_NAME, config.engine());
        capabilities.setCapability(APP, resolveAppPath(config.appPath()));

        return capabilities;
    }

    private static String resolveAppPath(String path) {
        if (new File(path).exists()) {
            return path;
        }
        // assuming path points to resource file
        if (path.contains(File.separator)) {
            path = StringUtils.substringAfterLast(path, File.separator);
        }
        File file = new File("src/test/resources/apps/" + path);
        return file.getAbsolutePath();
    }

    public static synchronized AppiumDriver<MobileElement> create(String serverUrl, DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        try {
            return create(new URL(serverUrl), deviceConfig, config);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized AppiumDriver<MobileElement> create(URL serverUrl, DeviceConfig deviceConfig, DeviceGeneralConfig config) {
        LOGGER.info(String.format("Starting new driver session: %s", serverUrl));

        AppiumDriver<MobileElement> initializedDriver = new AppiumDriver<>(serverUrl, resolveCapabilities(deviceConfig, config));
        driver.set(initializedDriver);

        LOGGER.info(String.format("Driver session has started: %s", initializedDriver.getSessionId().toString()));
        return getDriver();
    }

    public static AppiumDriver<MobileElement> getDriver() {
        return driver.get();
    }

    public static boolean quit() {
        if (driver.get() != null) {
            if (driver.get().getSessionId() != null) {
                LOGGER.info(String.format("Closing driver session: %s", driver.get().getSessionId().toString()));
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

    public static synchronized File getScreenshot() {
        try {
            File screenshot = getDriver().getScreenshotAs(OutputType.FILE);
            File localFile = new File(Constants.PROJECT_TEMP_FOLDER + RandomHelper.temp() + "." + Constants.PNG);
            FileUtils.copyFile(screenshot, localFile);
            return localFile;
        } catch (WebDriverException | IOException e) {
            String udid = (String) getDriver().getSessionDetails().get("deviceUDID");
            String deviceFilePath = AdbCommandExecutor.takeScreenshot(udid).getAbsolutePath();
            return AdbCommandExecutor.pull(deviceFilePath);
        } catch (NullPointerException e) {
            throw new RuntimeException("An error while taking the screenshot. The driver is null or hasn't initialised yet");
        }
    }
}
