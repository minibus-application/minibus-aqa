package org.minibus.aqa.tests.ui;

import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.device.Device;
import org.minibus.aqa.core.common.handlers.TestLogger;
import org.minibus.aqa.tests.BaseTest;
import org.testng.annotations.Test;


public class DebugTest extends BaseTest {

    @Test
    public void testDebug() {
        TestLogger.get().info(AdbCommandExecutor.getDevices(", "));
        TestLogger.get().info(AdbCommandExecutor.getDeviceVersion(Device.getUdid()));
        TestLogger.get().info(AdbCommandExecutor.getDeviceState(Device.getUdid()).toString());
    }
}
