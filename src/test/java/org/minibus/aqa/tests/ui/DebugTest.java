package org.minibus.aqa.tests.ui;

import org.minibus.aqa.core.common.cli.AdbCommandExecutor;
import org.minibus.aqa.tests.BaseTest;
import org.testng.annotations.Test;


public class DebugTest extends BaseTest {

    @Test
    public void testDebug() {

        System.out.println(AdbCommandExecutor.getDevices(", "));
    }
}
