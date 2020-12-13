package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.pagefactory.ViewFieldDecorator;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidView;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public abstract class BaseScreen implements Screen {

    @CacheLookup
    @ViewInfo(name = "Toolbar title", findBy = @FindBy(id = "tv_toolbar_title"))
    private AndroidView textTitle;

    protected static final Logger LOGGER = LogManager.getLogger(BaseScreen.class);
    private AndroidDriver<WebElement> driver;
    private final String screenName;
    private final int screenTimeout;
    private final int elementTimeout;

    protected BaseScreen(AndroidDriver<WebElement> driver, String screenName) {
        this.driver = driver;
        this.screenName = screenName;
        this.screenTimeout = ConfigManager.getGeneralConfig().screenTimeout();
        this.elementTimeout = ConfigManager.getGeneralConfig().elementTimeout();
        PageFactory.initElements(new ViewFieldDecorator(Device.getDriver()), this);
    }

    protected AndroidView getTitleElement() {
        return textTitle;
    }

    protected int getScreenTimeout() {
        return screenTimeout;
    }

    protected int getElementTimeout() {
        return elementTimeout;
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
    public AndroidDriver<WebElement> getDriver() {
        return driver;
    }
}
