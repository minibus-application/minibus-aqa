package org.minibus.aqa.core.common.env;

import org.minibus.aqa.Constants;
import org.minibus.aqa.core.helpers.ResourceHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DeviceConfig implements Config {

    private Properties config;
    private Properties commonConfig;
    private Properties mergedConfig;

    private String deviceName;
    private String deviceEngine;
    private String devicePlatform;
    private String devicePlatformVersion;
    private boolean isEmulated;
    private boolean toReset;
    private boolean toFullReset;
    private String appFileName;
    private String appActivity;
    private String appPackage;
    private String adbHost;
    private int adbPort;

    private int emulatorLaunchTimeout;
    private int emulatorReadyTimeout;
    private boolean emulatorIsHeadless;
    private List<String> emulatorArgs;

    private int newCommandTimeout;
    private boolean enableEventTimings;
    private boolean clearGeneratedFiles;

    public DeviceConfig(String fileName) {
        commonConfig = ResourceHelper.getInstance().loadProperties(DIR, "common_" + DIR);
        mergedConfig = new Properties(commonConfig);

        if (fileName != null) {
            mergedConfig.putAll(config = ResourceHelper.getInstance().loadProperties(DIR, fileName));
        }

        isEmulated = Boolean.valueOf(initProperty(Key.DEVICE_EMULATED, "false"));
        if (isEmulated) {
            emulatorLaunchTimeout = Integer.valueOf(initProperty(Key.AVD_LAUNCH_TIMEOUT, "60"));
            emulatorReadyTimeout = Integer.valueOf(initProperty(Key.AVD_READY_TIMEOUT, "60"));
            emulatorIsHeadless = Boolean.valueOf(initProperty(Key.AVD_HEADLESS, "false"));
            emulatorArgs = Arrays.asList(initProperty(Key.AVD_ARGS, "false").split(Constants.PROPERTIES_DELIMITER));
        }

        deviceName = initProperty(Key.DEVICE_NAME);
        deviceEngine = initProperty(Key.DEVICE_ENGINE);
        devicePlatform = initProperty(Key.DEVICE_PLATFORM);
        devicePlatformVersion = initProperty(Key.DEVICE_PLATFORM_VERSION);

        toReset = Boolean.valueOf(initProperty(Key.DEVICE_TO_RESET, "false"));
        toFullReset = Boolean.valueOf(initProperty(Key.DEVICE_TO_FULL_RESET, "false"));
        appFileName = initProperty(Key.APP_FILE_NAME);
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
        return mergedConfig;
    }

    public String getAbsoluteAppPath() {
        return ResourceHelper.getInstance().getResourceFile("apps", getAppFileName()).getAbsolutePath();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceEngine() {
        return deviceEngine;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public String getDevicePlatformVersion() {
        return devicePlatformVersion;
    }

    public boolean isEmulated() {
        return isEmulated;
    }

    public boolean toReset() {
        return toReset;
    }

    public boolean toFullReset() {
        return toFullReset;
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

    public int getEmulatorLaunchTimeout() {
        return emulatorLaunchTimeout;
    }

    public int getEmulatorReadyTimeout() {
        return emulatorReadyTimeout;
    }

    public boolean isHeadless() {
        return emulatorIsHeadless;
    }

    public List<String> getEmulatorArgs() {
        return emulatorArgs;
    }

    public String getEmulatorArgsAsString() {
        return getEmulatorArgs().stream()
                .reduce((argsString, arg) -> argsString + " " + arg)
                .get();
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
        return getClass().getSimpleName()
                + Constants.NEW_LINE
                + config.toString().replace(", ", Constants.NEW_LINE);
    }

    public enum Key {
        DEVICE_NAME("device.name"),
        DEVICE_ENGINE("device.engine"),
        DEVICE_PLATFORM("device.platform"),
        DEVICE_PLATFORM_VERSION("device.platform.ver"),
        DEVICE_EMULATED("device.emulated"),
        DEVICE_TO_RESET("device.reset"),
        DEVICE_TO_FULL_RESET("device.reset.full"),
        APP_FILE_NAME("device.app.file"),
        APP_ACTIVITY("device.app.activity"),
        APP_PACKAGE("device.app.package"),
        ADB_HOST("device.adb.host"),
        ADB_PORT("device.adb.port"),
        AVD_LAUNCH_TIMEOUT("device.emulated.launchTimeout"),
        AVD_READY_TIMEOUT("device.emulated.readyTimeout"),
        AVD_HEADLESS("device.emulated.headless"),
        AVD_ARGS("device.emulated.args"),
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
