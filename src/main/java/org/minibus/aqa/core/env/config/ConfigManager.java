package org.minibus.aqa.core.env.config;

import org.aeonbits.owner.ConfigCache;

import java.io.File;
import java.util.Objects;

public class ConfigManager {

    private ConfigManager() {
    }

    public static DeviceConfig getDeviceConfig() {
        return ConfigCache.getOrCreate(DeviceConfig.class);
    }

    public static DeviceGeneralConfig getDeviceGeneralConfig() {
        return ConfigCache.getOrCreate(DeviceGeneralConfig.class);
    }

    public static GeneralConfig getGeneralConfig() {
        return ConfigCache.getOrCreate(GeneralConfig.class);
    }

    public static AppiumConfig getAppiumConfig() {
        return ConfigCache.getOrCreate(AppiumConfig.class);
    }

    public static AdbConfig getAdbConfig() {
        return ConfigCache.getOrCreate(AdbConfig.class);
    }

    public static File getJsonSchema(String fileName) {
        return loadResourceFile("schemas/" + fileName);
    }

    private static File loadResourceFile(String relativePath) {
        ClassLoader classLoader = ConfigManager.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(relativePath)).getFile());
    }
}
