package org.minibus.aqa.main.core.handlers;

import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.env.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;


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
                File imageFile = new File(Paths.get(Constants.PROJECT_REPORT_SCREENSHOT_FOLDER, screenshotFileName).toUri());
                try {
                    ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), "png", imageFile);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
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
