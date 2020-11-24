package org.minibus.aqa.main.domain.api.helpers;

import org.minibus.aqa.main.domain.api.URI;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.testng.SkipException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.when;
import static org.minibus.aqa.main.domain.api.URI.ROUTES;

public class RoutesHelper {

    public static RouteDTO findOperationalRoute(LocalDate desiredDate, boolean skipIfAbsent) {
        List<RouteDTO> routesResponse = when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);
        RouteDTO route = null;
        Optional<RouteDTO> opt = routesResponse.stream()
                .filter(r -> r.getOpDays().contains(desiredDate.getDayOfWeek().getValue()))
                .findFirst();
        if (!opt.isPresent()) {
            if (skipIfAbsent) throw new SkipException(String.format("Can't find operating route with %s desired date", desiredDate.toString()));
        } else {
            route = opt.get();
        }
        return route;
    }

    public static RouteDTO findAnyRouteWithNonOperationalDays() {
        List<RouteDTO> routesResponse = when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);
        return routesResponse.stream()
                .filter(route -> route.getOpDays().size() < DayOfWeek.values().length)
                .findFirst()
                .orElseThrow(() -> {
                    throw new NullPointerException("The Route with non operational days was not found");
                });
    }
}
