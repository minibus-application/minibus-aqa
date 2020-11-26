package org.minibus.aqa.main.core.handlers;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.openqa.selenium.OutputType;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import java.beans.Introspector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class TestAssertion extends Assertion {
    public static final String ASSERTION_PREFIX = "Assert that";

    @Override
    protected void doAssert(IAssert<?> iAssert) {
        super.onBeforeAssert(iAssert);

        try {
            super.executeAssert(iAssert);
            this.onAssertSuccess(iAssert);
        } catch (AssertionError assertionError) {
            this.onAssertFailure(iAssert, assertionError);

            String throwableDetailedMessage = String.format("%s() -> expected was [%s], but found [%s]",
                    iAssert.getClass().getEnclosingMethod().getName(),
                    iAssert.getActual(),
                    iAssert.getExpected());

            throw new AssertionError(throwableDetailedMessage, assertionError.getCause());
        } finally {
            super.onAfterAssert(iAssert);
        }
    }

    @Override
    public void onAssertSuccess(IAssert<?> iAssert) {
        String assertionMessage = iAssert.getMessage();

        if (assertionMessage != null) {
            Allure.step(String.format("%s [ACTUAL:%s]", getFormattedMessage(assertionMessage), iAssert.getActual()), Status.PASSED);

            Allure.getLifecycle().updateStep(x -> {
                x.setStatusDetails(new StatusDetails().setMessage(assertionMessage));
            });
        }

        super.onAssertSuccess(iAssert);
    }

    @Override
    public void onAssertFailure(IAssert<?> iAssert, AssertionError assertionError) {
        String assertionMessage = iAssert.getMessage() == null ? assertionError.getMessage() : iAssert.getMessage();

        Allure.step(getFormattedMessage(assertionMessage), Status.FAILED);

        pushAttachment(assertionError.getMessage());
        pushScreenshot(Device.getDriver().getScreenshotAs(OutputType.BASE64));

        Allure.getLifecycle().updateStep(x -> {
            x.setStatusDetails(new StatusDetails().setMessage(assertionMessage));
            x.setDescription(assertionError.getMessage());
        });

        super.onAssertFailure(iAssert, assertionError); // 2nd arg is actually a stub, see doAssert instead
    }

    private String getFormattedMessage(String userErrorMessage) {
        return String.format("%s %s", ASSERTION_PREFIX, Introspector.decapitalize(userErrorMessage));
    }

    @Attachment(value = "Assertion message", type = "text/plain")
    public byte[] pushAttachment(String text) {
        byte[] decodedString;
        try {
            decodedString = text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ignore) {
            decodedString = text.getBytes(Charset.defaultCharset());
        }
        return decodedString;
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] pushScreenshot(String screenshot) {
        byte[] decodedString;
        try {
            decodedString = Base64.getDecoder().decode(screenshot);
        } catch (Exception ignore) {
            decodedString = Base64.getMimeDecoder().decode(screenshot);
        }
        return decodedString;
    }
}
