package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.PowerACState;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import org.minibus.aqa.core.common.env.DeviceConfig;

public class EmulatedDevice extends BaseDevice<EmulatedDevice> {

    public EmulatedDevice(DeviceConfig config) {
        super(config);
        getCapabilities().setCapability(AndroidMobileCapabilityType.AVD, config.getDeviceName());
        getCapabilities().setCapability(AndroidMobileCapabilityType.AVD_LAUNCH_TIMEOUT, config.getEmulatorLaunchTimeout());
        getCapabilities().setCapability(AndroidMobileCapabilityType.AVD_READY_TIMEOUT, config.getEmulatorReadyTimeout());
        getCapabilities().setCapability(AndroidMobileCapabilityType.AVD_ARGS, config.getEmulatorArgsAsString());

        super.initDriver();
    }

    public boolean powerAc(boolean state) {
        getDriver().setPowerAC(state ? PowerACState.ON : PowerACState.OFF);
        return getBatteryState().equals(AndroidBatteryInfo.BatteryState.CHARGING);
    }

    public void powerCapacity(int batteryPercentage) {
        getDriver().setPowerCapacity(batteryPercentage);
    }

    public void setNetworkSpeed() {

    }
}
