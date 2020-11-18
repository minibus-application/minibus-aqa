package org.minibus.aqa.test.api;

import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpStatus;
import org.minibus.aqa.main.domain.api.models.Error;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.api.models.ScheduleDTO;
import org.minibus.aqa.main.domain.api.models.TripDTO;
import org.minibus.aqa.test.TestGroup;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.minibus.aqa.main.domain.api.QueryParam.ROUTE_ID;
import static org.minibus.aqa.main.domain.api.QueryParam.TRIP_DATE;
import static org.minibus.aqa.main.domain.api.URI.ROUTES;
import static org.minibus.aqa.main.domain.api.URI.SCHEDULE_FILTER_BY;

public class ScheduleApiTest extends BaseApiTest {

    @Test(groups = {TestGroup.API},
            description = "When request today's Schedule then OK and actualized")
    public void testWhenRequestCurrentScheduleThenOkAndRelevant() {
        final LocalDate today = getTodayDate();
        RouteDTO operationalRoute = findOperationalRoute(today, true);

        ScheduleDTO scheduleResponse = given()
                .queryParam(TRIP_DATE, getTodayDate().toString())
                .queryParam(ROUTE_ID, operationalRoute.getId())
                .when().get(SCHEDULE_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getObject("$", ScheduleDTO.class);

        if (scheduleResponse.getTimeline().isEmpty()) {
            throw new SkipException(String.format("The timeline is empty for selected '%s' route and %s date",
                    operationalRoute.getDesc(), today.toString()));
        } else {
            TripDTO trip = scheduleResponse.getTimeline().get(0);
            int hours = Integer.parseInt(trip.getDepartureTime().split(":")[0]);
            int minutes = Integer.parseInt(trip.getDepartureTime().split(":")[1]);

            LocalDateTime currentTime = getTodayDateTime();
            LocalDateTime departureTime = getTodayDateTime().withHour(hours).withMinute(minutes);

            assertThat(departureTime.isAfter(currentTime), is(true));
        }
    }

    @Test(groups = {TestGroup.API},
            description = "When request Schedule with date in future then OK and full")
    public void testWhenRequestScheduleInFutureThenOk() {
        final LocalDate dateInFuture = getTodayDate().plusDays(RandomUtils.nextInt(1, DayOfWeek.values().length));
        RouteDTO operationalRoute = findOperationalRoute(dateInFuture, true);

        ScheduleDTO scheduleResponse = given()
                .queryParam(TRIP_DATE, dateInFuture.toString())
                .queryParam(ROUTE_ID, operationalRoute.getId())
                .when().get(SCHEDULE_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ScheduleDTO.class);

        assertThat(scheduleResponse.getTimeline().size(), is(TRIPS_PER_ROUTE_COUNT));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Schedule with date in past then fail")
    public void testWhenRequestScheduleInPastThenFail() {
        final LocalDate yesterday = getTodayDate().minusDays(1);
        List<RouteDTO> routesResponse = when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);

        Error response = given()
                .queryParam(TRIP_DATE, yesterday.toString())
                .queryParam(ROUTE_ID, routesResponse.get(0).getId())
                .when().get(SCHEDULE_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Schedule at non-operational day then fail")
    public void testWhenRequestScheduleAtNonOperationalDayThenFail() {
        LocalDate nonOperationalDate;

        List<RouteDTO> routesResponse = when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);
        RouteDTO route = routesResponse.stream()
                .filter(r -> r.getOpDays().size() < 7)
                .findFirst()
                .orElse(null);

        if (route == null || getNonOperationalDays(route.getOpDays()).isEmpty()) {
            throw new SkipException("Can't find the route with non-operational day(s)");
        } else {
            List<Integer> nonOperationalDays = getNonOperationalDays(route.getOpDays());
            nonOperationalDate = getWeekAhead().stream()
                    .filter(d -> d.getDayOfWeek().getValue() == nonOperationalDays.get(0))
                    .findFirst()
                    .get(); // always OK

            String routeId = route.getId();

            Error response = given()
                    .queryParam(TRIP_DATE, nonOperationalDate.toString())
                    .queryParam(ROUTE_ID, routeId)
                    .when().get(SCHEDULE_FILTER_BY)
                    .then()
                    .assertThat()
                    .statusCode(not(HttpStatus.SC_OK))
                    .extract().as(Error.class);

            assertThat(response.isSucceeded(), is(false));
            assertThat(response.getMessage(), not(emptyOrNullString()));
        }
    }
}
