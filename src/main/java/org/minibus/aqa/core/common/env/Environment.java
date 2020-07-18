package org.minibus.aqa.core.common.env;

import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.env.device.Device;
import org.minibus.aqa.core.helpers.ResourceHelper;

public class Environment {

    private static Environment instance;
    private AppiumConfig appiumConfig;
    // private DomainConfig domainConfig;
    private DeviceConfig deviceConfig;

    private String systemArchitecture;
    private String systemName;
    private String systemVersion;
    private String systemUser;

    private Environment() {
        ResourceHelper.getInstance().initResources();

        systemName = System.getProperty("os.name", Constants.NOT_ASSIGNED);
        systemArchitecture = System.getProperty("os.arch", Constants.NOT_ASSIGNED);
        systemVersion = System.getProperty("os.version", Constants.NOT_ASSIGNED);
        systemUser = System.getProperty("user.name", Constants.NOT_ASSIGNED);

        // domainConfig = new DomainConfig();
        appiumConfig = new AppiumConfig();
        deviceConfig = new DeviceConfig();
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    public AppiumConfig getAppiumConfig() {
        return appiumConfig;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public String getSystemArchitecture() {
        return systemArchitecture;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public String getSystemUser() {
        return systemUser;
    }

    public boolean isUnixLike() {
        return !systemName.toLowerCase().startsWith("windows");
    }
}
