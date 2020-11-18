package org.minibus.aqa.main.core.handlers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger {

    private static ThreadLocal<TestLogger> instance = new ThreadLocal<>();
    private static Logger logback;

    private TestLogger() {
        logback = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("appium.tests");
        logback.setLevel(Level.DEBUG);

//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        StatusPrinter.print(lc);
    }

    public static synchronized TestLogger get() {
        if (instance.get() == null) {
            instance.set(new TestLogger());
        }
        return instance.get();
    }

    public void debug(String msg) {
        logback.debug(msg);
    }

    public void info(String msg) {
        logback.info(msg);
    }

    public void error(String msg) {
        logback.error(msg);
    }

    public void warning(String msg) {
        logback.warn(msg);
    }

    public void success(String msg) {
        logback.info(msg);
    }

    public void fail(String msg) {
        logback.error(msg);
    }
}
