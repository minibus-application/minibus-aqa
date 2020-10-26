package org.minibus.aqa.tests.ui;

import org.assertj.core.api.Assertions;
import org.minibus.aqa.core.common.cli.AdbCommand;
import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.core.common.env.Device;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.tests.BaseTest;
import org.testng.annotations.Test;


public class DebugTest extends BaseTest {

    @Test
    public void testDebug() {
        ScheduleScreen scheduleScreen = new ScheduleScreen(Device.getDriver());
        Assertions.assertThat(scheduleScreen.isOpened()).isTrue();

        LOGGER.info("Taking screenshot...");
        AdbCommandExecutor.takeScreenshot(Device.getUdid());

        LOGGER.info("Taking screenshot...");
        AdbCommandExecutor.takeScreenshot();

        LOGGER.info("Get devices count...");
        AdbCommandExecutor.getDevicesCount();

        LOGGER.info("Get devices...");
        LOGGER.info(AdbCommandExecutor.getDevices(", "));

        LOGGER.info("Get connected devices...");
        LOGGER.info(AdbCommandExecutor.getDevices(AdbCommand.DeviceState.ONLINE, ", "));

        LOGGER.info("Is device connected...");
        LOGGER.info(String.valueOf(AdbCommandExecutor.isDeviceConnected(Device.getUdid())));

        LOGGER.info("Get device model...");
        LOGGER.info(AdbCommandExecutor.getDeviceInfo(Device.getUdid(), AdbCommand.DeviceInfo.MODEL));

        LOGGER.info("Get device info...");
        LOGGER.info(AdbCommandExecutor.getDeviceInfo(Device.getUdid()));

        LOGGER.info("Get device version...");
        LOGGER.info(AdbCommandExecutor.getDeviceVersion(Device.getUdid()));
    }
}
