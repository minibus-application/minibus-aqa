package org.minibus.aqa.domain.screens.schedule;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.domain.screens.BaseScreen;

import java.util.List;

public class ScheduleScreen extends BaseScreen {

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

    private ScheduleCalendar calendar;
    private List<RouteTrip> routeTrips;

    private static final String SCREEN_NAME = "Schedule";
    private static final String DEFAULT_DEPARTURE_FIELD_VAL = "From";
    private static final String DEFAULT_ARRIVAL_FIELD_VAL = "To";

    public ScheduleScreen(AppiumDriver<MobileElement> driver) {
        super(driver, SCREEN_NAME);
    }

    @Override
    public boolean isOpened(int timeoutSec) {
        return waitForLoading(timeoutSec);
    }

    public String getDepartureCity() {
        return fieldDepartureCity.getText().split(",")[0].trim();
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

    public boolean isDepartureCitySelected() {
        return !fieldDepartureCity.getText().isEmpty() && !fieldDepartureCity.getText().equals(DEFAULT_DEPARTURE_FIELD_VAL);
    }

    public boolean isArrivalCitySelected() {
        return !fieldArrivalCity.getText().isEmpty() && !fieldArrivalCity.getText().equals(DEFAULT_ARRIVAL_FIELD_VAL);
    }

    public boolean isScheduleAvailable() {
        return routeTrips.size() > 0;
    }

    public SortType getSelectedSortType() {
        String type = StringUtils.substringAfterLast(btnSortBy.getText(), "Sort by");
        return SortType.fromString(type);
    }

    public ScheduleCalendar getCalendar() {
        return calendar;
    }
}
