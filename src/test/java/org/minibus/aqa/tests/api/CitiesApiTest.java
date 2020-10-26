package org.minibus.aqa.tests.api;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;
import org.minibus.aqa.core.common.env.config.ConfigManager;
import org.minibus.aqa.core.helpers.RandomHelper;
import org.minibus.aqa.domain.api.models.CityDTO;
import org.minibus.aqa.domain.api.models.Error;
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

public class CitiesApiTest extends BaseApiTest {

    @Test(description = "When request Cities then OK and size is " + REGION_CITIES_COUNT)
    public void testWhenRequestCitiesThenOk() {
        when().get("/cities")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(REGION_CITIES_COUNT));
    }

    @Test(description = "When request Cities then OK and json schema matches expected")
    public void testWhenRequestCitiesThenSchemaIsOk() {
        final File citiesJsonSchema = ConfigManager.getJsonSchema("cities.json");
        when().get("/cities")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(citiesJsonSchema));
    }

    @Test(description = "When request Cities with exclusion then OK and doesn't respond with excluded city object")
    public void testWhenRequestCitiesWithExclusionThenOk() {
        List<CityDTO> cityResponse = when().get("/cities").then().extract().jsonPath().getList(".", CityDTO.class);

        Collections.shuffle(new ArrayList<>(cityResponse));
        final CityDTO cityToExclude = cityResponse.get(0);

        given().queryParam("id", cityToExclude.getId())
                .when().get("/cities/exclude")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(REGION_CITIES_COUNT - 1))
                .body("$", not(hasItem(cityToExclude)));
    }

    @Test(description = "When request Cities with nonexistent city id to exclude then OK and size is " + REGION_CITIES_COUNT)
    public void testWhenRequestCitiesWithNonexistentExclusionThenOk() {
        String nonexistentCityIdToExclude = new ObjectId().toString();

        given().queryParam("id", nonexistentCityIdToExclude)
                .when().get("/cities/exclude")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(REGION_CITIES_COUNT));
    }

    @Test(description = "When request Cities with invalid city id to exclude then fail")
    public void testWhenRequestCitiesWithInvalidExclusionThenFail() {
        String invalidCityIdToExclude = RandomHelper.getInvalidObjectIdString();

        Error response = given().queryParam("id", invalidCityIdToExclude)
                .when().get("/cities/exclude")
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.SC_OK))
                .extract().as(Error.class);

        assertThat(response.isSucceeded(), is(false));
        assertThat(response.getMessage(), not(emptyOrNullString()));
    }
}
