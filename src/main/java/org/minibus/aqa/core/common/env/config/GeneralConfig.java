package org.minibus.aqa.core.common.env.config;

import org.aeonbits.owner.Config;
import org.minibus.aqa.Constants;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:general.properties"})
public interface GeneralConfig extends Config {

    /**
     * Page Object related
     */
    @DefaultValue("30")
    @Key("screenTimeout")
    int screenTimeout();

    @DefaultValue("9")
    @Key("elementTimeout")
    int elementTimeout();

    /**
     * OS related
     */
    @DefaultValue(Constants.NOT_ASSIGNED)
    @Key("os.name")
    String systemName();

    @DefaultValue(Constants.NOT_ASSIGNED)
    @Key("os.arch")
    String systemArch();

    @DefaultValue(Constants.NOT_ASSIGNED)
    @Key("os.version")
    String systemVersion();

    @DefaultValue(Constants.NOT_ASSIGNED)
    @Key("user.name")
    String userName();
}
