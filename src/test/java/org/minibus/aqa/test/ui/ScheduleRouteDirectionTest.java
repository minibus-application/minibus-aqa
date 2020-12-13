package org.minibus.aqa.test.ui;

import org.minibus.aqa.main.core.helpers.AppInteractionsHelper;
import org.minibus.aqa.main.core.helpers.RandomHelper;
import org.minibus.aqa.main.domain.api.helpers.RoutesHelper;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.data.schedule.DirectionData;
import org.minibus.aqa.main.domain.screens.cities.CitiesScreen;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;


public class ScheduleRouteDirectionTest extends BaseUiTest {

    @Test(groups = {TestGroup.UI},
            description = "When the app opens and initial loading completed then route direction view expands")
    public void testWhenInitialLoadingCompletedThenRouteDirectionViewExpands() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");
        test.assertTrue(scheduleScreen.isRouteDirectionExpanded(), "Route direction filter is expanded");
    }

    @Test(groups = {TestGroup.UI},
            description = "When the app opens and initial loading completed then default route direction sets")
    public void testWhenInitialLoadingCompletedThenDefaultRouteDirectionSets() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");
        test.assertTrue(scheduleScreen.isDepartureCitySet(), "Departure city is set");
        test.assertTrue(scheduleScreen.isArrivalCitySet(), "Arrival city is set");

        RouteDTO foundRoute = RoutesHelper.getRouteByDirection(scheduleScreen.getDirectionData());

        test.assertTrue(foundRoute != null, String.format("Default route direction (%s) set up correctly",
                scheduleScreen.getDirectionData().getDirectionDesc()));
    }

    @Test(groups = {TestGroup.UI},
            description = "When user swap route direction then direction swaps")
    public void testWhenSwapRouteDirectionThenDirectionSwaps() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        DirectionData oldDirectionData = scheduleScreen.getDirectionData();

        scheduleScreen.swapRouteDirection();
        scheduleScreen.waitForContentLoading();

        DirectionData newDirectionData = scheduleScreen.getDirectionData();

        test.assertNotEquals(newDirectionData, oldDirectionData, "Direction data is changed");
    }

    @Test(groups = {TestGroup.UI},
            description = "When user changes route direction then corresponding fields get changed")
    public void testWhenRouteDirectionChangesThenCorrespondingFieldsGetChanged() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        RouteDTO route = RandomHelper.getAny(RoutesHelper.getAllRoutesExceptDirection(scheduleScreen.getDirectionData()));

        DirectionData oldDirectionData = scheduleScreen.getDirectionData();

        CitiesScreen departureCitiesScreen = scheduleScreen.openDepartureCitiesScreen();
        departureCitiesScreen.waitForLoading();
        departureCitiesScreen.selectCity(route.getFrom().getName());
        scheduleScreen.waitForContentLoading();

        test.assertEquals(scheduleScreen.getDirectionData().getDepCity(), route.getFrom().getName(), "Departure city is changed");
        test.assertEquals(scheduleScreen.getDirectionData().getArrCity(), oldDirectionData.getArrCity(), "Arrival city stays with the old value");

        CitiesScreen arrivalCitiesScreen = scheduleScreen.openArrivalCitiesScreen();
        arrivalCitiesScreen.waitForLoading();
        arrivalCitiesScreen.selectCity(route.getTo().getName());
        scheduleScreen.waitForContentLoading();

        test.assertEquals(scheduleScreen.getDirectionData().getArrCity(), route.getTo().getName(), "Arrival city is changed");
        test.assertEquals(scheduleScreen.getDirectionData().getDepCity(), route.getFrom().getName(), "Departure city stays with the old value");
    }

    @Test(groups = {TestGroup.UI},
            description = "When user changes route direction then toolbar subtitle displays the same direction value")
    public void testWhenRouteDirectionChangesThenToolbarSubtitleDisplaysActualDirection() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        DirectionData actualDirectionData = scheduleScreen.getDirectionData();
        scheduleScreen.toggleRouteDirection();
        String oldSubtitle = scheduleScreen.getSubtitle();

        test.assertEquals(scheduleScreen.getSubtitle(), actualDirectionData.getDirectionDesc(), "Toolbar subtitle shows an actual direction");

        scheduleScreen.toggleRouteDirection();
        scheduleScreen.swapRouteDirection();
        scheduleScreen.waitForContentLoading();
        actualDirectionData = scheduleScreen.getDirectionData();
        scheduleScreen.toggleRouteDirection();

        test.assertNotEquals(scheduleScreen.getSubtitle(), oldSubtitle, "Toolbar subtitle changes");
        test.assertEquals(scheduleScreen.getSubtitle(), actualDirectionData.getDirectionDesc(), "Toolbar subtitle shows an actual direction");
    }

    @Test(groups = {TestGroup.UI},
            description = "When user changes route direction then the choice restores after reopening the app")
    public void testWhenRouteDirectionChangesAndAppReopensThenChosenDirectionRestores() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isOpened(), scheduleScreen.getScreenName() + " screen is opened");

        RouteDTO route = RandomHelper.getAny(RoutesHelper.getAllRoutesExceptDepCity(scheduleScreen.getDirectionData().getDepCity()));

        CitiesScreen departureCitiesScreen = scheduleScreen.openDepartureCitiesScreen();
        departureCitiesScreen.waitForLoading();
        departureCitiesScreen.selectCity(route.getFrom().getName());
        scheduleScreen.waitForContentLoading();

        CitiesScreen arrivalCitiesScreen = scheduleScreen.openArrivalCitiesScreen();
        arrivalCitiesScreen.waitForLoading();
        arrivalCitiesScreen.selectCity(route.getTo().getName());
        scheduleScreen.waitForContentLoading();

        DirectionData chosenDirectionData = scheduleScreen.getDirectionData();

        test.assertEquals(chosenDirectionData.getDepCity(), route.getFrom().getName(), "Departure city is changed");
        test.assertEquals(chosenDirectionData.getArrCity(), route.getTo().getName(), "Arrival city is changed");

        AppInteractionsHelper.closeAppUnderTest(getDriver());

        test.assertTrue(!AppInteractionsHelper.isAppUnderTestOpened(getDriver()), "The app is closed");

        AppInteractionsHelper.openAppUnderTest(getDriver());

        scheduleScreen.isOpened();
        scheduleScreen.waitForContentLoading();

        DirectionData restoredDirectionData = scheduleScreen.getDirectionData();

        test.assertEquals(restoredDirectionData, chosenDirectionData, "Previously chosen direction data restores");
    }
}
