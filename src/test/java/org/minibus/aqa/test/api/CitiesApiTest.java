package org.minibus.aqa.test.api;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.helpers.RandomHelper;
import org.minibus.aqa.main.domain.api.models.CityDTO;
import org.minibus.aqa.main.domain.api.models.Error;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.minibus.aqa.main.domain.api.URI.CITIES;
import static org.minibus.aqa.main.domain.api.URI.CITIES_EXCLUDE;
import static org.minibus.aqa.main.domain.api.QueryParam.ID;

public class CitiesApiTest extends BaseApiTest {

    @Test(groups = {TestGroup.API},
            description = "When request Cities then OK and size is " + REGION_CITIES_COUNT)
    public void testWhenRequestCitiesThenOk() {
        when().get(CITIES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.is(REGION_CITIES_COUNT));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Cities then OK and json schema matches expected")
    public void testWhenRequestCitiesThenSchemaIsOk() {
        final String citiesSchemaFileName = "cities.json";
        final File citiesJsonSchema = ConfigManager.getJsonSchema(citiesSchemaFileName);

        when().get(CITIES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(citiesJsonSchema));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Cities with exclusion then OK and doesn't respond with excluded city object")
    public void testWhenRequestCitiesWithExclusionThenOk() {
        List<CityDTO> cityResponse = when().get(CITIES).then().extract().jsonPath().getList(".", CityDTO.class);

        Collections.shuffle(new ArrayList<>(cityResponse));
        final CityDTO cityToExclude = cityResponse.get(0);

        given().queryParam(ID, cityToExclude.getId())
                .when().get(CITIES_EXCLUDE)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.is(REGION_CITIES_COUNT - 1))
                .body("$", not(hasItem(cityToExclude)));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Cities with nonexistent city id to exclude then OK and size is " + REGION_CITIES_COUNT)
    public void testWhenRequestCitiesWithNonexistentExclusionThenOk() {
        String nonexistentCityIdToExclude = new ObjectId().toString();

        given().queryParam(ID, nonexistentCityIdToExclude)
                .when().get(CITIES_EXCLUDE)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.is(REGION_CITIES_COUNT));
    }

    @Test(groups = {TestGroup.API},
            description = "When request Cities with invalid city id to exclude then fail")
    public void testWhenRequestCitiesWithInvalidExclusionThenFail() {
        String invalidCityIdToExclude = RandomHelper.getInvalidObjectIdString();

        Error response = given().queryParam(ID, invalidCityIdToExclude)
                .when().get(CITIES_EXCLUDE)
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }
}
