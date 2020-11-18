package org.minibus.aqa.main.core.env.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:api/service.properties"})
public interface ApiConfig extends Config  {

    @DefaultValue("30")
    @Key("api.timeout")
    int timeout();

    @Key("api.baseUrl.stage")
    String stageBaseUrl();

    @Key("api.baseUrl.dev")
    String debBaseUrl();
}
