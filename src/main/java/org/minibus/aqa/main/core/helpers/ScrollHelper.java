package org.minibus.aqa.main.core.helpers;

import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.handlers.TestStepLogger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.Duration;
import java.util.HashMap;

final public class ScrollHelper {
    private static final Logger LOGGER = LogManager.getLogger(ScrollHelper.class);
    private static final Duration DEFAULT_SCROLL_DURATION = Duration.ofMillis(300);
    private static final int DEFAULT_AREA_OFFSET = 10;

    public static void scroll(ScrollDirection direction, WebElement element) {
        final HashMap<String, String> args = new HashMap<>();
        args.putAll(getElementScrollCmdArg(element));
        args.putAll(getDirectionScrollArg(direction));
        executeMobileScroll(args);
    }

    public static void scrollByDirection(Rectangle onArea, ScrollDirection direction) {
        Point topCenter = new Point(onArea.getX() + (onArea.getWidth() / 2), onArea.getY() + DEFAULT_AREA_OFFSET);
        Point bottomCenter = new Point(onArea.getX() + (onArea.getWidth() / 2), onArea.getY() + (onArea.getHeight() - DEFAULT_AREA_OFFSET));
        Point leftCenter = new Point(onArea.getX() + DEFAULT_AREA_OFFSET, onArea.getY() + (onArea.getHeight() / 2));
        Point rightCenter = new Point(onArea.getX() + onArea.getWidth() - DEFAULT_AREA_OFFSET, onArea.getY() + (onArea.getHeight() / 2));

        switch (direction) {
            case DOWN:
                LOGGER.debug("Scrolling to the {}: from {}, to {}", direction, bottomCenter, topCenter);
                scroll(bottomCenter, topCenter);
                break;
            case UP:
                LOGGER.debug("Scrolling to the {}: from {}, to {}", direction, topCenter, bottomCenter);
                scroll(topCenter, bottomCenter);
                break;
            case RIGHT:
                LOGGER.debug("Scrolling to the {}: from {}, to {}", direction, rightCenter, leftCenter);
                scroll(rightCenter, leftCenter);
                break;
            case LEFT:
                LOGGER.debug("Scrolling to the {}: from {}, to {}", direction, leftCenter, rightCenter);
                scroll(leftCenter, rightCenter);
                break;
            default:
                throw new NotImplementedException(direction.name() + " is not implemented");
        }
    }

    public static void scrollByDirection(WebElement onElement, ScrollDirection direction) {
        if (onElement == null) {
            scrollByDirection(direction);
        } else {
            scrollByDirection(onElement.getRect(), direction);
        }
    }

    public static void scrollByDirection(ScrollDirection direction) {
        Point screenPoint = Device.getDriver().manage().window().getPosition();
        Dimension screenDimension = Device.getDriver().manage().window().getSize();
        scrollByDirection(new Rectangle(screenPoint, screenDimension), direction);
    }

    public static void scroll(Point startPoint, Point endPoint) {
        new AndroidTouchAction(Device.getDriver()).press(PointOption.point(startPoint))
                .waitAction(WaitOptions.waitOptions(DEFAULT_SCROLL_DURATION))
                .moveTo(PointOption.point(endPoint))
                .release()
                .perform();
    }

    private static HashMap<String, String> getElementScrollCmdArg(WebElement element) {
        return new HashMap<>() {{ put("element", ((RemoteWebElement) element).getId()); }};
    }

    private static HashMap<String, String> getDirectionScrollArg(ScrollDirection direction) {
        return new HashMap<>() {{ put("direction", direction.name().toLowerCase()); }};
    }

    private static void executeMobileScroll(HashMap<String, String> args) {
        Device.getDriver().executeScript("mobile: scroll", args);
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
