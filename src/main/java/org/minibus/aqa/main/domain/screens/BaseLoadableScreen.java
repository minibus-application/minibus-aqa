package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.helpers.VisibilityHelper;


public abstract class BaseLoadableScreen extends BaseScreen implements Loadable {

    @AndroidFindBy(id = "progress_loading")
    private AndroidElement progressBar;

    protected BaseLoadableScreen(AndroidDriver<AndroidElement> driver, String screenName) {
        super(driver, screenName);
    }

    @Override
    public boolean isLoading() {
        return VisibilityHelper.isVisible((progressBar), ConfigManager.getGeneralConfig().elementTimeout() / 2);
    }

    @Step("Wait for screen to load ({timeoutSec} sec)")
    @Override
    public boolean waitForLoading(int timeoutSec) {
        boolean isLoaded = VisibilityHelper.isInvisible(progressBar, timeoutSec);
        LOGGER.debug("'{}' screen is loaded", getScreenName());
        return isLoaded;
    }

    protected AndroidElement getProgressBar() {
        return progressBar;
    }
}
