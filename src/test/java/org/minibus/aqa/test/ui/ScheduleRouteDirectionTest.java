package org.minibus.aqa.test.ui;

import org.minibus.aqa.main.core.helpers.RandomHelper;
import org.minibus.aqa.main.domain.api.helpers.RoutesHelper;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.screens.cities.CitiesScreen;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;


public class ScheduleRouteDirectionTest extends BaseUiTest {

    @Test(priority = 1, groups = {TestGroup.UI},
            description = "When the app opens and initial loading completed then route direction view expands")
    public void testWhenInitialLoadingCompletedThenRouteDirectionViewExpands() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");
        test.assertTrue(scheduleScreen.isRouteDirectionExpanded(), "Route direction filter is expanded");
    }

    @Test(priority = 2, groups = {TestGroup.UI},
            description = "When the app opens and initial loading completed then default route direction sets")
    public void testWhenInitialLoadingCompletedThenDefaultRouteDirectionSets() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");
        test.assertTrue(scheduleScreen.isDepartureCitySet(), "Departure city is set");
        test.assertTrue(scheduleScreen.isArrivalCitySet(), "Arrival city is set");

        RouteDTO foundRoute = RoutesHelper.getRouteByDirection(scheduleScreen.getDirectionData());

        test.assertTrue(foundRoute != null, String.format("Default route direction (%s) set up correctly",
                scheduleScreen.getDirectionData().getDirectionDescription()));
    }

    @Test(priority = 3, groups = {TestGroup.UI},
            description = "When user swap route direction then direction swaps")
    public void testWhenSwapRouteDirectionThenDirectionSwaps() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        String oldDeparture = scheduleScreen.getDirectionData().getDepartureFieldValue();
        String oldArrival = scheduleScreen.getDirectionData().getArrivalFieldValue();

        scheduleScreen.swapRouteDirection();
        scheduleScreen.waitForContentUpdating();

        String newDeparture = scheduleScreen.getDirectionData().getDepartureFieldValue();
        String newArrival = scheduleScreen.getDirectionData().getArrivalFieldValue();

        test.assertEquals(newDeparture, oldArrival, "Departure city field value is changed");
        test.assertEquals(newArrival, oldDeparture, "Arrival city field value is changed");
    }

    @Test(priority = 4, groups = {TestGroup.UI},
            description = "When user changes route direction then corresponding fields get changed")
    public void testWhenRouteDirectionChangesThenCorrespondingFieldsGetChanged() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        RouteDTO foundRoute = RandomHelper.getAny(RoutesHelper.getAllRoutesExceptDirection(scheduleScreen.getDirectionData()));

        CitiesScreen departureCitiesScreen = scheduleScreen.openDepartureCitiesScreen();
        departureCitiesScreen.selectCity(foundRoute.getFrom().getName());
        scheduleScreen.waitForContentUpdating();

        test.assertEquals(scheduleScreen.getDirectionData().getDepartureCity(), foundRoute.getFrom().getName(), "Departure city is changed");

        CitiesScreen arrivalCitiesScreen = scheduleScreen.openArrivalCitiesScreen();
        arrivalCitiesScreen.selectCity(foundRoute.getTo().getName());
        scheduleScreen.waitForContentUpdating();

        test.assertEquals(scheduleScreen.getDirectionData().getArrivalCity(), foundRoute.getTo().getName(), "Arrival city is changed");
    }
}
