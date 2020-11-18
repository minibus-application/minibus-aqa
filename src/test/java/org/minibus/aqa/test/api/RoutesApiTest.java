package org.minibus.aqa.test.api;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.domain.api.models.CityDTO;
import org.minibus.aqa.main.domain.api.models.Error;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.minibus.aqa.main.domain.api.QueryParam.FROM_ID;
import static org.minibus.aqa.main.domain.api.QueryParam.TO_ID;
import static org.minibus.aqa.main.domain.api.URI.*;

public class RoutesApiTest extends BaseApiTest {

    @Test(groups = {TestGroup.API},
            description = "When request Routes then OK and size is " + ROUTES_COUNT)
    public void testWhenRequestRoutesThenOk() {
        when().get(ROUTES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.is(ROUTES_COUNT));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Routes then OK and json schema matches expected")
    public void testWhenRequestRoutesThenSchemaIsOk() {
        final String routesSchemaFileName = "routes.json";
        final File routesJsonSchema = ConfigManager.getJsonSchema(routesSchemaFileName);

        when().get(ROUTES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(routesJsonSchema));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Routes with departure and arrival city id's then OK and has size of 1")
    public void testWhenRequestRoutesWithFilteringThenOk() {
        List<CityDTO> cityResponse = when().get(CITIES).then().extract().jsonPath().getList(".", CityDTO.class);

        Collections.shuffle(new ArrayList<>(cityResponse));
        final CityDTO departureCity = cityResponse.get(0);
        final CityDTO arrivalCity = cityResponse.get(cityResponse.size() - 1);

        final String routeSchemaFileName = "route.json";
        final File routeJsonSchema = ConfigManager.getJsonSchema(routeSchemaFileName);

        RouteDTO routesResponse = given().queryParam(FROM_ID, departureCity.getId()).queryParam(TO_ID, arrivalCity.getId())
                .when().get(ROUTES_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(routeJsonSchema))
                .extract()
                .as(RouteDTO.class);

        assertThat(routesResponse.getFrom(), equalTo(departureCity));
        assertThat(routesResponse.getTo(), equalTo(arrivalCity));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Routes with nonexistent departure city id then fail")
    public void testWhenRequestRoutesWithNonexistentDepartureThenFail() {
        List<CityDTO> cityResponse = when().get(CITIES).then().extract().jsonPath().getList("$", CityDTO.class);

        final String arrivalCityId = cityResponse.get(0).getId();
        final String nonexistentDepartureCityId = new ObjectId().toHexString();

        Error response = given().queryParam(FROM_ID, nonexistentDepartureCityId).queryParam(TO_ID, arrivalCityId)
                .when().get(ROUTES_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Routes with nonexistent arrival city id then fail")
    public void testWhenRequestRoutesWithNonexistentArrivalThenFail() {
        List<CityDTO> cityResponse = when().get(CITIES).then().extract().jsonPath().getList("$", CityDTO.class);

        final String nonexistentArrivalCityId = new ObjectId().toHexString();
        final String departureCityId = cityResponse.get(0).getId();

        Error response = given().queryParam(FROM_ID, departureCityId).queryParam(TO_ID, nonexistentArrivalCityId)
                .when().get(ROUTES_FILTER_BY)
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }
}
