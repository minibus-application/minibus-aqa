package org.minibus.aqa.api;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.minibus.aqa.core.env.config.ConfigManager;
import org.minibus.aqa.domain.models.CityDTO;
import org.minibus.aqa.domain.models.Error;
import org.minibus.aqa.domain.models.RouteDTO;
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

public class RoutesApiTest extends BaseApiTest {

    @Test(description = "When request Routes then OK and size is " + ROUTES_COUNT)
    public void testWhenRequestRoutesThenOk() {
        when().get("/routes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(ROUTES_COUNT));
    }

    @Test(description = "When request Routes then OK and json schema matches expected")
    public void testWhenRequestRoutesThenSchemaIsOk() {
        final File routesJsonSchema = ConfigManager.getJsonSchema("routes.json");
        when().get("/routes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(routesJsonSchema));
    }

    @Test(description = "When request Routes with departure and arrival city id's then OK and has size of 1")
    public void testWhenRequestRoutesWithFilteringThenOk() {
        List<CityDTO> cityResponse = when().get("/cities").then().extract().jsonPath().getList(".", CityDTO.class);

        Collections.shuffle(new ArrayList<>(cityResponse));
        final CityDTO departureCity = cityResponse.get(0);
        final CityDTO arrivalCity = cityResponse.get(cityResponse.size() - 1);

        final File routeJsonSchema = ConfigManager.getJsonSchema("route.json");

        RouteDTO routesResponse = given().queryParam("fromId", departureCity.getId()).queryParam("toId", arrivalCity.getId())
                .when().get("/routes/filterBy")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(routeJsonSchema))
                .extract()
                .as(RouteDTO.class);

        assertThat(routesResponse.getFrom(), equalTo(departureCity));
        assertThat(routesResponse.getTo(), equalTo(arrivalCity));
    }

    @Test(description = "When request Routes with nonexistent departure city id then fail")
    public void testWhenRequestRoutesWithNonexistentDepartureThenFail() {
        List<CityDTO> cityResponse = when().get("/cities").then().extract().jsonPath().getList(".", CityDTO.class);

        final String arrivalCityId = cityResponse.get(0).getId();
        final String nonexistentDepartureCityId = new ObjectId().toHexString();

        Error response = given().queryParam("fromId", nonexistentDepartureCityId).queryParam("toId", arrivalCityId)
                .when().get("/routes/filterBy")
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }

    @Test(description = "When request Routes with nonexistent arrival city id then fail")
    public void testWhenRequestRoutesWithNonexistentArrivalThenFail() {
        List<CityDTO> cityResponse = when().get("/cities").then().extract().jsonPath().getList(".", CityDTO.class);

        final String nonexistentArrivalCityId = new ObjectId().toHexString();
        final String departureCityId = cityResponse.get(0).getId();

        Error response = given().queryParam("fromId", departureCityId).queryParam("toId", nonexistentArrivalCityId)
                .when().get("/routes/filterBy")
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }
}
