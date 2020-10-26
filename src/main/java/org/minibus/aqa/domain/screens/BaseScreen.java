package org.minibus.aqa.domain.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.minibus.aqa.core.common.env.config.ConfigManager;
import org.minibus.aqa.core.helpers.ActionsHelper;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BaseScreen implements Screen {

    @AndroidFindBy(id = "tv_toolbar_title")
    private AndroidElement textTitle;

    @AndroidFindBy(id = "progress_loading")
    private AndroidElement progressBar;

    @AndroidFindBy(id = "container_hud")
    private AndroidElement progressHud;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseScreen.class);
    private AppiumDriver<MobileElement> driver;
    private final String screenName;
    private final int screenTimeout;

    protected BaseScreen(AppiumDriver<MobileElement> driver, String screenName) {
        this.driver = driver;
        this.screenName = screenName;
        this.screenTimeout = ConfigManager.getGeneralConfig().screenTimeout();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected boolean isLoading() {
        return ActionsHelper.isVisible(progressBar) || ActionsHelper.isVisible(progressHud);
    }

    protected boolean waitForLoading(int timeoutSec) {
        if (isLoading()) {
            return ActionsHelper.isInvisible(List.of(progressBar, progressHud), timeoutSec);
        }
        return true;
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
