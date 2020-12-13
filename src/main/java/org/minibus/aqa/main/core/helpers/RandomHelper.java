package org.minibus.aqa.main.core.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.minibus.aqa.main.Constants;

import java.util.*;
import java.util.stream.Collectors;

public class RandomHelper {
    private static final Logger LOGGER = LogManager.getLogger(RandomHelper.class);

    public static String temp() {
        return String.format("%s%s", Constants.TEMP, UUID.randomUUID().toString().replace("-", ""));
    }

    public static String getInvalidObjectIdString() {
        return RandomStringUtils.random(new ObjectId().toString().length(), true, true);
    }

    public static <T> T getAny(List<T> list) {
        return getAny(list, new ArrayList<>());
    }

    public static <T> T getAny(List<T> list, T... except) {
        return getAny(list, Arrays.asList(except));
    }

    public static <T> T getAny(List<T> list, List<T> except) {
        if (except != null && except.size() > 0) {
            list = list.stream().filter(x -> !except.contains(x)).collect(Collectors.toList());
        }

        LOGGER.trace("Get any from list: {}", except);

        Collections.shuffle(list);
        T res = list.get(0);

        LOGGER.trace("Result is: {}", res);
        return res;
    }
}
