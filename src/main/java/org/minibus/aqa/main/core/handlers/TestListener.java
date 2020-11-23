package org.minibus.aqa.main.core.handlers;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.env.Device;
import org.testng.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class TestListener implements ITestListener, IConfigurationListener {
    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);
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
        LOGGER.info("[TEST START][TEST NAME:{}]", iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        LOGGER.info("[TEST PASSED][DURATION:{}ms]", getDuration(iTestResult.getEndMillis()));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if(!iTestResult.isSuccess()) {
            if (Device.getDriver() != null) {
                byte[] imageBytes = Device.getScreenshot();
                String screenshotFileName = iTestResult.getName() + "_" + Instant.now().toEpochMilli() + ".png";
                File imageFile = new File(Paths.get(Constants.PROJECT_REPORT_SCREENSHOT_FOLDER, screenshotFileName).toUri());
                imageFile.mkdirs();
                try {
                    ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), "png", imageFile);
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        }
        LOGGER.error(iTestResult.getThrowable().getMessage());
        LOGGER.info("[TEST FAILED][DURATION:{}ms]", getDuration(iTestResult.getEndMillis()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOGGER.info("[TEST SKIPPED][DURATION:{}ms]", getDuration(iTestResult.getEndMillis()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        LOGGER.info("[START][FOUND:{}]", iTestContext.getSuite().getXmlSuite().getTests().size());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOGGER.info("[FINISH][FAILED:{}][SUCCEEDED:{}]", iTestContext.getFailedTests().size(), iTestContext.getPassedTests().size());
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

    private static String getDuration(long endTimeMs) {
        long durationMs = startTimeMs == 0 ? startTimeMs : endTimeMs - startTimeMs;
        return DurationFormatUtils.formatDuration(durationMs, "H:mm:ss.SSS");
    }
}
