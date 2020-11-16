package org.minibus.aqa.core.handlers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.env.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class TestListener implements ITestListener, IConfigurationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);
    private static long startTimeMs;
    private static TestListener instance;

    public TestListener() {
    }

    public static TestListener getInstance() {
        if (instance == null) {
            instance = new TestListener();
        }
        return instance;
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        startTimeMs = iTestResult.getStartMillis();
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        LOGGER.info(String.format("[TEST PASSED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if(!iTestResult.isSuccess()) {
            if (Device.getDriver() != null) {
                byte[] imageBytes = Device.getScreenshot();
                String screenshotFileName = iTestResult.getName() + "_" + Instant.now().toEpochMilli() + ".png";

                try {
                    Files.write(Paths.get(Constants.PROJECT_REPORT_SCREENSHOT_FOLDER, screenshotFileName), imageBytes);
                    // Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        LOGGER.info(String.format("[TEST FAILED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOGGER.info(String.format("[TEST SKIPPED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        LOGGER.info(String.format("[TESTING HAS STARTED][TESTS FOUND: %d]",
                iTestContext.getSuite().getXmlSuite().getTests().size()));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOGGER.info(String.format("[TESTING HAS FINISHED][FAILED: %d][SUCCEEDED: %d]",
                iTestContext.getFailedTests().size(), iTestContext.getPassedTests().size()));
    }

    @Override
    public void onConfigurationSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        iTestResult.setStatus(ITestResult.FAILURE);
        onTestFailure(iTestResult);
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {

    }

    private static long getDuration(long endTimeMs) {
        return startTimeMs == 0 ? startTimeMs : endTimeMs - startTimeMs;
    }
}
