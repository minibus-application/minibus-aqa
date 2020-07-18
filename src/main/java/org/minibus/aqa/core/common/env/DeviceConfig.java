package org.minibus.aqa.core.common.env;

import io.appium.java_client.remote.MobilePlatform;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.helpers.ResourceHelper;

import java.util.Properties;

public class DeviceConfig implements Config {

    private Properties config;

    private String name;
    private String udid;
    private String engine;
    private String platform;
    private String platformVersion;
    private boolean isEmulated;
    private boolean toReset;
    private boolean toUninstallApp;
    private boolean grantPermissions;
    private boolean autoLaunchApp;
    private String appFileName;
    private String appPath;
    private String appActivity;
    private String appPackage;
    private String adbHost;
    private int adbPort;
    private int avdLaunchTimeout;
    private int avdReadyTimeout;
    private boolean avdIsHeadless;
    private String avdArgs;

    private int newCommandTimeout;
    private boolean enableEventTimings;
    private boolean clearGeneratedFiles;

    public DeviceConfig() {
        config = ResourceHelper.getInstance().loadProperties(DIR, "device");

        isEmulated = Boolean.valueOf(initProperty(Key.DEVICE_EMULATED, true));
        engine = initProperty(Key.DEVICE_ENGINE, "UiAutomator2");
        platform = initProperty(Key.DEVICE_PLATFORM, MobilePlatform.ANDROID).toLowerCase();
        platformVersion = initProperty(Key.DEVICE_PLATFORM_VERSION);

        // physical android device serial number or android emulator name given while creating
        // if not defined, appium will use platform version as the point to find plugged device
        udid = initProperty(Key.DEVICE_UDID, true);
        // on android this property is currently ignored, thought it remains required for appium
        // so, this property only matters in sense of running on ios simulators
        name = initProperty(Key.DEVICE_NAME, platform);

        if (isEmulated) {
            avdLaunchTimeout = Integer.valueOf(initProperty(Key.AVD_LAUNCH_TIMEOUT, "60"));
            avdReadyTimeout = Integer.valueOf(initProperty(Key.AVD_READY_TIMEOUT, "60"));
            avdIsHeadless = Boolean.valueOf(initProperty(Key.AVD_HEADLESS, "false"));
            avdArgs = initProperty(Key.AVD_ARGS, "false");
        }

        grantPermissions = Boolean.parseBoolean(initProperty(Key.DEVICE_GRANT_PERMISSIONS, "true"));
        autoLaunchApp = Boolean.parseBoolean(initProperty(Key.APP_AUTO_LAUNCH, "false"));
        toReset = Boolean.valueOf(initProperty(Key.APP_RESET, "false"));
        toUninstallApp = Boolean.valueOf(initProperty(Key.APP_UNINSTALL, "false"));

        appFileName = initProperty(Key.APP_FILE_NAME);
        appPath = ResourceHelper.getInstance().getResourceFile("apps", appFileName).getAbsolutePath();
        appActivity = initProperty(Key.APP_ACTIVITY);
        appPackage = initProperty(Key.APP_PACKAGE);

        adbHost = initProperty(Key.ADB_HOST, "localhost");
        adbPort = Integer.valueOf(initProperty(Key.ADB_PORT, "5037"));

        // client related capabilities
        newCommandTimeout = Integer.valueOf(initProperty(Key.CLIENT_NEW_COMMAND_TIMEOUT, "60"));
        enableEventTimings = Boolean.valueOf(initProperty(Key.CLIENT_EVENT_TIMINGS, "false"));
        clearGeneratedFiles = Boolean.valueOf(initProperty(Key.CLIENT_CLEAR_GENERATED_FILES, "true"));
    }

    @Override
    public Properties getConfig() {
        return config;
    }

    public String getAppPath() {
        return appPath;
    }

    public String getName() {
        return name;
    }

    public String getEngine() {
        return engine;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getUdid() {
        return udid;
    }

    public boolean isGrantPermissions() {
        return grantPermissions;
    }

    public boolean isAutoLaunchApp() {
        return autoLaunchApp;
    }

    public boolean isEmulated() {
        return isEmulated;
    }

    public boolean toReset() {
        return toReset;
    }

    public boolean toUninstallApp() {
        return toUninstallApp;
    }

    public String getAppFileName() {
        return appFileName;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAdbHost() {
        return adbHost;
    }

    public int getAdbPort() {
        return adbPort;
    }

    public int getAvdLaunchTimeout() {
        return avdLaunchTimeout;
    }

    public int getAvdReadyTimeout() {
        return avdReadyTimeout;
    }

    public boolean isHeadless() {
        return avdIsHeadless;
    }

    public String getAvdArgs() {
        return avdArgs;
    }

    public int getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public boolean enableEventTimings() {
        return enableEventTimings;
    }

    public boolean clearGeneratedFiles() {
        return clearGeneratedFiles;
    }

    @Override
    public String toString() {
        return stringify();
    }

    public enum Key {
        DEVICE_NAME("device.name"),
        DEVICE_UDID("device.udid"),
        DEVICE_GRANT_PERMISSIONS("device.grantPermissions"),
        DEVICE_ENGINE("device.engine"),
        DEVICE_PLATFORM("device.platform"),
        DEVICE_PLATFORM_VERSION("device.platform.ver"),
        DEVICE_EMULATED("device.emulated"),
        APP_RESET("device.app.reset"),
        APP_UNINSTALL("device.app.uninstall"),
        APP_FILE_NAME("device.app.file"),
        APP_ACTIVITY("device.app.activity"),
        APP_PACKAGE("device.app.package"),
        APP_AUTO_LAUNCH("device.app.autoLaunch"),
        ADB_HOST("device.adb.host"),
        ADB_PORT("device.adb.port"),
        AVD_LAUNCH_TIMEOUT("device.avd.launchTimeout"),
        AVD_READY_TIMEOUT("device.avd.readyTimeout"),
        AVD_HEADLESS("device.avd.headless"),
        AVD_ARGS("device.avd.args"),
        CLIENT_NEW_COMMAND_TIMEOUT("client.newCommandTimeout"),
        CLIENT_EVENT_TIMINGS("client.eventTimings"),
        CLIENT_CLEAR_GENERATED_FILES("client.clearGeneratedFiles");

        private final String key;

        Key(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
