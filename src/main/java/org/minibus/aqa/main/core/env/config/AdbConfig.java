package org.minibus.aqa.main.core.env.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:adb.properties"})
public interface AdbConfig extends Config {

    @DefaultValue("5037")
    @Key("adb.port")
    String adbPort();

    @DefaultValue("localhost")
    @Key("adb.host")
    String adbHost();

    @DefaultValue("50")
    @Key("adb.execTimeout")
    int adbExecTimeout();
}
