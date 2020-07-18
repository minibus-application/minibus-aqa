package org.minibus.aqa.core.common.env;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface Config {

    String DIR = "config";
    String DEFAULT = "N/A";

    Properties getConfig();

    default String initProperty(Enum<?> key, boolean isMandatory) {
        String property = initProperty(key, DEFAULT);

        if (property.equals(DEFAULT) && isMandatory) {
            throw new NotImplementedException(String.format("'%s' property is mandatory and needed to be specified", key.toString()));
        }

        return property;
    }

    default String initProperty(Enum<?> key) {
        return initProperty(key, DEFAULT);
    }

    default String initProperty(Enum<?> key, String def) {
        return initProperty(key.toString(), def);
    }

    default String initProperty(String key) {
        return initProperty(key, DEFAULT);
    }

    default String initProperty(String key, String def) {
        String configProperty = getConfig().getProperty(key, def);
        String property = System.getProperty(key, configProperty);

        if (!property.equals(configProperty) || !configProperty.equals(DEFAULT)) {
            getConfig().setProperty(key, property);
        }

        return property;
    }

    default boolean isDefined(Enum<?> key) {
        return isDefined(key.toString());
    }

    default boolean isDefined(String key) {
        Object value = initProperty(key);
        return value != null && !value.equals(DEFAULT);
    }

    default String stringify() {
        return getConfig().entrySet().stream()
                .filter(o -> !o.getValue().equals(DEFAULT))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .toString()
                .replace(", ", "\n");
    }
}
