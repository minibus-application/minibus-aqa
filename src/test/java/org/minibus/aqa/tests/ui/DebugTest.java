package org.minibus.aqa.tests.ui;

import io.appium.java_client.MobileElement;
import org.minibus.aqa.tests.BaseTest;
import org.testng.annotations.Test;


public class DebugTest extends BaseTest {

    @Test
    public void testDebug() throws InterruptedException {
        MobileElement busStopsButton = (MobileElement) getDevice().getDriver().findElementById("android:id/button1");
        busStopsButton.click();

        Thread.sleep(3000);
    }
}
