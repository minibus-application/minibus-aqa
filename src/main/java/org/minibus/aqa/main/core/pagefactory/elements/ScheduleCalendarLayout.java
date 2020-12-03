package org.minibus.aqa.main.core.page_factory.elements;

import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.Pair;
import org.minibus.aqa.main.core.page_factory.locators.ViewFindBy;
import org.minibus.aqa.main.domain.screens.schedule.CalendarDayWidget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScheduleCalendarLayout extends AndroidLayout {

    @ViewFindBy(name = "Calendar date", findBy = @FindBy(id = "ll_date_container"))
    private List<CalendarDayLayout> calendarDays;

    public static final int DEFAULT_SELECTED_DATE_POSITION = 1;

    public ScheduleCalendarLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    @Step("Select {date} in the calendar")
    public void selectDay(final LocalDate date) {
        OptionalInt opt = IntStream.range(0, calendarDays.size())
                .filter(i -> date.equals(calendarDays.get(i).getDate(i + 1)))
                .findFirst();

        if (opt.isPresent()) {
            CalendarDayLayout calendarDay = calendarDays.get(opt.getAsInt());
            calendarDay.tap();
        } else {
            throw new RuntimeException("Such date was not found: " + date);
        }
    }

    public boolean isDaySelected(final LocalDate date) {
        return getSelectedDate().equals(date);
    }

    public boolean isOperationalDay(final LocalDate date) {
        return getOperationalDates().contains(date);
    }

    public List<LocalDate> getOperationalDates() {
        LinkedHashMap<Integer, CalendarDayLayout> opCalendarDaysMap = getOperationalCalendarDaysMap();
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
        Pair<Integer, CalendarDayLayout> calendarDatePair = getSelectedCalendarDayPair();
        return getSelectedCalendarDayPair().getValue().getDate(calendarDatePair.getKey());
    }

    public int getSelectedDatePosition() {
        return getSelectedCalendarDayPair().getKey();
    }

    public int getSelectedDatesCount() {
        return (int) calendarDays.stream().filter(CalendarDayLayout::isSelected).count();
    }

    private List<CalendarDayLayout> getCalendarDays() {
        return calendarDays;
    }

    private List<CalendarDayLayout> getOperationalCalendarDays() {
        return new ArrayList<>(getOperationalCalendarDaysMap().values());
    }

    // get calendar day elements mapped to their position within the calendar
    private LinkedHashMap<Integer, CalendarDayLayout> getCalendarDaysMap() {
        AtomicInteger index = new AtomicInteger();
        return calendarDays.stream().collect(LinkedHashMap::new, (map, val) -> map.put(index.incrementAndGet(), val), Map::putAll);
    }

    // get operational calendar day elements mapped to their position within the calendar
    private LinkedHashMap<Integer, CalendarDayLayout> getOperationalCalendarDaysMap() {
        return getCalendarDaysMap().entrySet().stream()
                .filter(e -> e.getValue().isEnabled())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    // get selected calendar day element paired with its position within the calendar
    private Pair<Integer, CalendarDayLayout> getSelectedCalendarDayPair() {
        OptionalInt optIndex = IntStream.range(0, calendarDays.size()).filter(i -> calendarDays.get(i).isSelected()).findFirst();
        if (optIndex.isPresent()) {
            int index = optIndex.getAsInt();
            return Pair.of(index + 1, calendarDays.get(index));
        }
        LOGGER.warn("No selected date found");
        return Pair.of(-1, null);
    }
}
