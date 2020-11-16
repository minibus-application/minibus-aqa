package org.minibus.aqa.core.handlers;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import org.minibus.aqa.core.env.Device;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestAssertion extends Assertion {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAssertion.class);
    private static final String PASSED_MSG_PATTERN = "%s [RESULT:%s][PASSED]";

    @Step("Assert: {iAssert.m_message}")
    @Override
    public void onAssertSuccess(IAssert<?> iAssert) {
        String assertionMessage = iAssert.getMessage();

        LOGGER.info(String.format("%s [RESULT:%s][PASSED]", assertionMessage, iAssert.getActual()));

//        Allure.getLifecycle().updateStep(x -> {
//            x.setStatus(Status.PASSED);
//            x.setStatusDetails(new StatusDetails().setMessage(assertionMessage));
//        });

        super.onAssertSuccess(iAssert);
    }

    @Step("Assert: {iAssert.m_message}")
    @Override
    public void onAssertFailure(IAssert<?> iAssert, AssertionError assertionError) {
        String assertionMessage = iAssert.getMessage();

        LOGGER.info(String.format("%s [RESULT:%s][FAILED]", assertionMessage, iAssert.getActual()));

//        pushAttachment(assertionMessage);
//        pushScreenshot(Device.getDriver().getScreenshotAs(OutputType.BASE64));
//
//        Allure.getLifecycle().updateStep(x -> {
//            x.setStatus(Status.FAILED);
//            x.setStatusDetails(new StatusDetails().setMessage(assertionMessage));
//            x.setDescription(assertionError.getMessage());
//        });

        super.onAssertFailure(iAssert, assertionError);
    }

    private static void failAllureStep(Status status, String statusDetails, String description) {
        pushAttachment(statusDetails);
        pushScreenshot(Device.getDriver().getScreenshotAs(OutputType.BASE64));
        updateAllureStep(status, statusDetails, description);
    }

    private static void updateAllureStep(Status status, String statusDetails, String description) {
        Allure.getLifecycle().updateStep(x -> {
            x.setStatus(status);
            x.setStatusDetails(new StatusDetails().setMessage(statusDetails));
            x.setDescription(description);
        });
    }

    @Attachment(value = "Assertion message", type = "text/plain")
    public static byte[] pushAttachment(String text) {
        byte[] decodedString;
        try {
            decodedString = text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ignore) {
            decodedString = text.getBytes(Charset.defaultCharset());
        }
        return decodedString;
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] pushScreenshot(String screenshot) {
        byte[] decodedString;
        try {
            decodedString = Base64.getDecoder().decode(screenshot);
        } catch (Exception ignore) {
            decodedString = Base64.getMimeDecoder().decode(screenshot);
        }
        return decodedString;
    }
}
