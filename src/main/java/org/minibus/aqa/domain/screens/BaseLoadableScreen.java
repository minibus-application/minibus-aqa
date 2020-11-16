package org.minibus.aqa.domain.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.minibus.aqa.core.env.config.ConfigManager;
import org.minibus.aqa.core.env.config.GeneralConfig;
import org.minibus.aqa.core.helpers.VisibilityHelper;


public abstract class BaseLoadableScreen extends BaseScreen implements Loadable {

    @AndroidFindBy(id = "progress_loading")
    private AndroidElement progressBar;

    protected BaseLoadableScreen(AppiumDriver<MobileElement> driver, String screenName) {
        super(driver, screenName);
    }

    @Override
    public boolean isLoading() {
        return VisibilityHelper.isVisible((progressBar), ConfigManager.getGeneralConfig().elementTimeout() / 2);
    }

    @Override
    public boolean waitForLoading(int timeoutSec) {
        if (isLoading()) {
            return VisibilityHelper.isInvisible((progressBar), timeoutSec);
        }
        return true;
    }

    protected AndroidElement getProgressBar() {
        return progressBar;
    }
}
