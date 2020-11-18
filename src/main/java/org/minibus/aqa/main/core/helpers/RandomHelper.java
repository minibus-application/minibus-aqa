package org.minibus.aqa.main.core.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.minibus.aqa.main.Constants;

import java.util.UUID;

public class RandomHelper {

    public static String temp() {
        return String.format("%s%s", Constants.TEMP, UUID.randomUUID().toString().replace("-", ""));
    }

    public static String getInvalidObjectIdString() {
        return RandomStringUtils.random(new ObjectId().toString().length(), true, true);
    }
}
