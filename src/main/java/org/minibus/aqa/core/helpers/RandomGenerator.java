package org.minibus.aqa.core.helpers;

import org.minibus.aqa.Constants;

import java.util.UUID;

public class RandomGenerator {

    public static String temp() {
        return String.format("%s%s", Constants.TEMP, UUID.randomUUID().toString().replace("-", ""));
    }
}
