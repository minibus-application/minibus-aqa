package org.minibus.aqa.core.common.env.device;

import org.minibus.aqa.core.common.env.DeviceConfig;

public class DeviceFactory {

    public static BaseDevice create(DeviceConfig deviceConfig) {
        if (deviceConfig.isEmulated()) {
            return new EmulatedDevice(deviceConfig);
        } else {
            return new PhysicalDevice(deviceConfig);
        }
    }
}
