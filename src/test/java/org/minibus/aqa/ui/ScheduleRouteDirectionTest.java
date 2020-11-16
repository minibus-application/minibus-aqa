package org.minibus.aqa.ui;

import org.assertj.core.api.Assertions;
import org.minibus.aqa.domain.models.RouteDTO;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.when;


public class ScheduleRouteDirectionTest extends BaseScheduleTest {

    @Test(description = "")
    public void testWhenOpenTheAppAndLoadingCompleteThenRouteDirectionViewAppears() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        Assertions.assertThat(scheduleScreen.isRouteDirectionExpanded())
                .as("'Route Direction' expands").isTrue();
    }

    @Test(description = "")
    public void testWhenOpenTheAppAndLoadingCompleteThenRouteDirectionIsSet() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        Assertions.assertThat(scheduleScreen.isDepartureCitySet())
                .as("Departure city is set").isTrue();
        Assertions.assertThat(scheduleScreen.isArrivalCitySet())
                .as("Arrival city is set").isTrue();

        String depCityName = scheduleScreen.getDepartureCity();
        String depCityRegion = scheduleScreen.getDepartureCityRegion();
        String arrCityName = scheduleScreen.getArrivalCity();
        String arrCityRegion = scheduleScreen.getArrivalCityRegion();

        List<RouteDTO> routesResponse = when().get("/routes").then().extract().jsonPath().getList("$", RouteDTO.class);
        Optional<RouteDTO> optRoute = routesResponse.stream()
                .filter(r -> (depCityName.equals(r.getFrom().getName()) && depCityRegion.equals(r.getFrom().getRegion()))
                        && (arrCityName.equals(r.getTo().getName()) && arrCityRegion.equals(r.getTo().getRegion())))
                .findFirst();

        Assertions.assertThat(optRoute.isPresent())
                .as(String.format("Route exists: from=%s, to=%s", scheduleScreen.getDeparture(), scheduleScreen.getArrival())).isTrue();
    }
}
