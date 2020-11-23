package org.minibus.aqa.main.core.helpers;

import org.minibus.aqa.main.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

//    public static LocalDate toDate(LocalDate date, String pattern) {
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Constants.APP_LOCALE);
//        String formatted = date.format(formatter);
//        return LocalDate.parse(formatted, formatter);
//    }
}
