package org.minibus.aqa.main.domain.screens.schedule;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import io.qameta.allure.Step;
import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.helpers.ImageProcessor;
import org.minibus.aqa.main.domain.screens.BaseWidget;
import org.openqa.selenium.WebElement;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

@AndroidFindBy(id = "ll_date_container")
public class CalendarDayWidget extends BaseWidget {

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

    @Step("Click on the calendar day")
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

    public LocalDate getDate(final int positionInCalendar) {
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
        return Boolean.parseBoolean(btnDayOfMonth.getAttribute("checked"));
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(getWrappedElement().getAttribute("enabled"));
    }
}
