package org.minibus.aqa.domain.screens;

import org.minibus.aqa.core.common.env.device.Device;

public abstract class BaseScreen implements Screen {

    private Device device;
    private String screenName;

    protected BaseScreen(Device device, String screenName) {
        this.device = device;
        this.screenName = screenName;
    }
}
