package org.minibus.aqa.core.common.handlers;

import org.minibus.aqa.core.common.env.DeviceConfig;
import org.minibus.aqa.core.common.env.Environment;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TestInterceptor implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        List<IMethodInstance> methodInstances = new LinkedList<>(methods);
        DeviceConfig config = Environment.getInstance().getDeviceConfig();

        return methodInstances.stream()
                .filter(i -> {
                    Method m = i.getMethod().getConstructorOrMethod().getMethod();

                    if (m.isAnnotationPresent(SpecificDevice.class)) {
                        SpecificDevice annotation = m.getAnnotation(SpecificDevice.class);

                        final String[] versions = annotation.versions();
                        final DeviceType[] types = annotation.types();

                        boolean byVersion = versions.length == 0 || Arrays.stream(versions).anyMatch(v -> v.equals(config.getPlatform()));
                        boolean byType = types.length == 0 || List.of(types).contains(config.isEmulated() ? DeviceType.EMULATOR : DeviceType.PHYSICAL);

                        return byVersion && byType;
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }
}
