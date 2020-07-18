package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.minibus.aqa.core.helpers.RandomGenerator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.*;

public class Device {

    private DesiredCapabilities capabilities;
    private static AndroidDriver driver;
    private static String udid;
    private static String version;

    public Device(DeviceConfig config) {
        this.capabilities = new DesiredCapabilities();

        try {
            if (config.isDefined(DeviceConfig.Key.DEVICE_UDID)) {
                String passedUdid = config.getUdid();

                if (AdbCommandExecutor.isDeviceConnected(passedUdid)) {
                    udid = config.getUdid();

                    capabilities.setCapability(config.isEmulated() ? AVD : UDID, udid);

                    version = !config.isDefined(DeviceConfig.Key.DEVICE_PLATFORM_VERSION)
                            ? AdbCommandExecutor.getDeviceVersion(passedUdid)
                            : config.getPlatformVersion();

                    capabilities.setCapability(PLATFORM_VERSION, version);
                } else {
                    throw new UnsupportedCommandException(String.format("'%s' device was not found in the list of connected devices", passedUdid));
                }
            } else {
                throw new UnsupportedCommandException(String.format("%s is undefined",
                        DeviceConfig.Key.DEVICE_UDID.toString()));
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        if (config.isEmulated()) {
            capabilities.setCapability(AVD_LAUNCH_TIMEOUT, config.getAvdLaunchTimeout());
            capabilities.setCapability(AVD_READY_TIMEOUT, config.getAvdReadyTimeout());
            capabilities.setCapability(AVD_ARGS, config.getAvdArgs());
        }

        capabilities.setCapability(NEW_COMMAND_TIMEOUT, config.getNewCommandTimeout());
        capabilities.setCapability(EVENT_TIMINGS, config.enableEventTimings());
        capabilities.setCapability(CLEAR_SYSTEM_FILES, config.clearGeneratedFiles());
        capabilities.setCapability(FULL_RESET, config.toUninstallApp());
        capabilities.setCapability(NO_RESET, !config.toReset());
        capabilities.setCapability(DEVICE_NAME, config.getName());
        capabilities.setCapability(PLATFORM_NAME, config.getPlatform());
        capabilities.setCapability(AUTOMATION_NAME, config.getEngine());
        capabilities.setCapability(APP, config.getAppPath());
        capabilities.setCapability(APP_PACKAGE, config.getAppPackage());
        capabilities.setCapability(APP_ACTIVITY, config.getAppActivity());
    }

    public AndroidDriver initDriver() {
        return initDriver(Environment.getInstance().getAppiumConfig().getAppiumUrl(), capabilities);
    }

    public AndroidDriver initDriver(URL serverUrl, DesiredCapabilities capabilities) {
        driver = new AndroidDriver(serverUrl, capabilities);
        return driver;
    }

    public static AndroidDriver getDriver() {
        return driver;
    }

    public static String getUdid() {
        try {
            return Objects.requireNonNullElse(udid, (String) driver.getSessionDetails().get("deviceUDID"));
        } catch (NullPointerException e) {
            return StringUtils.EMPTY;
        }
    }

    public static String getVersion() {
        return Objects.requireNonNullElse(version, StringUtils.EMPTY);
    }

    public static synchronized File getScreenshot() {
        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            File localFile = new File(Constants.PROJECT_TEMP_FOLDER + RandomGenerator.temp());
            FileUtils.copyFile(screenshot, localFile);
            return localFile;
        } catch (WebDriverException | IOException e) {
            String udid = (String) driver.getSessionDetails().get("deviceUDID");
            String deviceFilePath = AdbCommandExecutor.takeScreenshot(udid).getAbsolutePath();
            return AdbCommandExecutor.pull(deviceFilePath);
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignore) {
                    // ignore
                }
            }
        }));
    }
}
