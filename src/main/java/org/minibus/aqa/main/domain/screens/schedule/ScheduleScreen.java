package org.minibus.aqa.main.domain.screens.schedule;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.helpers.ScrollHelper;
import org.minibus.aqa.main.core.helpers.VisibilityHelper;
import org.minibus.aqa.main.domain.data.schedule.DirectionData;
import org.minibus.aqa.main.domain.data.schedule.TripData;
import org.minibus.aqa.main.domain.screens.BaseLoadableScreen;
import org.minibus.aqa.main.domain.screens.cities.CitiesScreen;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.support.CacheLookup;

import java.util.ArrayList;
import java.util.List;

public class ScheduleScreen extends BaseLoadableScreen {

    @AndroidFindBy(id = "tv_toolbar_subtitle")
    private AndroidElement textSubtitle;

    @AndroidFindBy(id = "et_dep_city")
    private AndroidElement fieldDepartureCity;

    @AndroidFindBy(id = "et_arr_city")
    private AndroidElement fieldArrivalCity;

    @AndroidFindBy(id = "ib_swap_direction")
    private AndroidElement btnSwapDirection;

    @AndroidFindBy(id = "btn_sort_by")
    private AndroidElement btnSortBy;

    @AndroidFindBy(id = "fab_route_direction")
    private AndroidElement fabRouteDirection;

    @AndroidFindBy(id = "toolbar")
    private AndroidElement toolbar;

    @AndroidFindBy(id = "container_hud")
    private AndroidElement progressHud;

    @CacheLookup
    private ScheduleCalendarWidget calendar;

    private List<TripWidget> trips;

    private static final String SCREEN_NAME = "Schedule";
    private static final String DEFAULT_DEP_FIELD_VALUE = "From";
    private static final String DEFAULT_ARR_FIELD_VALUE = "To";

    public ScheduleScreen(AndroidDriver<AndroidElement> driver) {
        super(driver, SCREEN_NAME);
    }

    public ScheduleScreen() {
        super(Device.getDriver(), SCREEN_NAME);
    }

    @Override
    public boolean isOpened(int timeoutSec) {
        return VisibilityHelper.isVisible(getTitleElement()) && getTitleElement().getText().equals(SCREEN_NAME);
    }

    @Override
    public boolean isLoading() {
        return VisibilityHelper.isVisible(progressHud, 0) || VisibilityHelper.isVisible(getProgressBar(), 0);
    }

    public AndroidElement getBtnSortBy() {
        return btnSortBy;
    }

    @Step("Wait for Schedule screen to load ({timeoutSec} sec)")
    @Override
    public boolean waitForLoading(int timeoutSec) {
        return VisibilityHelper.areInvisible(List.of(progressHud, getProgressBar()), timeoutSec);
    }

    @Step("Wait for content to load")
    public boolean waitForContentLoading() {
        return VisibilityHelper.isInvisible(getProgressBar(), getScreenTimeout());
    }

    @Step("Click on route direction floating action button")
    public void toggleRouteDirection() {
        fabRouteDirection.click();
    }

    @Step("Swap route direction")
    public void swapRouteDirection() {
        btnSwapDirection.click();
    }

    @Step("Open departure cities screen")
    public CitiesScreen openDepartureCitiesScreen() {
        fieldDepartureCity.click();
        CitiesScreen citiesScreen = new CitiesScreen(CitiesScreen.Type.DEPARTURE);
        citiesScreen.waitForLoading();
        return citiesScreen;
    }

    @Step("Open arrival cities screen")
    public CitiesScreen openArrivalCitiesScreen() {
        fieldArrivalCity.click();
        CitiesScreen citiesScreen = new CitiesScreen(CitiesScreen.Type.ARRIVAL);
        citiesScreen.waitForLoading();
        return citiesScreen;
    }

    public ScheduleCalendarWidget getCalendar() {
        return calendar;
    }

    public DirectionData getDirectionData() {
        return new DirectionData(fieldDepartureCity.getText(), fieldArrivalCity.getText());
    }

    public String getSubtitle() {
        return textSubtitle.getText();
    }

    public SortType getSelectedSortType() {
        String type = StringUtils.substringAfterLast(btnSortBy.getText(), "Sort by");
        return SortType.fromString(type);
    }

    public boolean isDepartureCityFieldEnabled() {
        return fieldDepartureCity.isEnabled();
    }

    public boolean isArrivalCityFieldEnabled() {
        return fieldArrivalCity.isEnabled();
    }

    public boolean isDepartureCitySet() {
        String departureCityValue = fieldDepartureCity.getText();
        return !departureCityValue.isEmpty() && !departureCityValue.equals(DEFAULT_DEP_FIELD_VALUE);
    }

    public boolean isArrivalCitySet() {
        String arrivalCityValue = fieldArrivalCity.getText();
        return !arrivalCityValue.isEmpty() && !arrivalCityValue.equals(DEFAULT_ARR_FIELD_VALUE);
    }

    public boolean isScheduleAvailable() {
        return trips.size() > 0;
    }

    public boolean isRouteDirectionExpanded() {
        return toolbar.getRect().getHeight() != calendar.getWrappedElement().getRect().getY();
    }

    public enum SortType {
        DEPARTURE_TIME("Departure time"),
        ARRIVAL_TIME("Arrival time"),
        CARRIER_RATING("Carrier rating"),
        AVAILABLE_SEATS("Available seats"),
        PRICE("Price");

        private final String type;

        SortType(String type) {
            this.type = type;
        }

        public static SortType fromString(String type) {
            for (SortType sortType : SortType.values()) {
                if (sortType.type.equalsIgnoreCase(type)) {
                    return sortType;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
