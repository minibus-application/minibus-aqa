package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.minibus.aqa.core.helpers.RandomGenerator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Device {

    private static AndroidDriver driver;
    private DeviceConfig config;
    private DesiredCapabilities capabilities;

    public Device(DeviceConfig config) {
        this.config = config;

        capabilities = new DesiredCapabilities();

        if (config.isEmulated()) {
            capabilities.setCapability(AndroidMobileCapabilityType.AVD, config.getDeviceName());
            capabilities.setCapability(AndroidMobileCapabilityType.AVD_LAUNCH_TIMEOUT, config.getEmulatorLaunchTimeout());
            capabilities.setCapability(AndroidMobileCapabilityType.AVD_READY_TIMEOUT, config.getEmulatorReadyTimeout());
            capabilities.setCapability(AndroidMobileCapabilityType.AVD_ARGS, config.getEmulatorArgsAsString());
        } else {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.getDeviceName());
        }

        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, config.getNewCommandTimeout());
        capabilities.setCapability(MobileCapabilityType.EVENT_TIMINGS, config.enableEventTimings());
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, config.clearGeneratedFiles());

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, config.getDevicePlatformVersion());
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, config.getDevicePlatform());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getDeviceEngine());
        capabilities.setCapability(MobileCapabilityType.APP, config.getAbsoluteAppPath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, config.getAppPackage());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, config.getAppActivity());
    }

    public AndroidDriver initDriver() {
        return initDriver(Environment.getInstance().getAppiumConfig().getAppiumUrl(), capabilities);
    }

    public AndroidDriver initDriver(URL serverUrl, DesiredCapabilities capabilities) {
        driver = new AndroidDriver(serverUrl, capabilities);
        return driver;
    }

    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    public static AndroidDriver getDriver() {
        return driver;
    }

    public DeviceConfig getConfig() {
        return config;
    }

    public int getAdbPort() {
        return config.getAdbPort();
    }

    public String getAdbHost() {
        return config.getAdbHost();
    }

    public boolean isEmulated() {
        return config.isEmulated();
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
