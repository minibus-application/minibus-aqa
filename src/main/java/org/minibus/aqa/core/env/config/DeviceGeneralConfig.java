package org.minibus.aqa.core.env.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(org.aeonbits.owner.Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:general.properties"})
public interface DeviceGeneralConfig extends Config {

    /**
     * Device related
     */
    @DefaultValue("UiAutomator2")
    @Key("engine")
    String engine();

    @DefaultValue("Android")
    @Key("platform")
    String platform();

    @DefaultValue("false")
    @Key("emulated")
    boolean emulated();

    /**
     * App related
     */
    @DefaultValue("apps/app-debug.apk")
    @Key("app.path")
    String appPath();

    @DefaultValue("true")
    @Key("app.fullReset")
    boolean fullReset();

    @DefaultValue("false")
    @Key("app.noReset")
    boolean noReset();

    @DefaultValue("org.minibus.app.org.minibus.aqa.ui.main.MainActivity")
    @Key("app.activity")
    String appActivity();

    @DefaultValue("org.minibus.app.org.minibus.aqa.ui")
    @Key("app.package")
    String appPackage();

    @DefaultValue("60")
    @Key("app.installTimeout")
    int installTimeout();

    @DefaultValue("true")
    @Key("app.grantPermissions")
    boolean grantPermissions();

    @DefaultValue("false")
    @Key("app.autoLaunch")
    boolean autoLaunch();
}
