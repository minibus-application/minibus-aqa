package org.minibus.aqa.core.common.env.device;

import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public abstract class BaseDevice<T extends BaseDevice<T>> implements Device {

    private AndroidDriver driver;
    private DeviceConfig config;
    private DesiredCapabilities capabilities;

    protected BaseDevice(DeviceConfig config) {
        this.config = config;

        capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, config.getNewCommandTimeout());
        capabilities.setCapability(MobileCapabilityType.EVENT_TIMINGS, config.enableEventTimings());
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, config.clearGeneratedFiles());

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, config.getDevicePlatformVersion());
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, config.getDevicePlatform());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getDeviceEngine());
        capabilities.setCapability(MobileCapabilityType.APP, config.getAbsoluteAppPath());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, config.getAppPackage());
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, config.getAppActivity());
    }

    public AndroidDriver initDriver() {
        return initDriver(Environment.getInstance().getAppiumConfig().getAppiumUrl(), capabilities);
    }

    @Override
    public AndroidDriver initDriver(URL serverUrl, DesiredCapabilities capabilities) {
        driver = new AndroidDriver(serverUrl, capabilities);
        return driver;
    }

    @Override
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public AndroidDriver getDriver() {
        return driver;
    }

    @Override
    public DeviceConfig getConfig() {
        return config;
    }

    public int getAdbPort() {
        return config.getAdbPort();
    }

    public String getAdbHost() {
        return config.getAdbHost();
    }

    public boolean isEmulated() {
        return config.isEmulated();
    }

    // TODO

    public AndroidBatteryInfo.BatteryState getBatteryState() {
        return getDriver().getBatteryInfo().getState();
    }

    @Override
    public void closeApp() {

    }

    @Override
    public void sendAppBackground() {

    }

    @Override
    public void resetApp() {

    }

    @Override
    public void removeApp() {

    }

    @Override
    public void terminateApp() {

    }

    @Override
    public void getAppState() {

    }

    @Override
    public void getCurrentActivity() {

    }

    @Override
    public void getCurrentPackage() {

    }

    @Override
    public void startActivity() {

    }

    @Override
    public void setClipboard() {

    }

    @Override
    public void getClipboard() {

    }

    @Override
    public void pushFile() {

    }

    @Override
    public void pullFile() {

    }

    @Override
    public void pullFolder() {

    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    @Override
    public void isLocked() {

    }

    @Override
    public void rotate() {

    }

    @Override
    public void sendKeycode() {

    }

    @Override
    public void sendLongKeycode() {

    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void isKeyboardShown() {

    }

    @Override
    public void toggleAirplaneMode() {

    }

    @Override
    public void toggleData() {

    }

    @Override
    public void toggleWifi() {

    }

    @Override
    public void toggleLocationServices() {

    }

    @Override
    public void startScreenRecording() {

    }

    @Override
    public void stopScreenRecording() {

    }
}
