package org.minibus.aqa.main.domain.screens;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidView;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public abstract class BaseLoadableScreen extends BaseScreen implements Loadable {

    @ViewInfo(name = "Progress bar", findBy = @FindBy(id = "progress_loading"))
    private AndroidView progressBar;

    protected BaseLoadableScreen(AndroidDriver<WebElement> driver, String screenName) {
        super(driver, screenName);
    }

    @Override
    public boolean isLoading() {
        return progressBar.exists();
    }

    @Override
    @Step("Wait for screen to load ({timeoutSec} sec)")
    public boolean waitForLoading(int timeoutSec) {
        return progressBar.waitUntilInvisible(timeoutSec);
    }

    protected AndroidView getProgressBar() {
        return progressBar;
    }
}
