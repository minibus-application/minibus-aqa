package org.minibus.aqa.main.domain.screens.cities;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.helpers.ImageProcessor;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidLayout;
import org.minibus.aqa.main.core.pagefactory.elements.TextView;
import org.minibus.aqa.main.domain.screens.schedule.CalendarDayLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CityLayout extends AndroidLayout {

    @ViewInfo(name = "City name", findBy = @FindBy(id = "tv_city_title"))
    private TextView textCityName;

    @ViewInfo(name = "City region name", findBy = @FindBy(id = "tv_region_subtitle"))
    private TextView textRegionName;

    @ViewInfo(name = "City location button", findBy = @FindBy(id = "iv_city_location"))
    private TextView buttonCityLocation;

    public CityLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    @Step("Open Google Directions app")
    public void openGoogleDirections() {
        buttonCityLocation.tap();
    }

    public String getCityName() {
        return textCityName.getText();
    }

    public String getRegionName() {
        return textRegionName.getText();
    }

    public boolean isSelected() {
        return ImageProcessor.hasColor(textCityName, ImageProcessor.ImageColor.BLUE);
    }
}
