package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.remote.MobileCapabilityType;
import org.minibus.aqa.core.common.env.DeviceConfig;


public class PhysicalDevice extends BaseDevice<PhysicalDevice> {

    public PhysicalDevice(DeviceConfig config) {
        super(config);
        getCapabilities().setCapability(MobileCapabilityType.DEVICE_NAME, config.getDeviceName());

        super.initDriver();
    }
}
