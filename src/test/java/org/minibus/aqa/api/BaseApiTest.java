package org.minibus.aqa.api;

import org.minibus.aqa.Constants;
import org.minibus.aqa.domain.models.RouteDTO;
import org.minibus.aqa.BaseTest;
import org.testng.SkipException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.when;

public abstract class BaseApiTest extends BaseTest {

    protected final int REGIONS_COUNT = 6;
    protected final int REGION_CITIES_COUNT = REGIONS_COUNT;
    protected final int ROUTES_COUNT = REGIONS_COUNT * (REGIONS_COUNT - 1);
    protected final int TRIPS_PER_ROUTE_COUNT = 15;
    protected final int TRIPS_PER_REGION_COUNT = (REGIONS_COUNT - 1) * TRIPS_PER_ROUTE_COUNT;
    protected final int TRIPS_COUNT = TRIPS_PER_REGION_COUNT *  REGIONS_COUNT;

    protected RouteDTO findOperationalRoute(LocalDate desiredDate, boolean failIfAbsent) {
        List<RouteDTO> routesResponse = when().get("/routes").then().extract().jsonPath().getList("$", RouteDTO.class);
        RouteDTO route = null;
        Optional<RouteDTO> opt = routesResponse.stream()
                .filter(r -> r.getOpDays().contains(desiredDate.getDayOfWeek().getValue()))
                .findFirst();
        if (!opt.isPresent()) {
            if (failIfAbsent) throw new SkipException(String.format("Can't find operating route with %s desired date", desiredDate.toString()));
        } else {
            route = opt.get();
        }
        return route;
    }

    protected List<Integer> getNonOperationalDays(List<Integer> operationalDays) {
        Collections.sort(operationalDays);
        int index = 0;
        List<Integer> nonOperationalDays = new ArrayList<>();
        for (int i = 1; i <= operationalDays.size(); i++) {
            if (i == operationalDays.get(index)) index++;
            else nonOperationalDays.add(i);
        }
        return nonOperationalDays;
    }

    protected static List<LocalDate> getWeekAhead() {
        List<LocalDate> days = new ArrayList<>();
        IntStream.range(0, DayOfWeek.values().length).forEach(i -> {
            LocalDateTime date = LocalDate.now().plus(i, ChronoUnit.DAYS).atStartOfDay();
            days.add(date.toLocalDate());
        });
        return days;
    }

    protected static LocalDate getTodayDate() {
        return getTodayDateTime().toLocalDate();
    }

    protected static LocalDateTime getTodayDateTime() {
        return LocalDateTime.now(ZoneId.of(Constants.APP_TIMEZONE));
    }
}
