package org.minibus.aqa.core.env.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Reloadable;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:appium.properties"})
public interface AppiumConfig extends Reloadable {

    @DefaultValue("http://${appium.host}:${appium.port}/wd/hub")
    @Key("appium.url")
    String url();

    @Key("appium.host")
    String host();

    @Key("appium.port")
    String port();

    @DefaultValue("15000") // for debugging in appium desktop
    @Key("appium.commandTimeout")
    String commandTimeout();

    @DefaultValue("false")
    @Key("appium.eventTimings")
    boolean eventTimings();

    @DefaultValue("false")
    @Key("appium.performanceLogging")
    boolean logPerformance();
}
