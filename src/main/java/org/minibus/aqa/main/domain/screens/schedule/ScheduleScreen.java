package org.minibus.aqa.main.domain.screens.schedule;

import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidView;
import org.minibus.aqa.main.core.pagefactory.elements.ButtonView;
import org.minibus.aqa.main.core.pagefactory.elements.TextView;
import org.minibus.aqa.main.domain.data.schedule.DirectionData;
import org.minibus.aqa.main.domain.screens.BaseLoadableScreen;
import org.minibus.aqa.main.domain.screens.cities.CitiesScreen;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDateTime;


public class ScheduleScreen extends BaseLoadableScreen {

    @ViewInfo(name = "Toolbar subtitle", findBy = @FindBy(id = "tv_toolbar_subtitle"))
    private TextView textSubtitle;

    @ViewInfo(name = "Departure city field", findBy = @FindBy(id = "et_dep_city"))
    private AndroidView fieldDepartureCity;

    @ViewInfo(name = "Arrival city field", findBy = @FindBy(id = "et_arr_city"))
    private AndroidView fieldArrivalCity;

    @ViewInfo(name = "Swap direction button", findBy = @FindBy(id = "ib_swap_direction"))
    private ButtonView buttonSwapDirection;

    @ViewInfo(name = "Sort By button", findBy = @FindBy(id = "btn_sort_by"))
    private ButtonView buttonSortBy;

    @ViewInfo(name = "Direction button", findBy = @FindBy(id = "fab_route_direction"))
    private ButtonView fabDirection;

    @ViewInfo(name = "Toolbar", findBy = @FindBy(id = "toolbar"))
    private AndroidView toolbar;

    @ViewInfo(name = "Progress dialog", findBy = @FindBy(id = "container_hud"))
    private AndroidView progressHud;

    @ViewInfo(name = "Schedule calendar layout", findBy = @FindBy(id = "rv_calendar"))
    private CalendarLayout calendarLayout;

    private static final String SCREEN_NAME = "Schedule";
    private static final String DEFAULT_DEP_FIELD_VALUE = "From";
    private static final String DEFAULT_ARR_FIELD_VALUE = "To";

    public ScheduleScreen() {
        super(Device.getDriver(), SCREEN_NAME);
    }

    @Override
    public boolean isOpened(int timeoutSec) {
        return getTitleElement().exists() && getTitleElement().getText().equals(SCREEN_NAME);
    }

    @Override
    public boolean isLoading() {
        return super.isLoading() || progressHud.exists();
    }

    @Override
    public boolean waitForLoading(int timeoutSec) {
        return super.waitForLoading(timeoutSec) && progressHud.waitUntilInvisible(timeoutSec);
    }

    @Step("Wait for content to load")
    public boolean waitForContentLoading() {
        return getProgressBar().waitUntilInvisible(getScreenTimeout());
    }

    @Step("Click on route direction floating action button")
    public void toggleRouteDirection() {
        fabDirection.tap();
        textSubtitle.waitUntilVisible();
    }

    @Step("Swap route direction")
    public void swapRouteDirection() {
        buttonSwapDirection.tap();
    }

    @Step("Open departure cities screen")
    public CitiesScreen openDepartureCitiesScreen() {
        fieldDepartureCity.tap();
        CitiesScreen citiesScreen = new CitiesScreen(CitiesScreen.Type.DEPARTURE);
        citiesScreen.waitForLoading();
        return citiesScreen;
    }

    @Step("Open arrival cities screen")
    public CitiesScreen openArrivalCitiesScreen() {
        fieldArrivalCity.tap();
        CitiesScreen citiesScreen = new CitiesScreen(CitiesScreen.Type.ARRIVAL);
        citiesScreen.waitForLoading();
        return citiesScreen;
    }

    public CalendarLayout getCalendar() {
        return calendarLayout;
    }

    public DirectionData getDirectionData() {
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(getElementTimeout());
        do {
            if (isRouteDirectionExpanded()) break;
        } while(LocalDateTime.now().isBefore(endTime));

        return new DirectionData(fieldDepartureCity.getText(), fieldArrivalCity.getText());
    }

    public String getSubtitle() {
        return textSubtitle.getText();
    }

    public SortType getSelectedSortType() {
        String type = StringUtils.substringAfterLast(buttonSortBy.getText(), "Sort by");
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

    public boolean isRouteDirectionExpanded() {
        return toolbar.getRect().getHeight() != calendarLayout.getWrappedElement().getRect().getY();
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
