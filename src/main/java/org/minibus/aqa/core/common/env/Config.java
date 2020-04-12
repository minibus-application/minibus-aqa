package org.minibus.aqa.core.common.env;

import org.minibus.aqa.Constants;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface Config {

    String FILE_POSTFIX = "_config";

    default Map<String, String> getPropertiesMap(Properties properties) {
        return properties.entrySet().stream()
                .collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()));
    }

    default String initProperty(Enum<?> key, boolean isMandatory) {
        String property = initProperty(key, Constants.NOT_ASSIGNED);

        if (property.equals(Constants.NOT_ASSIGNED) && isMandatory) {
            throw new RuntimeException(key.toString() + " property is mandatory");
        }

        return property;
    }

    default String initProperty(Enum<?> key) {
        return initProperty(key, Constants.NOT_ASSIGNED);
    }

    default String initProperty(Enum<?> key, String def) {
        String configProperty = getConfig().getProperty(key.toString(), def);
        String property = System.getProperty(key.toString(), configProperty);
        if (!property.equals(configProperty)) getConfig().setProperty(key.toString(), property);

        return property;
    }

    Properties getConfig();
}
