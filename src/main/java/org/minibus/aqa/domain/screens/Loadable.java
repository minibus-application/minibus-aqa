package org.minibus.aqa.domain.screens;

import org.minibus.aqa.core.env.config.ConfigManager;

public interface Loadable {

    boolean waitForLoading(int timeoutSec);

    boolean isLoading();

    default boolean waitForLoading() {
        return waitForLoading(ConfigManager.getGeneralConfig().screenTimeout());
    }
}
