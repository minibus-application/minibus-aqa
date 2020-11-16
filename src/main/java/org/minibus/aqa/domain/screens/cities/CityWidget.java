package org.minibus.aqa.domain.screens.cities;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import org.minibus.aqa.core.helpers.ImageProcessor;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;
import org.openqa.selenium.WebElement;

@AndroidFindBy(id = "ll_city_container")
public class CityWidget extends Widget {

    @AndroidFindBy(id = "tv_city_title")
    private AndroidElement textCityName;

    @AndroidFindBy(id = "tv_region_subtitle")
    private AndroidElement textRegionName;

    @AndroidFindBy(id = "iv_city_location")
    private AndroidElement iconCityLocation;

    protected CityWidget(WebElement element) {
        super(element);
    }

    public String getCityName() {
        return textCityName.getText();
    }

    public String getRegionName() {
        return textRegionName.getText();
    }

    public void openGoogleDirections() {
        iconCityLocation.click();
    }

    public boolean isSelected() {
        return ImageProcessor.hasColor(textCityName, ImageProcessor.ImageColor.BLUE);
    }

    public ScheduleScreen select() {
        this.getWrappedElement().click();
        return new ScheduleScreen();
    }
}
