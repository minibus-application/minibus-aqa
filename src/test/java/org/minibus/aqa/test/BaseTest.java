package org.minibus.aqa.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.AppEnvironment;
import org.minibus.aqa.main.core.handlers.TestAssertion;
import org.minibus.aqa.main.core.handlers.TestInterceptor;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.handlers.TestListener;
import org.minibus.aqa.main.domain.api.helpers.RoutesHelper;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.testng.ITestContext;
import org.testng.annotations.*;


@Listeners({TestInterceptor.class, TestListener.class})
public abstract class BaseTest {
    protected static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
    protected static TestAssertion test = new TestAssertion();

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        LOGGER.debug("System environment variables: {}", System.getenv());

        String passedTestEnv = ConfigManager.getGeneralConfig().environment();
        RestAssured.baseURI = AppEnvironment.STAGE.toString().equals(passedTestEnv)
                ? ConfigManager.getApiConfig().stageBaseUrl()
                : ConfigManager.getApiConfig().debBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @DataProvider(name = "dataProvider_RouteWithNonOperationalDays")
    public Object[] getRouteWithNonOperationalDays() {
        RouteDTO route = RoutesHelper.findAnyRouteWithNonOperationalDays();
        return new Object[] {route};
    }
}
