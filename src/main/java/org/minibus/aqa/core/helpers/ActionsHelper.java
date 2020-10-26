package org.minibus.aqa.core.helpers;

import io.appium.java_client.MobileElement;
import org.minibus.aqa.core.common.env.config.ConfigManager;
import org.minibus.aqa.core.common.env.Device;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ActionsHelper {

    private static final int DEFAULT_ELEMENT_TIMEOUT = ConfigManager.getGeneralConfig().elementTimeout();

    public static boolean isVisible(MobileElement el, int timeoutSec) {
        return fluentWaitUntil(ExpectedConditions.visibilityOf(el), timeoutSec);
    }

    public static boolean isVisible(MobileElement el) {
        return isVisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean isInvisible(MobileElement el, int timeoutSec) {
        return fluentWaitUntil(ExpectedConditions.invisibilityOf(el), timeoutSec);
    }

    public static boolean isInvisible(List<MobileElement> els, int timeoutSec) {
        return fluentWaitUntil(ExpectedConditions.invisibilityOfAllElements((WebElement) els), timeoutSec);
    }

    public static boolean isInvisible(MobileElement el) {
        return isInvisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    private static boolean fluentWaitUntil(ExpectedCondition<?> expectedCondition, int timeoutSec) {
        try {
            getFluentWait(timeoutSec).until(expectedCondition);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static FluentWait getFluentWait(int timeoutSec) {
        return new FluentWait(Device.getDriver())
                .withTimeout(Duration.of(timeoutSec, SECONDS))
                .pollingEvery(Duration.of(100, MILLIS))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }
}
