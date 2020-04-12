package org.minibus.aqa.core.common.env;

import org.apache.http.client.utils.URIBuilder;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.helpers.ResourceHelper;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class AppiumConfig implements Config {

    private static final String FILE_NAME = "appium" + FILE_POSTFIX;

    private Properties config;
    private int commandTimeout;
    private Boolean eventTimings;
    private Boolean logPerformance;
    private Boolean clearGeneratedFilesOnEnd;
    private String host;
    private int port;
    private URL appiumUrl;
    private Boolean isStandalone;

    public AppiumConfig() {
        config = ResourceHelper.getInstance().loadProperties(FILE_NAME);

        isStandalone = Boolean.valueOf(initProperty(Key.STANDALONE, "false"));
        if (isStandalone) {
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
        return isStandalone;
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

    public AppiumConfig setStandalone(Boolean standalone) {
        isStandalone = standalone;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + Constants.NEW_LINE
                + config.toString().replace(", ", Constants.NEW_LINE);
    }

    public enum Key {
        COMMAND_TIMEOUT("appium.command.timeout"),
        EVENT_TIMINGS("appium.event.timings"),
        LOG_PERFORMANCE("appium.performanceLogging"),
        CLEAR_GENERATED_FILES_ON_END("appium.clearGeneratedFiles.onEnd"),
        HOST("appium.host"),
        PORT("appium.port"),
        STANDALONE("appium.standalone");

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
