package org.minibus.aqa.main.domain.screens;

import org.minibus.aqa.main.core.env.config.ConfigManager;

public interface Loadable {

    boolean waitForLoading(int timeoutSec);

    boolean isLoading();

    default boolean waitForLoading() {
        return waitForLoading(ConfigManager.getGeneralConfig().screenTimeout());
    }
}
