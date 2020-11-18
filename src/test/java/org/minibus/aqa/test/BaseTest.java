package org.minibus.aqa.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.minibus.aqa.main.core.env.AppEnvironment;
import org.minibus.aqa.main.core.handlers.TestAssertion;
import org.minibus.aqa.main.core.handlers.TestInterceptor;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.handlers.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.*;


@Listeners(TestInterceptor.class)
public abstract class BaseTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected static TestAssertion test = new TestAssertion();

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        ((TestRunner) context).getSuite().getXmlSuite().setThreadCount(1);
        ((TestRunner) context).addListener(TestListener.getInstance());

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
}
