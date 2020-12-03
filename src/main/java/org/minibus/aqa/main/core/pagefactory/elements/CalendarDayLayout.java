package org.minibus.aqa.main.core.page_factory.elements;

import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.page_factory.locators.ViewFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

public class CalendarDayLayout extends AndroidLayout {

    @CacheLookup
    @ViewFindBy(name = "Day of week", findBy = @FindBy(id = "tv_day_of_week"))
    private AndroidView textDayOfWeek;

    @ViewFindBy(name = "Day of month", findBy = @FindBy(id = "rb_month_day"))
    private RadioButtonView btnDayOfMonth;

    protected static final String DAY_OF_WEEK_PATTERN = "EE";
    protected static final String DAY_OF_MONTH_PATTERN = "d";

    public CalendarDayLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public String getDayOfWeek() {
        return textDayOfWeek.getText();
    }

    public String getDayOfMonth() {
        return btnDayOfMonth.getText();
    }

    public String getDisplayedDate() {
        return String.format("%s %s", getDayOfWeek(), getDayOfMonth()).trim();
    }

    public LocalDate getDate(final int positionInCalendar) {
        String displayCalendarDate = getDisplayedDate();

        LocalDate currentDate = LocalDate.now();
        int calendarDayOfMonth = Integer.parseInt(getDayOfMonth());
        DayOfWeek calendarDayOfWeek = DayOfWeek.from(DateTimeFormatter.ofPattern(DAY_OF_WEEK_PATTERN).parse(getDayOfWeek()));

        LocalDate parsedDate = currentDate.plusDays(positionInCalendar - 1);

        if (parsedDate.getDayOfMonth() == calendarDayOfMonth && parsedDate.getDayOfWeek().equals(calendarDayOfWeek)) {
            return parsedDate;
        } else {
            String parsedDayOfWeekDisplayName = parsedDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Constants.APP_LOCALE);
            throw new DateTimeException(String.format("Calendar date on %d position is '%s', but the parsed one is '%s %s' (%s)",
                    positionInCalendar, displayCalendarDate, parsedDayOfWeekDisplayName, parsedDate.getDayOfMonth(), parsedDate.toString()));
        }
    }

    public boolean isSelected() {
        return btnDayOfMonth.isChecked();
    }
}
