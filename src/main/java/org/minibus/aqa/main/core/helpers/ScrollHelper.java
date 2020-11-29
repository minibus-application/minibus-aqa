package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;

import java.time.Duration;
import java.time.LocalDateTime;

final public class ScrollHelper {
    private static final Logger LOGGER = LogManager.getLogger(ScrollHelper.class);
    private static final Duration DEFAULT_SCROLL_DURATION = Duration.ofMillis(800);
    private static final Duration DEFAULT_ELEMENT_SEARCH_TIMEOUT = Duration.ofSeconds(25);
    private static final int DEFAULT_AREA_OFFSET = 10;

    public static void scrollToBottom(Rectangle onArea) {
        String pageSourceBeforeScroll;
        do {
            pageSourceBeforeScroll = Device.getDriver().getPageSource();
            scrollByDirection(onArea, ScrollDirection.DOWN);
        } while(!Device.getDriver().getPageSource().equals(pageSourceBeforeScroll));
    }

    public static boolean scrollToElementByDirection(Rectangle onArea, AndroidElement toElement, ScrollDirection byDirection) {
        if (VisibilityHelper.isInvisible(toElement, 0)) {
            String pageSourceBeforeScroll;
            LocalDateTime timeout = LocalDateTime.now().plusSeconds(DEFAULT_ELEMENT_SEARCH_TIMEOUT.getSeconds());

            do {
                pageSourceBeforeScroll = Device.getDriver().getPageSource();
                scrollByDirection(onArea, byDirection);
            } while(VisibilityHelper.isInvisible(toElement, 0)
                    && LocalDateTime.now().isBefore(timeout)
                    && !Device.getDriver().getPageSource().equals(pageSourceBeforeScroll));

            return VisibilityHelper.isVisible(toElement, 0);
        } else {
            return true;
        }
    }

    public static boolean scrollToElementByDirection(Rectangle onArea, By selector, ScrollDirection byDirection) {
        if (VisibilityHelper.isInvisible(selector, 0)) {
            String pageSourceBeforeScroll;
            LocalDateTime timeout = LocalDateTime.now().plusSeconds(DEFAULT_ELEMENT_SEARCH_TIMEOUT.getSeconds());

            do {
                pageSourceBeforeScroll = Device.getDriver().getPageSource();
                scrollByDirection(onArea, byDirection);
            } while(VisibilityHelper.isInvisible(selector, 0)
                    && LocalDateTime.now().isBefore(timeout)
                    && !Device.getDriver().getPageSource().equals(pageSourceBeforeScroll));

            return VisibilityHelper.isVisible(selector, 0);
        } else {
            return true;
        }
    }

    public static boolean scrollToElementByDirection(AndroidElement toElement, ScrollDirection byDirection) {
        return scrollToElementByDirection(getDefaultScrollArea(), toElement, byDirection);
    }

    public static void scrollByDirection(Rectangle onArea, ScrollDirection byDirection) {
        Point topCenter = new Point(onArea.getX() + (onArea.getWidth() / 2), onArea.getY() + DEFAULT_AREA_OFFSET);
        Point bottomCenter = new Point(onArea.getX() + (onArea.getWidth() / 2), onArea.getY() + (onArea.getHeight() - DEFAULT_AREA_OFFSET));
        Point leftCenter = new Point(onArea.getX() + DEFAULT_AREA_OFFSET, onArea.getY() + (onArea.getHeight() / 2));
        Point rightCenter = new Point(onArea.getX() + onArea.getWidth() - DEFAULT_AREA_OFFSET, onArea.getY() + (onArea.getHeight() / 2));

        switch (byDirection) {
            case DOWN:
                LOGGER.debug("Scrolling to the {}: {} -> {}", byDirection, bottomCenter, topCenter);
                scroll(bottomCenter, topCenter);
                break;
            case UP:
                LOGGER.debug("Scrolling to the {}: {} -> {}", byDirection, topCenter, bottomCenter);
                scroll(topCenter, bottomCenter);
                break;
            case RIGHT:
                LOGGER.debug("Scrolling to the {}: {} -> {}", byDirection, rightCenter, leftCenter);
                scroll(rightCenter, leftCenter);
                break;
            case LEFT:
                LOGGER.debug("Scrolling to the {}: {} -> {}", byDirection, leftCenter, rightCenter);
                scroll(leftCenter, rightCenter);
                break;
            default:
                throw new NotImplementedException(byDirection.name() + " is not implemented");
        }
    }

    public static void scrollByDirection(AndroidElement onElement, ScrollDirection byDirection) {
        if (onElement == null) {
            scrollByDirection(byDirection);
        } else {
            scrollByDirection(onElement.getRect(), byDirection);
        }
    }

    public static void scrollByDirection(ScrollDirection byDirection) {
        scrollByDirection(getDefaultScrollArea(), byDirection);
    }

    public static void scroll(Point startPoint, Point endPoint) {
        new AndroidTouchAction(Device.getDriver()).press(PointOption.point(startPoint))
                .waitAction(WaitOptions.waitOptions(DEFAULT_SCROLL_DURATION))
                .moveTo(PointOption.point(endPoint))
                .release()
                .perform();
    }

    private static Rectangle getDefaultScrollArea() {
        Dimension screenDimension = Device.getDriver().manage().window().getSize();
        Dimension halfScreenDimension = new Dimension(screenDimension.getWidth(), screenDimension.getHeight() / 2);
        // Modify start screen point to be in a center of the height (to avoid triggering the status bar panel)
        Point screenPoint = new Point(Device.getDriver().manage().window().getPosition().getX(), halfScreenDimension.getHeight());
        return new Rectangle(screenPoint, halfScreenDimension);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
