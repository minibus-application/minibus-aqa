package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BaseScreen implements Screen {

    @AndroidFindBy(id = "tv_toolbar_title")
    private AndroidElement textTitle;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseScreen.class);
    private AppiumDriver<MobileElement> driver;
    private final String screenName;
    private final int screenTimeout;

    protected BaseScreen(AppiumDriver<MobileElement> driver, String screenName) {
        this.driver = driver;
        this.screenName = screenName;
        this.screenTimeout = ConfigManager.getGeneralConfig().screenTimeout();
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(0)), this);
    }

    protected MobileElement getTitleElement() {
        return textTitle;
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
    public String getName() {
        return screenName;
    }

    @Override
    public AppiumDriver<MobileElement> getDriver() {
        return driver;
    }
}
