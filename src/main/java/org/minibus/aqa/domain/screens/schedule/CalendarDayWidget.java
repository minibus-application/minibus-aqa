package org.minibus.aqa.domain.screens.schedule;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.helpers.ImageProcessor;
import org.openqa.selenium.WebElement;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

@AndroidFindBy(id = "ll_date_container")
public class CalendarDayWidget extends Widget {

    @AndroidFindBy(id = "tv_day_of_week")
    private AndroidElement textDayOfWeek;

    @AndroidFindBy(id = "rb_month_day")
    private AndroidElement btnDayOfMonth;

    protected static final String DAY_OF_WEEK_PATTERN = "EE";
    protected static final String DAY_OF_MONTH_PATTERN = "d";
    protected static final String DATE_PATTERN = String.format("%s %s", DAY_OF_WEEK_PATTERN, DAY_OF_MONTH_PATTERN);

    protected CalendarDayWidget(WebElement element) {
        super(element);
    }

    public void select() {
        getWrappedElement().click();
    }

    public String getDayOfWeek() {
        return textDayOfWeek.getText();
    }

    public String getDayOfMonth() {
        return btnDayOfMonth.getText();
    }

    public String getDisplayDate() {
        return String.format("%s %s", getDayOfWeek(), getDayOfMonth()).trim();
    }

    public LocalDate getDate(int positionInCalendar) {
        String displayCalendarDate = getDisplayDate();

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
        return ImageProcessor.hasColor(btnDayOfMonth, ImageProcessor.ImageColor.BLUE);
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(getWrappedElement().getAttribute("enabled"));
    }

    protected static LocalDate toCalendarDate(LocalDate date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Constants.APP_LOCALE);
        String formatted = date.format(formatter);
        return LocalDate.parse(formatted, formatter);
    }
}
