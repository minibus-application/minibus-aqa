package org.minibus.aqa.core.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.core.common.env.Environment;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class AppiumLocalManager {

    private static AppiumLocalManager instance;

    private static AppiumDriverLocalService service;
    private AppiumDriver appiumDriver;
    private DesiredCapabilities capabilities;
    private String sessionId;
    private String host;
    private int port;

    private AppiumLocalManager() {}

    public static synchronized AppiumLocalManager getInstance(){
        if (instance == null) instance = new AppiumLocalManager();
        return instance;
    }

    public AppiumDriverLocalService getService() {
        return service;
    }

    public AppiumDriverLocalService createService(AppiumServiceBuilder builder) {
        service = builder.build();

        Environment.getInstance()
                .getAppiumConfig()
                .setStandalone(false)
                .setAppiumUrl(service.getUrl());

        return service;
    }

    public AppiumDriverLocalService createServiceDefault() {
        service = createService(new AppiumServiceBuilder());
        return service;
    }

    public void start() {
        service.start();
    }

    public void terminate() {
        service.stop();
    }

    // todo
    public void restart() {
        if (isRunning()) {
            host = getServiceUrl().getHost();
            port = getServiceUrl().getPort();
            terminate();
        }
    }

    public URL getServiceUrl() {
        return service.getUrl();
    }

    public boolean isRunning() {
        return service != null && service.isRunning();
    }
}
