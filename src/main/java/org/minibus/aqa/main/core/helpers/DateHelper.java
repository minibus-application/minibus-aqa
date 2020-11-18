package org.minibus.aqa.main.core.helpers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DateHelper {

    public static List<LocalDate> getDaysFromNow(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> LocalDate.now().plus(i, ChronoUnit.DAYS).atStartOfDay().toLocalDate())
                .collect(Collectors.toList());
    }
}
