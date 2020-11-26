package org.minibus.aqa.main.core.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.minibus.aqa.main.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
        LOGGER.debug("Get any from list: {}", list);

        T res = list.get(new Random().nextInt(list.size()));

        LOGGER.debug("Result is: {}", res);
        return res;
    }

    public static <T> T getAny(List<T> list, T... except) {
        return getAny(list, Arrays.asList(except));
    }

    public static <T> T getAny(List<T> list, List<T> except) {
        LOGGER.debug("Get any from list: {}, except: {}", list, except);

        T res = list.stream()
                .filter(x -> except == null || !except.contains(x))
                .collect(Collectors.toList())
                .get(new Random().nextInt(list.size() - (except == null ? 0 : except.size())));

        LOGGER.debug("Result is: {}", res);
        return res;
    }
}
