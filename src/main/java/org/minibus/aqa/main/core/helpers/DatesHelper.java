package org.minibus.aqa.main.core.helpers;

import org.minibus.aqa.main.Constants;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DatesHelper {

    public static List<LocalDate> getDaysFromNow(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> LocalDate.now().plus(i, ChronoUnit.DAYS).atStartOfDay().toLocalDate())
                .collect(Collectors.toList());
    }

    protected static List<LocalDate> getDaysWeekAhead() {
        return getDaysFromNow(DayOfWeek.values().length);
    }

    public static LocalDate fromDayOfWeekToDate(DayOfWeek dow) {
        return getDaysWeekAhead().stream().filter(d -> d.getDayOfWeek().equals(dow)).findFirst().get();
    }

    public static List<LocalDate> fromDaysOfWeekToDates(List<DayOfWeek> dow) {
        return getDaysWeekAhead().stream().filter(d -> dow.contains(d.getDayOfWeek())).collect(Collectors.toList());
    }

    public static List<LocalDate> fromDayOfWeekValuesToDates(List<Integer> dowValues) {
        return fromDaysOfWeekToDates(parseDaysOfWeek(dowValues));
    }

    public static List<DayOfWeek> parseDaysOfWeek(List<Integer> dowValues) {
        return dowValues.stream().map(v -> {
            try {
                return DayOfWeek.of(v);
            } catch (DateTimeException ignore) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<DayOfWeek> extractMissingDaysOfWeek(List<Integer> dowValues) {
        List<DayOfWeek> parsedDaysOfWeek = parseDaysOfWeek(dowValues);
        ArrayList<DayOfWeek> result = new ArrayList<>(Arrays.asList(DayOfWeek.values()));
        result.removeAll(parsedDaysOfWeek);
        return result;
    }

    public static LocalDate toDate(LocalDate date, String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Constants.APP_LOCALE);
        String formatted = date.format(formatter);
        return LocalDate.parse(formatted, formatter);
    }
}
