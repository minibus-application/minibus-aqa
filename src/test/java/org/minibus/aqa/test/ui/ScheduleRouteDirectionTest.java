package org.minibus.aqa.test.ui;

import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.when;

public class ScheduleRouteDirectionTest extends BaseUiTest {

    @Test(groups = {TestGroup.UI}, description = "")
    public void testWhenOpenTheAppAndLoadingCompleteThenRouteDirectionViewAppears() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isRouteDirectionExpanded(), "route direction filter is expanded");
    }

    @Test(groups = {TestGroup.UI}, description = "")
    public void testWhenOpenTheAppAndLoadingCompleteThenRouteDirectionIsSet() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        test.assertTrue(scheduleScreen.isDepartureCitySet(), "departure city is set");
        test.assertTrue(scheduleScreen.isArrivalCitySet(), "arrival city is set");

        String depCityName = scheduleScreen.getDepartureCity();
        String depCityRegion = scheduleScreen.getDepartureCityRegion();
        String arrCityName = scheduleScreen.getArrivalCity();
        String arrCityRegion = scheduleScreen.getArrivalCityRegion();

        List<RouteDTO> routesResponse = when().get("/routes").then().extract().jsonPath().getList("$", RouteDTO.class);
        Optional<RouteDTO> optRoute = routesResponse.stream()
                .filter(r -> (depCityName.equals(r.getFrom().getName()) && depCityRegion.equals(r.getFrom().getRegion()))
                        && (arrCityName.equals(r.getTo().getName()) && arrCityRegion.equals(r.getTo().getRegion())))
                .findFirst();

        test.assertTrue(optRoute.isPresent(), String.format("the route exists: from='%s', to='%s'",
                scheduleScreen.getDeparture(), scheduleScreen.getArrival()));
    }
}
