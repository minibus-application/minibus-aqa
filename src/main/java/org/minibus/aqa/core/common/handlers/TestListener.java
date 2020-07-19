package org.minibus.aqa.core.common.handlers;

import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, IConfigurationListener {

    private static long startTimeMs;
    private static TestListener instance;

    public TestListener() {

    }

    public static final TestListener getInstance() {
        if (instance == null) {
            instance = new TestListener();
        }
        return instance;
    }

    private static long getDuration(long endTimeMs) {
        return endTimeMs - startTimeMs;
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        startTimeMs = iTestResult.getStartMillis();
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        TestLogger.get().info(String.format("[TEST PASSED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        TestLogger.get().info(String.format("[TEST FAILED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        TestLogger.get().info(String.format("[TEST SKIPPED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

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
}
