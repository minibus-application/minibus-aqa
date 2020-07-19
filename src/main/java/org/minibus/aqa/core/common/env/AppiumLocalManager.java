package org.minibus.aqa.core.common.env;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.core.common.cli.ShellCommandExecutor;
import org.minibus.aqa.core.common.handlers.TestLogger;

import java.net.URL;
import java.time.LocalDateTime;

public class AppiumLocalManager {

    private static final int TERMINATING_TIMEOUT = 15;

    private static AppiumLocalManager instance;
    private static AppiumDriverLocalService service;
    private String host;
    private int port;

    private AppiumLocalManager(AppiumServiceBuilder builder) {
        service = builder.build();
        this.port = service.getUrl().getPort();
        this.host = service.getUrl().getHost();

        if (Environment.getInstance().isUnixLike()) {
            if (!ShellCommandExecutor.isPortOpened(port)) {
                throw new UnsupportedOperationException(String.format("Can't build Appium service, %d port is in use", port));
            }
        }

        Environment.getInstance().getAppiumConfig().setAppiumUrl(service.getUrl());
    }

    public static synchronized AppiumLocalManager getService(AppiumServiceBuilder builder) {
        if (instance == null) {
            TestLogger.get().info("Appium local service hasn't built yet, building a new one...");
            instance = new AppiumLocalManager(builder);
        }
        return instance;
    }

    public static synchronized AppiumLocalManager getService() {
        return getService(new AppiumServiceBuilder());
    }

    public void start() {
        TestLogger.get().info(String.format("Starting Appium local service on %s", service.getUrl()));
        service.start();
    }

    public void stop() {
        if (isRunning()) {
            TestLogger.get().info(String.format("Stopping Appium local service on %s", service.getUrl()));
            service.stop();
        }
    }

    public void restart() {
        if (isRunning()) {
            TestLogger.get().info(String.format("Restarting Appium local service on %s", service.getUrl()));
            service.stop();
            service.start();

            LocalDateTime endTime = LocalDateTime.now().plusSeconds(TERMINATING_TIMEOUT);
            do {
                if (isRunning()) {
                    TestLogger.get().info("Appium local service has been successfully restarted");
                    break;
                }
            } while(LocalDateTime.now().isBefore(endTime));
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public URL getServiceUrl() {
        return service.getUrl();
    }

    public boolean isRunning() {
        return service != null && service.isRunning();
    }
}
