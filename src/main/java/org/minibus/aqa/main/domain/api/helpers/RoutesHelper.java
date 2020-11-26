package org.minibus.aqa.main.domain.api.helpers;

import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.data.schedule.DirectionData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.when;
import static org.minibus.aqa.main.domain.api.URI.ROUTES;

public class RoutesHelper {

    public static List<RouteDTO> getAllRoutes() {
        return when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);
    }

    public static List<RouteDTO> getAllRoutesByDepartureCity(String depCity) {
        return getAllRoutes().stream()
                .filter(r -> depCity.equals(r.getFrom().getName()))
                .collect(Collectors.toList());
    }

    public static List<RouteDTO> getAllRoutesByArrivalCity(String arrCity) {
        return getAllRoutes().stream()
                .filter(r -> arrCity.equals(r.getTo().getName()))
                .collect(Collectors.toList());
    }
    
    public static List<RouteDTO> getAllRoutesExceptDirection(DirectionData direction) {
        return getAllRoutes().stream()
                .filter(r -> {
                    return !direction.getDepartureCity().equals(r.getFrom().getName())
                            && !direction.getArrivalCity().equals(r.getTo().getName());
                })
                .collect(Collectors.toList());
    }

    public static RouteDTO getRouteByDirection(DirectionData direction) {
        return getAllRoutes().stream()
                .filter(r -> {
                    return direction.getDepartureCity().equals(r.getFrom().getName())
                            && direction.getArrivalCity().equals(r.getTo().getName());
                })
                .findFirst()
                .orElse(null);
    }

    public static RouteDTO getRouteByOperationalDate(LocalDate desiredDate) {
        return getAllRoutes().stream()
                .filter(r -> r.getOpDays().contains(desiredDate.getDayOfWeek().getValue()))
                .findFirst()
                .orElse(null);
    }

    public static RouteDTO getAnyRouteWithNonOperationalDays() {
        return getAllRoutes().stream()
                .filter(route -> route.getOpDays().size() < DayOfWeek.values().length)
                .findFirst()
                .orElseThrow(() -> {
                    throw new NullPointerException("The Route with non operational days was not found");
                });
    }
}
