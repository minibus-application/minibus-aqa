package org.minibus.aqa.main.domain.screens.schedule;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.Pair;
import org.minibus.aqa.main.domain.screens.BaseWidget;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AndroidFindBy(id = "rv_calendar")
public class ScheduleCalendarWidget extends BaseWidget {

    public static final int DEFAULT_SELECTED_DATE_POSITION = 1;
    private List<CalendarDayWidget> calendarDays;

    protected ScheduleCalendarWidget(WebElement element) {
        super(element);
    }

    public int getCapacity() {
        return calendarDays.size();
    }

    @Step("Select {date} in the calendar")
    public void selectDay(final LocalDate date) {
        List<CalendarDayWidget> calendarDays = this.calendarDays;

        OptionalInt opt = IntStream.range(0, calendarDays.size())
                .filter(i -> date.equals(calendarDays.get(i).getDate(i + 1)))
                .findFirst();

        if (opt.isPresent()) {
            CalendarDayWidget calendarDay = calendarDays.get(opt.getAsInt());
            calendarDay.click();
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

    public int getSelectedDatesCount() {
        return (int) calendarDays.stream().filter(CalendarDayWidget::isSelected).count();
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
        OptionalInt optIndex = IntStream.range(0, calendarDays.size()).filter(i -> calendarDays.get(i).isSelected()).findFirst();
        if (optIndex.isPresent()) {
            int index = optIndex.getAsInt();
            return Pair.of(index + 1, calendarDays.get(index));
        }
        LOGGER.warn("No selected date found");
        return Pair.of(-1, null);
    }
}
