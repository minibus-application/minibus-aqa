package org.minibus.aqa.core.env.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:device.properties"})
public interface DeviceConfig extends Config {

    /**
     * Physical device related
     */
    @Key("udid")
    String udid();

    /**
     * Emulated device related
     */
    @DefaultValue("60")
    @Key("avd.launchTimeout")
    int avdLaunchTimeout();

    @DefaultValue("60")
    @Key("avd.readyTimeout")
    int avdReadyTimeout();

    @Key("avd.name")
    String avdName();

    @Key("avd.args")
    String avdArgs();

    @DefaultValue("PORTRAIT")
    @Key("avd.orientation")
    String avdOrientation();
}
