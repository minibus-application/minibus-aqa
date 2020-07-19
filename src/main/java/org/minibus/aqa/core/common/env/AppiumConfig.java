package org.minibus.aqa.core.common.env;

import org.apache.http.client.utils.URIBuilder;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.handlers.TestLogger;
import org.minibus.aqa.core.helpers.ResourceHelper;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class AppiumConfig implements Config {

    private Properties config;
    private String host;
    private int port;
    private URL appiumUrl;
    private int commandTimeout;
    private Boolean eventTimings;
    private Boolean logPerformance;
    private Boolean clearGeneratedFilesOnEnd;

    public AppiumConfig() {
        config = ResourceHelper.getInstance().loadProperties("appium");

        if (isStandalone()) {
            host = initProperty(Key.HOST, true);
            port = Integer.valueOf(initProperty(Key.PORT, true));

            try {
                appiumUrl = new URIBuilder().setScheme("http")
                        .setHost(host)
                        .setPort(port)
                        .setPath("/wd/hub")
                        .build()
                        .toURL();
            } catch (URISyntaxException | MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Properties getConfig() {
        return config;
    }

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public Boolean isEventTimingsEnabled() {
        return eventTimings;
    }

    public Boolean isPerformanceLoggingEnabled() {
        return logPerformance;
    }

    public Boolean clearGeneratedFilesOnEnd() {
        return clearGeneratedFilesOnEnd;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Boolean isStandalone() {
        return isDefined(Key.HOST) && isDefined(Key.PORT);
    }

    public URL getAppiumUrl() {
        return appiumUrl;
    }

    public void setAppiumUrl(URL url) {
        this.appiumUrl = url;
    }

    public AppiumConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public AppiumConfig setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return stringify();
    }

    public enum Key {
        COMMAND_TIMEOUT("appium.command.timeout"),
        EVENT_TIMINGS("appium.event.timings"),
        LOG_PERFORMANCE("appium.performanceLogging"),
        CLEAR_GENERATED_FILES_ON_END("appium.clearGeneratedFiles.onEnd"),
        HOST("appium.host"),
        PORT("appium.port");

        private final String key;

        Key(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
