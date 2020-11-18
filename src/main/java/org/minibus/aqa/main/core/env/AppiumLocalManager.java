package org.minibus.aqa.main.core.env;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.cli.ShellCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;

public class AppiumLocalManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumLocalManager.class);
    private static final int TERMINATING_TIMEOUT = 15;
    private static AppiumLocalManager instance;
    private static AppiumDriverLocalService service;

    private AppiumLocalManager(AppiumServiceBuilder builder) {
        service = builder.build();
        int port = service.getUrl().getPort();
        String host = service.getUrl().getHost();

        if (!ConfigManager.getGeneralConfig().systemName().toLowerCase().startsWith("windows")) {
            if (!ShellCommandExecutor.isPortOpened(port)) {
                throw new UnsupportedOperationException(String.format("Can't build Appium service, %d port is in use", port));
            }
        }

        System.setProperty("appium.port", String.valueOf(port));
        System.setProperty("appium.host", host);
        ConfigManager.getAppiumConfig().reload();
    }

    public static synchronized AppiumLocalManager getService(AppiumServiceBuilder builder) {
        if (instance == null) {
            LOGGER.info("Appium local service hasn't built yet, building a new one...");
            instance = new AppiumLocalManager(builder);
        }
        return instance;
    }

    public static synchronized AppiumLocalManager getService() {
        return getService(new AppiumServiceBuilder());
    }

    public void start() {
        LOGGER.info(String.format("Starting Appium local service: %s", service.getUrl()));
        service.start();
    }

    public static void stop() {
        if (isRunning()) {
            LOGGER.info(String.format("Stopping Appium local service: %s", service.getUrl()));
            service.stop();
        }
    }

    public static void restart() {
        if (isRunning()) {
            LOGGER.info(String.format("Restarting Appium local service: %s", service.getUrl()));
            service.stop();
            service.start();

            LocalDateTime endTime = LocalDateTime.now().plusSeconds(TERMINATING_TIMEOUT);
            do {
                if (isRunning()) {
                    LOGGER.info("Appium local service has been successfully restarted");
                    break;
                }
            } while(LocalDateTime.now().isBefore(endTime));
        }
    }

    public String getHost() {
        return service.getUrl().getHost();
    }

    public int getPort() {
        return service.getUrl().getPort();
    }

    public URL getServiceUrl() {
        return service.getUrl();
    }

    public static boolean isRunning() {
        return service != null && service.isRunning();
    }
}
