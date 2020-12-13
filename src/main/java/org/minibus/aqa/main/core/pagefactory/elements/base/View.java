package org.minibus.aqa.main.core.pagefactory.elements.base;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.helpers.ScrollHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public interface View {
    Logger LOGGER = LogManager.getLogger(View.class);
    int DEFAULT_VIEW_TIMEOUT = ConfigManager.getGeneralConfig().elementTimeout();

    WebElement getWrappedElement();

    String getName();

    By getBy();

    default void tap() {
        LOGGER.trace("Tap on view: {}", getName().toUpperCase());
        getWrappedElement().click();
    }

    default boolean isEnabled() {
        return Boolean.parseBoolean(getWrappedElement().getAttribute("enabled"));
    }

    default String getText() {
        return getWrappedElement().getText();
    }

    default Rectangle getRect() {
        return getWrappedElement().getRect();
    }

    default Point getCenter() {
        Point upperLeft = getWrappedElement().getLocation();
        Dimension dimensions = getWrappedElement().getSize();
        return new Point(upperLeft.getX() + dimensions.getWidth() / 2,
                upperLeft.getY() + dimensions.getHeight() / 2);
    }

    default void scroll(ScrollHelper.ScrollDirection direction) {
        LOGGER.trace("Scroll over view: {}", getName().toUpperCase());
        ScrollHelper.scrollByDirection(direction, getRect());
    }

    default boolean exists() {
        try {
            LOGGER.trace("Check view existence: {}", getName().toUpperCase());
            return getWrappedElement().isDisplayed();
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }

    default boolean waitUntilVisible() {
        return waitUntilVisible(DEFAULT_VIEW_TIMEOUT);
    }

    default boolean waitUntilInvisible() {
        return waitUntilInvisible(DEFAULT_VIEW_TIMEOUT);
    }

    default boolean waitUntilVisible(int timeoutSec) {
        try {
            LOGGER.trace("Wait for view visibility ({} sec): {}", timeoutSec, getName().toUpperCase());
            getDefaultWait(timeoutSec).until(ExpectedConditions.visibilityOfElementLocated(getBy()));
            return true;
        } catch (WebDriverException ignore) {
            return false;
        }
    }

    default boolean waitUntilInvisible(int timeoutSec) {
        try {
            LOGGER.trace("Wait for view invisibility ({} sec): {}", timeoutSec, getName().toUpperCase());
            getDefaultWait(timeoutSec).until(ExpectedConditions.invisibilityOfElementLocated(getBy()));
            return true;
        } catch (WebDriverException ignore) {
            return false;
        }
    }

    default FluentWait getDefaultWait(int timeoutSec) {
        return new FluentWait<>(Device.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(NotFoundException.class)
                .ignoring(TimeoutException.class);
    }
}
