package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.pagefactory.elements.base.View;
import org.openqa.selenium.*;

import java.time.Duration;
import java.time.LocalDateTime;

final public class ScrollHelper {
    private static final Logger LOGGER = LogManager.getLogger(ScrollHelper.class);
    private static final Duration DEFAULT_SCROLL_DURATION = Duration.ofMillis(600);
    private static final double DEFAULT_AREA_PADDING = 0.8;
    private static final Duration DEFAULT_ELEMENT_SEARCH_TIMEOUT = Duration.ofSeconds(25);

    public static void scroll(Point startPoint, Point endPoint, Duration duration) {
        new AndroidTouchAction(Device.getDriver()).press(PointOption.point(startPoint))
                .waitAction(WaitOptions.waitOptions(duration))
                .moveTo(PointOption.point(endPoint))
                .release()
                .perform();
    }

    public static void scrollToBottom(Rectangle onArea) {
        String pageSourceBeforeScroll;
        do {
            pageSourceBeforeScroll = Device.getDriver().getPageSource();
            scrollByDirection(ScrollDirection.DOWN, onArea);
        } while (!Device.getDriver().getPageSource().equals(pageSourceBeforeScroll));
    }

    public static void scrollByDirection(ScrollDirection byDirection, Rectangle onArea, Duration duration, double padding) {
        if (padding < 0 || padding > 1) throw new RuntimeException("Scrolling area padding must be between 0 and 1");

        Dimension areaSize = onArea.getDimension();
        Point middlePoint = new Point(areaSize.getWidth() / 2 + onArea.x, areaSize.getHeight() / 2 + onArea.y);
        Point middleTopPoint = new Point(middlePoint.x, middlePoint.y - (int) ((areaSize.height * padding) * 0.5));
        Point middleBottomPoint = new Point(middlePoint.x, middlePoint.y + (int) ((areaSize.height * padding) * 0.5));
        Point middleRightPoint = new Point(middlePoint.x + (int) ((areaSize.width * padding) * 0.5), middlePoint.y);
        Point middleLeftPoint = new Point(middlePoint.x - (int) ((areaSize.width * padding) * 0.5), middlePoint.y);

        switch (byDirection) {
            case DOWN:
                LOGGER.trace("Scrolling to the {}: {} -> {}", byDirection, middleBottomPoint, middleTopPoint);
                scroll(middleBottomPoint, middleTopPoint, duration);
                break;
            case UP:
                LOGGER.trace("Scrolling to the {}: {} -> {}", byDirection, middleTopPoint, middleBottomPoint);
                scroll(middleTopPoint, middleBottomPoint, duration);
                break;
            case RIGHT:
                LOGGER.trace("Scrolling to the {}: {} -> {}", byDirection, middleRightPoint, middleLeftPoint);
                scroll(middleRightPoint, middleLeftPoint, duration);
                break;
            case LEFT:
                LOGGER.trace("Scrolling to the {}: {} -> {}", byDirection, middleRightPoint, middleLeftPoint);
                scroll(middleLeftPoint, middleRightPoint, duration);
                break;
            default:
                throw new NotImplementedException(byDirection + " is not implemented");
        }
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, Rectangle onArea, View toView, Duration withDuration, double withPadding) {
        if (!toView.isVisible()) {
            String pageSourceBeforeScroll;
            LocalDateTime timeout = LocalDateTime.now().plusSeconds(DEFAULT_ELEMENT_SEARCH_TIMEOUT.getSeconds());

            do {
                pageSourceBeforeScroll = Device.getDriver().getPageSource();
                scrollByDirection(byDirection, onArea, withDuration, withPadding);
            } while (!toView.isVisible()
                    && LocalDateTime.now().isBefore(timeout)
                    && !Device.getDriver().getPageSource().equals(pageSourceBeforeScroll));

            return toView.isVisible();
        } else {
            return true;
        }
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, Rectangle onArea, View toView) {
        return scrollToViewByDirection(byDirection, onArea, toView, DEFAULT_SCROLL_DURATION, DEFAULT_AREA_PADDING);
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, Rectangle onArea, View toView, Duration withDuration) {
        return scrollToViewByDirection(byDirection, onArea, toView, withDuration, DEFAULT_AREA_PADDING);
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, Rectangle onArea, View toView, double withPadding) {
        return scrollToViewByDirection(byDirection, onArea, toView, DEFAULT_SCROLL_DURATION, withPadding);
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, View toView) {
        return scrollToViewByDirection(byDirection, getDefaultScrollingArea(), toView, DEFAULT_SCROLL_DURATION, DEFAULT_AREA_PADDING);
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, View toView, Duration withDuration) {
        return scrollToViewByDirection(byDirection, getDefaultScrollingArea(), toView, withDuration, DEFAULT_AREA_PADDING);
    }

    public static boolean scrollToViewByDirection(ScrollDirection byDirection, View toView, double withPadding) {
        return scrollToViewByDirection(byDirection, getDefaultScrollingArea(), toView, DEFAULT_SCROLL_DURATION, withPadding);
    }

    public static void scrollByDirection(ScrollDirection byDirection) {
        scrollByDirection(byDirection, getDefaultScrollingArea(), DEFAULT_SCROLL_DURATION, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, Duration withDuration) {
        scrollByDirection(byDirection, getDefaultScrollingArea(), withDuration, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, double withPadding) {
        scrollByDirection(byDirection, getDefaultScrollingArea(), DEFAULT_SCROLL_DURATION, withPadding);
    }

    public static void scrollByDirection(ScrollDirection byDirection, Rectangle onArea, Duration withDuration) {
        scrollByDirection(byDirection, onArea, withDuration, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, Rectangle onArea, double withPadding) {
        scrollByDirection(byDirection, onArea, DEFAULT_SCROLL_DURATION, withPadding);
    }

    public static void scrollByDirection(ScrollDirection byDirection, Rectangle onArea) {
        scrollByDirection(byDirection, onArea, DEFAULT_SCROLL_DURATION, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, View onView) {
        scrollByDirection(byDirection, onView.getRect(), DEFAULT_SCROLL_DURATION, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, View onView, Duration duration) {
        scrollByDirection(byDirection, onView.getRect(), duration, DEFAULT_AREA_PADDING);
    }

    public static void scrollByDirection(ScrollDirection byDirection, View onView, double padding) {
        scrollByDirection(byDirection, onView.getRect(), DEFAULT_SCROLL_DURATION, padding);
    }

    public static void scrollByDirection(ScrollDirection byDirection, View onView, Duration duration, double padding) {
        scrollByDirection(byDirection, onView.getRect(), duration, padding);
    }

    private static Duration getDefaultDurationMs(ScrollDirection direction, Rectangle area) {
        final int durationFactor = 2;
        if (direction.equals(ScrollDirection.DOWN) || direction.equals(ScrollDirection.UP)) {
            return Duration.ofMillis(area.getHeight() * durationFactor);
        } else {
            return Duration.ofMillis(area.getWidth() * durationFactor);
        }
    }

    private static Rectangle getDefaultScrollingArea() {
        Dimension screenDimension = Device.getDriver().manage().window().getSize();
        return new Rectangle(new Point(0, 0), screenDimension);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
