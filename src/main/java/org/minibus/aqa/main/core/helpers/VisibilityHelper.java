package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.env.Device;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisibilityHelper {
    private static final Logger LOGGER = LogManager.getLogger(VisibilityHelper.class);
    private static final int DEFAULT_ELEMENT_TIMEOUT = ConfigManager.getGeneralConfig().elementTimeout();

    public static boolean isVisible(By selector, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.presenceOfElementLocated(selector), timeoutSec);
    }

    public static boolean isInvisible(By selector, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.absenceOfElementLocated(selector), timeoutSec);
    }

    public static boolean isVisible(AndroidElement el, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.presenceOfElement(el), timeoutSec);
    }

    public static boolean isVisible(AndroidElement el) {
        return isVisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean isInvisible(AndroidElement el, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.absenceOfElement(el), timeoutSec);
    }

    public static boolean isInvisible(AndroidElement el) {
        return isInvisible(el, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean areVisible(List<AndroidElement> els, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.presenceOfAllElements(els), timeoutSec);
    }

    public static boolean areVisible(List<AndroidElement> els) {
        return areVisible(els, DEFAULT_ELEMENT_TIMEOUT);
    }

    public static boolean areInvisible(List<AndroidElement> els, int timeoutSec) {
        return fluentWaitUntil(AndroidElementExpectedConditions.absenceOfAllElements(els), timeoutSec);
    }

    public static boolean areInvisible(List<AndroidElement> els) {
        return areInvisible(els, DEFAULT_ELEMENT_TIMEOUT);
    }

    private static boolean fluentWaitUntil(AndroidElementExpectedCondition<?> expectedCondition, int timeoutSec) {
        try {
            getFluentWait(timeoutSec).until(expectedCondition);
            return true;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    private static FluentWait<AndroidDriver<AndroidElement>> getFluentWait(int timeoutSec) {
        return new AppiumFluentWait<>(Device.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(NotFoundException.class)
                .ignoring(TimeoutException.class);
    }

    private interface AndroidElementExpectedCondition<T> extends Function<AndroidDriver<AndroidElement>, T> {
    }

    static class AndroidElementExpectedConditions {

        public static AndroidElementExpectedCondition<List<AndroidElement>> presenceOfAllElements(List<AndroidElement> els) {
            return new AndroidElementExpectedCondition<List<AndroidElement>>() {
                @Override
                public List<AndroidElement> apply(AndroidDriver driver) {
                    return !els.stream()
                            .map(el -> AndroidElementExpectedConditions.presenceOfElement(el).apply(driver))
                            .collect(Collectors.toList()).contains(null) ? els : null;
                }

                public String toString() {
                    return "presence of elements " + els;
                }
            };
        }

        public static AndroidElementExpectedCondition<Boolean> absenceOfAllElements(List<AndroidElement> els) {
            return new AndroidElementExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(AndroidDriver driver) {
                    return els.stream().allMatch(el -> {
                        return AndroidElementExpectedConditions.absenceOfElement(el).apply(driver);
                    });
                }

                public String toString() {
                    return "absence of elements " + els;
                }
            };
        }

        public static AndroidElementExpectedCondition<Boolean> absenceOfElement(AndroidElement el) {
            return new AndroidElementExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(AndroidDriver driver) {
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

        public static AndroidElementExpectedCondition<AndroidElement> presenceOfElement(AndroidElement el) {
            return new AndroidElementExpectedCondition<AndroidElement>() {

                @Override
                public AndroidElement apply(AndroidDriver driver) {
                    return AndroidElementExpectedConditions.elementIfVisible(el);
                }

                public String toString() {
                    return "presence of element " + el;
                }
            };
        }

        public static AndroidElementExpectedCondition<AndroidElement> presenceOfElementLocated(By selector) {
            return new AndroidElementExpectedCondition<AndroidElement>() {

                @Override
                public AndroidElement apply(AndroidDriver driver) {
                    try {
                        return AndroidElementExpectedConditions.elementIfVisible((AndroidElement) driver.findElement(selector));
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }

                public String toString() {
                    return "presence of element located by " + selector;
                }
            };
        }

        public static AndroidElementExpectedCondition<Boolean> absenceOfElementLocated(By selector) {
            return new AndroidElementExpectedCondition<Boolean>() {

                @Override
                public Boolean apply(AndroidDriver driver) {
                    try {
                        driver.findElement(selector);
                        return false;
                    } catch (NoSuchElementException | StaleElementReferenceException e1) {
                        return true;
                    }
                }

                public String toString() {
                    return "absence of element located by " + selector;
                }
            };
        }

        private static AndroidElement elementIfVisible(AndroidElement element) {
            return element.isDisplayed() ? element : null;
        }
    }
}
