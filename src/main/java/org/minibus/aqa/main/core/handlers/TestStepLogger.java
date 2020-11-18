package org.minibus.aqa.main.core.handlers;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestStepLogger implements StepLifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestStepLogger.class);

    @Override
    public void beforeStepStart(final StepResult result) {
        LOGGER.info(result.getName());
    }
}
