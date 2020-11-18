package org.minibus.aqa.main.domain.screens.schedule;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.helpers.VisibilityHelper;
import org.minibus.aqa.main.domain.screens.BaseLoadableScreen;
import org.minibus.aqa.main.domain.screens.cities.CitiesScreen;

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

    private ScheduleCalendarWidget calendar;
    private List<RouteTripWidget> routeTrips;

    private static final String SCREEN_NAME = "Schedule";
    private static final String DEF_DEP_FIELD_VAL = "From";
    private static final String DEF_ARR_FIELD_VAL = "To";

    public ScheduleScreen(AppiumDriver<MobileElement> driver) {
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

    @Override
    public boolean waitForLoading(int timeoutSec) {
        return VisibilityHelper.isInvisible(progressHud, timeoutSec) && super.waitForLoading(timeoutSec);
    }

    @Step("Click on route direction floating action button")
    public void toggleRouteDirection() {
        fabRouteDirection.click();
    }

    @Step("Open departure cities screen")
    public CitiesScreen openDepartureCitiesScreen() {
        fieldDepartureCity.click();
        return new CitiesScreen(CitiesScreen.Type.DEPARTURE);
    }

    @Step("Open arrival cities screen")
    public CitiesScreen openArrivalCitiesScreen() {
        fieldArrivalCity.click();
        return new CitiesScreen(CitiesScreen.Type.ARRIVAL);
    }

    public ScheduleCalendarWidget getCalendar() {
        return calendar;
    }

    public String getDeparture() {
        return fieldDepartureCity.getText();
    }

    public String getDepartureCity() {
        return fieldDepartureCity.getText().split(",")[0].trim();
    }

    public String getArrival() {
        return fieldArrivalCity.getText();
    }

    public String getArrivalCity() {
        return fieldArrivalCity.getText().split(",")[0].trim();
    }

    public String getDepartureCityRegion() {
        return fieldDepartureCity.getText().split(",")[1].trim();
    }

    public String getArrivalCityRegion() {
        return fieldArrivalCity.getText().split(",")[1].trim();
    }

    public SortType getSelectedSortType() {
        String type = StringUtils.substringAfterLast(btnSortBy.getText(), "Sort by");
        return SortType.fromString(type);
    }

    public boolean isDepartureCitySet() {
        String departureCityValue = fieldDepartureCity.getText();
        return !departureCityValue.isEmpty() && !departureCityValue.equals(DEF_DEP_FIELD_VAL);
    }

    public boolean isArrivalCitySet() {
        String arrivalCityValue = fieldArrivalCity.getText();
        return !arrivalCityValue.isEmpty() && !arrivalCityValue.equals(DEF_ARR_FIELD_VAL);
    }

    public boolean isScheduleAvailable() {
        return routeTrips.size() > 0;
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
