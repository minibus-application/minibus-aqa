package org.minibus.aqa.domain.screens.schedule;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AndroidFindBy(id = "rv_calendar")
public class ScheduleCalendar extends Widget {

    private List<CalendarDate> calendarDates;

    protected ScheduleCalendar(WebElement element) {
        super(element);
    }

    public void selectDate(LocalDate date) {
        final LocalDate convertedDate = CalendarDate.toCalendarDate(date);

        OptionalInt opt = IntStream.range(0, calendarDates.size())
                .filter(i -> convertedDate.equals(calendarDates.get(i).getDate(i + 1)))
                .findFirst();

        if (opt.isPresent()) {
            if (isOperationalDay(convertedDate)) {
                CalendarDate calendarDate = calendarDates.get(opt.getAsInt());
                calendarDate.select();
            } else {
                throw new RuntimeException(String.format("Can not select the date that isn't operational: %s\nOperational days: %s",
                        convertedDate.toString(), getOperationalDays().stream().map(LocalDate::toString).collect(Collectors.joining(", "))));
            }
        } else {
            throw new RuntimeException("A such date was not found: " + convertedDate.toString());
        }
    }

    public boolean isDateSelected(LocalDate date) {
        final LocalDate convertedDate = CalendarDate.toCalendarDate(date);
        return getSelectedDate().equals(convertedDate);
    }

    public int getCalendarSize() {
        return calendarDates.size();
    }

    public boolean isOperationalDay(LocalDate date) {
        final LocalDate convertedDate = CalendarDate.toCalendarDate(date);
        return getOperationalDays().contains(convertedDate);
    }

    public List<LocalDate> getOperationalDays() {
        List<CalendarDate> opCalendarDays = getOperationalCalendarDays();
        return IntStream.range(0, opCalendarDays.size())
                .mapToObj(i -> calendarDates.get(i).getDate(i + 1))
                .collect(Collectors.toList());
    }

    public List<LocalDate> getDates() {
        return IntStream.range(0, calendarDates.size())
                .mapToObj(i -> calendarDates.get(i).getDate(i + 1))
                .collect(Collectors.toList());
    }

    public LocalDate getSelectedDate() {
        Pair<Integer, CalendarDate> calendarDatePair = getSelectedCalendarDatePair();
        return getSelectedCalendarDatePair().getValue().getDate(calendarDatePair.getKey());
    }

    public int getSelectedCalendarDatePosition() {
        return getSelectedCalendarDatePair().getKey();
    }

    private Pair<Integer, CalendarDate> getSelectedCalendarDatePair() {
        OptionalInt optIndex = IntStream.range(0, calendarDates.size()).filter(i -> calendarDates.get(i).isSelected()).findFirst();
        if (optIndex.isPresent()) {
            int index = optIndex.getAsInt();
            return Pair.of(index + 1, calendarDates.get(index));
        }
        throw new RuntimeException("No selected date found");
    }

    private List<CalendarDate> getOperationalCalendarDays() {
        return calendarDates.stream().filter(CalendarDate::isEnabled).collect(Collectors.toList());
    }
}
