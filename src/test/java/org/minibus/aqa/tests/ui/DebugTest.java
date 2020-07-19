package org.minibus.aqa.tests.ui;

import org.minibus.aqa.core.common.cli.AdbCommand;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.device.Device;
import org.minibus.aqa.core.common.handlers.TestLogger;
import org.minibus.aqa.tests.BaseTest;
import org.testng.annotations.Test;


public class DebugTest extends BaseTest {

    @Test
    public void testDebug() {
        TestLogger.get().info("Taking screenshot...");
        AdbCommandExecutor.takeScreenshot(Device.getUdid());

        TestLogger.get().info("Taking screenshot...");
        AdbCommandExecutor.takeScreenshot();

        TestLogger.get().info("Get devices count...");
        AdbCommandExecutor.getDevicesCount();

        TestLogger.get().info("Get devices...");
        TestLogger.get().info(AdbCommandExecutor.getDevices(", "));

        TestLogger.get().info("Get connected devices...");
        TestLogger.get().info(AdbCommandExecutor.getDevices(AdbCommand.DeviceState.ONLINE, ", "));

        TestLogger.get().info("Is device connected...");
        TestLogger.get().info(String.valueOf(AdbCommandExecutor.isDeviceConnected(Device.getUdid())));

        TestLogger.get().info("Get device model...");
        TestLogger.get().info(AdbCommandExecutor.getDeviceInfo(Device.getUdid(), AdbCommand.DeviceInfo.MODEL));

        TestLogger.get().info("Get device info...");
        TestLogger.get().info(AdbCommandExecutor.getDeviceInfo(Device.getUdid()));

        TestLogger.get().info("Get device version...");
        TestLogger.get().info(AdbCommandExecutor.getDeviceVersion(Device.getUdid()));
    }
}
