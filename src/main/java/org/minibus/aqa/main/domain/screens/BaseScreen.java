package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.helpers.VisibilityHelper;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BaseScreen implements Screen {

    @CacheLookup
    @AndroidFindBy(id = "tv_toolbar_title")
    private AndroidElement textTitle;

    protected static final Logger LOGGER = LogManager.getLogger(BaseScreen.class);
    private AndroidDriver<AndroidElement> driver;
    private final String screenName;
    private final int screenTimeout;

    protected BaseScreen(AndroidDriver<AndroidElement> driver, String screenName) {
        this.driver = driver;
        this.screenName = screenName;
        this.screenTimeout = ConfigManager.getGeneralConfig().screenTimeout();
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(0)), this);
    }

    protected AndroidElement getTitleElement() {
        return textTitle;
    }

    protected int getScreenTimeout() {
        return screenTimeout;
    }

    @Override
    public abstract boolean isOpened(int timeoutSec);

    @Override
    public boolean isOpened() {
        return isOpened(screenTimeout);
    }

    @Override
    public String getTitle() {
        return textTitle.getText();
    }

    @Override
    public String getScreenName() {
        return screenName;
    }

    @Override
    public AndroidDriver<AndroidElement> getDriver() {
        return driver;
    }
}
