package org.minibus.aqa.domain.screens;

import org.minibus.aqa.core.common.env.device.Device;
import org.minibus.aqa.core.context.DeviceAppContext;

public abstract class BaseScreen implements Screen {


    protected BaseScreen(Device device, String screenName) {
        // this(device, screenName, ContextManager.getExecutionContext().getDeviceAppContext());
    }

    public BaseScreen(Device device, String screenName, DeviceAppContext deviceAppContext) {

    }
}
