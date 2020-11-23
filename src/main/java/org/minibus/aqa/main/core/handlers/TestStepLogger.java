package org.minibus.aqa.main.core.handlers;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestStepLogger implements StepLifecycleListener {
    private static final Logger LOGGER = LogManager.getLogger(TestStepLogger.class);

    @Override
    public void beforeStepStart(final StepResult result) {
        LOGGER.info(result.getName());
    }
}
