package org.minibus.aqa.main.core.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.env.DeviceType;
import org.minibus.aqa.main.core.env.SpecificDevice;
import org.minibus.aqa.main.core.env.config.ConfigManager;
import org.minibus.aqa.main.core.env.config.DeviceGeneralConfig;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestInterceptor implements IMethodInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(TestInterceptor.class);

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        ArrayList<IMethodInstance> methodInstances = new ArrayList<>(methods);
        DeviceGeneralConfig config = ConfigManager.getDeviceGeneralConfig();

        methodInstances = methodInstances.stream()
                .filter(i -> {
                    Method m = i.getMethod().getConstructorOrMethod().getMethod();

                    if (m.isAnnotationPresent(SpecificDevice.class)) {
                        SpecificDevice annotation = m.getAnnotation(SpecificDevice.class);

                        final String[] versions = annotation.versions();
                        final DeviceType[] types = annotation.types();

                        boolean byVersion = versions.length == 0 || Arrays.stream(versions).anyMatch(v -> v.equals(config.platform()));
                        boolean byType = types.length == 0 || List.of(types).contains(config.emulated() ? DeviceType.EMULATOR : DeviceType.PHYSICAL);

                        return byVersion && byType;
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        LOGGER.debug("Tests after filtering: {}", methodInstances.size());

        return methodInstances;
    }
}
