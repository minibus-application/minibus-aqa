package org.minibus.aqa.main.core.helpers;

import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.Device;

import java.util.Arrays;
import java.util.Map;

final public class MobileCommandHelper {

    @Step("Execute adb shell {cmd} {args}")
    public static String shell(String cmd, String... args) {
        Map<String, Object> var = ImmutableMap.of("command", cmd, "args", Arrays.asList(args));
        return (String) Device.getDriver().executeScript("mobile: shell", var);
    }
}
