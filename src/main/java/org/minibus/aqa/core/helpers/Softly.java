package org.minibus.aqa.core.helpers;

import org.minibus.aqa.core.common.env.Device;
import org.minibus.aqa.core.common.handlers.TestLogger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import java.util.Map;


public class Softly extends SoftAssert {

    private static final String FAILURE_MSG_FORMAT = "%s [screenshot:%s]";
    private static final String PASSED_MSG_FORMAT = "%s [PASSED]";
    private static final Map<AssertionError, IAssert<?>> errors = Maps.newLinkedHashMap();

    @Override
    protected void doAssert(IAssert<?> iAssert) {
        onBeforeAssert(iAssert);
        try {
            iAssert.doAssert();
            onAssertSuccess(iAssert);

            TestLogger.get().success(String.format(PASSED_MSG_FORMAT, iAssert.getMessage()));
        } catch (AssertionError e) {
            onAssertFailure(iAssert, e);

            errors.put(e, iAssert);
            String formattedMessage = String.format(FAILURE_MSG_FORMAT, iAssert.getMessage(), Device.getScreenshot().getAbsolutePath());
            TestLogger.get().warning(formattedMessage + e.getMessage());
        } finally {
            onAfterAssert(iAssert);
        }
    }

    public static boolean hasErrors() {
        return errors.size() > 0;
    }

    public static void clearRegisteredErrors() {
        errors.clear();
    }

//    public static boolean verifyTrue(Boolean condition, String message) {
//        try {
//            assertThat(condition).isTrue();
//
//            return true;
//        } catch (AssertionError e) {
//            String formattedMessage = String.format(FAILURE_MSG_FORMAT, message, Device.getScreenshot().getAbsolutePath());
//            TestLogger.get().warning(formattedMessage + e.getMessage());
//            return false;
//        }
//    }
//
//    public static boolean verifyEquals(Object actual, Object expected, String message) {
//        try {
//            assertThat(actual).isEqualTo(expected);
//            TestLogger.get().success(String.format(PASSED_MSG_FORMAT, message));
//            return true;
//        } catch (AssertionError e) {
//            String formattedMessage = String.format(FAILURE_MSG_FORMAT, message, Device.getScreenshot().getAbsolutePath());
//            TestLogger.get().warning(formattedMessage + e.getMessage());
//            return false;
//        }
//    }
}
