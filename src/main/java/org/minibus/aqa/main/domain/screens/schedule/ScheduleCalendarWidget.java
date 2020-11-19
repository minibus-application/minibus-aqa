package org.minibus.aqa.main.domain.screens.schedule;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.Pair;
import org.minibus.aqa.main.domain.screens.BaseWidget;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AndroidFindBy(id = "rv_calendar")
public class ScheduleCalendarWidget extends BaseWidget {

    private List<CalendarDayWidget> calendarDays;

    protected ScheduleCalendarWidget(WebElement element) {
        super(element);
    }

    public int getCapacity() {
        return calendarDays.size();
    }

    @Step("Select {date} in the calendar")
    public void selectDay(final LocalDate date) {
        final LocalDate convertedDate = CalendarDayWidget.toCalendarDate(date);

        OptionalInt opt = IntStream.range(0, calendarDays.size())
                .filter(i -> convertedDate.equals(calendarDays.get(i).getDate(i + 1)))
                .findFirst();

        if (opt.isPresent()) {
            if (isOperationalDay(convertedDate)) {
                CalendarDayWidget calendarDay = calendarDays.get(opt.getAsInt());
                calendarDay.select();
            } else {
                throw new RuntimeException(String.format("Can not select the date that isn't operational: %s\nOperational days: %s",
                        convertedDate.toString(), getOperationalDates().stream().map(LocalDate::toString).collect(Collectors.joining(", "))));
            }
        } else {
            throw new RuntimeException("A such date was not found: " + convertedDate.toString());
        }
    }

    public boolean isDaySelected(final LocalDate date) {
        final LocalDate convertedDate = CalendarDayWidget.toCalendarDate(date);
        return getSelectedDate().equals(convertedDate);
    }

    public boolean isOperationalDay(final LocalDate date) {
        final LocalDate convertedDate = CalendarDayWidget.toCalendarDate(date);
        return getOperationalDates().contains(convertedDate);
    }

    public List<LocalDate> getOperationalDates() {
        LinkedHashMap<Integer, CalendarDayWidget> opCalendarDaysMap = getOperationalCalendarDaysMap();
        return opCalendarDaysMap.entrySet().stream()
                .map(e -> e.getValue().getDate(e.getKey()))
                .collect(Collectors.toList());
    }

    public List<LocalDate> getDates() {
        return IntStream.range(0, calendarDays.size())
                .mapToObj(i -> calendarDays.get(i).getDate(i + 1))
                .collect(Collectors.toList());
    }

    public LocalDate getSelectedDate() {
        Pair<Integer, CalendarDayWidget> calendarDatePair = getSelectedCalendarDayPair();
        return getSelectedCalendarDayPair().getValue().getDate(calendarDatePair.getKey());
    }

    public int getSelectedDatePosition() {
        return getSelectedCalendarDayPair().getKey();
    }

    private List<CalendarDayWidget> getCalendarDays() {
        return calendarDays;
    }

    private List<CalendarDayWidget> getOperationalCalendarDays() {
        return new ArrayList<>(getOperationalCalendarDaysMap().values());
    }

    // get calendar day elements mapped to their position within the calendar
    private LinkedHashMap<Integer, CalendarDayWidget> getCalendarDaysMap() {
        AtomicInteger index = new AtomicInteger();
        return calendarDays.stream().collect(LinkedHashMap::new, (map, val) -> map.put(index.incrementAndGet(), val), Map::putAll);
    }

    // get operational calendar day elements mapped to their position within the calendar
    private LinkedHashMap<Integer, CalendarDayWidget> getOperationalCalendarDaysMap() {
        return getCalendarDaysMap().entrySet().stream()
                .filter(e -> e.getValue().isEnabled())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    // get selected calendar day element paired with its position within the calendar
    private Pair<Integer, CalendarDayWidget> getSelectedCalendarDayPair() {
        LOGGER.debug("Getting selected calendar day pair");

        OptionalInt optIndex = IntStream.range(0, calendarDays.size()).filter(i -> calendarDays.get(i).isSelected()).findFirst();
        if (optIndex.isPresent()) {
            int index = optIndex.getAsInt();
            Pair<Integer, CalendarDayWidget> pair = Pair.of(index + 1, calendarDays.get(index));
            LOGGER.debug(String.format("The pair is (%s, %s)", pair.getKey(), pair.getValue().getDisplayDate()));
            return pair;
        }
        throw new RuntimeException("No selected date found");
    }
}
