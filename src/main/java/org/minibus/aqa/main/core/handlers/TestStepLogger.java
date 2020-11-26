package org.minibus.aqa.main.core.handlers;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class TestStepLogger implements StepLifecycleListener {
    private static final Logger LOGGER = LogManager.getLogger(TestStepLogger.class);

    @Override
    public void beforeStepStart(final StepResult result) {
        if (Objects.nonNull(result.getStatus()) && !result.getStatus().equals(Status.PASSED)) {
            LOGGER.error(result.getName());
            return;
        }
        LOGGER.info(result.getName());
    }
}
