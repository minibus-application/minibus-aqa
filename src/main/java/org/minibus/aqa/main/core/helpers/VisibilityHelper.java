package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.handlers.TestStepLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class VisibilityHelper {
    private static final Logger LOGGER = LogManager.getLogger(VisibilityHelper.class);
    private static final int DEFAULT_ELEMENT_TIMEOUT = ConfigManager.getGeneralConfig().elementTimeout();

    public static boolean isVisible(MobileElement el, int timeoutSec) {
        return fluentWaitUntil(MobileElementExpectedConditions.presenceOfElement(el), timeoutSec);
    }

    public static boolean isVisible(MobileElement el) {
        return isVisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean isInvisible(MobileElement el, int timeoutSec) {
        return fluentWaitUntil(MobileElementExpectedConditions.absenceOfElement(el), timeoutSec);
    }

    public static boolean isInvisible(MobileElement el) {
        return isInvisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean areVisible(List<MobileElement> els, int timeoutSec) {
        return fluentWaitUntil(MobileElementExpectedConditions.presenceOfAllElements(els), timeoutSec);
    }

    public static boolean areVisible(List<MobileElement> els) {
        return areVisible(els, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean areInvisible(List<MobileElement> els, int timeoutSec) {
        return fluentWaitUntil(MobileElementExpectedConditions.absenceOfAllElements(els), timeoutSec);
    }

    public static boolean areInvisible(List<MobileElement> els) {
        return areInvisible(els, DEFAULT_ELEMENT_TIMEOUT);
    }

    private static boolean fluentWaitUntil(ExpectedCondition<?> expectedCondition, int timeoutSec) {
        try {
            getFluentWait(timeoutSec).until(expectedCondition);
            return true;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    private static FluentWait<AndroidDriver<MobileElement>> getFluentWait(int timeoutSec) {
        return new FluentWait<>(Device.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(NotFoundException.class)
                .ignoring(TimeoutException.class);
    }

    static class MobileElementExpectedConditions {

        public static ExpectedCondition<List<MobileElement>> presenceOfAllElements(List<MobileElement> els) {
            return new ExpectedCondition<>() {
                @Override
                public List<MobileElement> apply(WebDriver webDriver) {
                    return !els.stream()
                            .map(el -> MobileElementExpectedConditions.presenceOfElement(el).apply(webDriver))
                            .collect(Collectors.toList()).contains(null) ? els : null;
                }

                public String toString() {
                    return "presence of elements " + els;
                }
            };
        }

        public static ExpectedCondition<Boolean> absenceOfAllElements(List<MobileElement> els) {
            return new ExpectedCondition<>() {
                @Override
                public Boolean apply(WebDriver webDriver) {
                    return els.stream().allMatch(el -> {
                        return MobileElementExpectedConditions.absenceOfElement(el).apply(webDriver);
                    });
                }

                public String toString() {
                    return "absence of elements " + els;
                }
            };
        }

        public static ExpectedCondition<Boolean> absenceOfElement(MobileElement el) {
            return new ExpectedCondition<>() {

                @Override
                public Boolean apply(WebDriver webDriver) {
                    try {
                        return !el.isDisplayed();
                    } catch (NoSuchElementException | StaleElementReferenceException e) {
                        return true;
                    }
                }

                public String toString() {
                    return "absence of element " + el;
                }
            };
        }

        public static ExpectedCondition<WebElement> presenceOfElement(MobileElement el) {
            return new ExpectedCondition<>() {

                @Override
                public WebElement apply(WebDriver webDriver) {
                    return ExpectedConditions.visibilityOf(el).apply(webDriver);
                }

                public String toString() {
                    return "presence of element " + el;
                }
            };
        }
    }

}
