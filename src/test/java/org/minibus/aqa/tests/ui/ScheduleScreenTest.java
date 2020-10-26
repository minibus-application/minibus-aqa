package org.minibus.aqa.tests.ui;

import org.joda.time.LocalDate;
import org.minibus.aqa.tests.BaseTest;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.domain.screens.schedule.SortType;
import org.testng.annotations.Test;


public class ScheduleScreenTest extends BaseTest {

    @Test(description = "Verify initial schedule screen")
    public void test() {
        ScheduleScreen scheduleScreen = new ScheduleScreen(driver);

        test.assertTrue(scheduleScreen.isOpened(), "Schedule screen is opened");
        test.assertEquals(scheduleScreen.getCalendar().getCalendarSize(), 7, "Calendar has desired capacity");
        test.assertEquals(scheduleScreen.getCalendar().getSelectedCalendarDatePosition(), 1, "Selected date position is correct");
        test.assertEquals(scheduleScreen.getCalendar().getSelectedDate().toString(), LocalDate.now().toString(), "Selected date is correct");
        test.assertTrue(scheduleScreen.isDepartureCitySelected(), "Departure city is selected");
        test.assertTrue(scheduleScreen.isArrivalCitySelected(), "Arrival city is selected");
        test.assertEquals(scheduleScreen.getTitle(), scheduleScreen.getName(), "Screen title is correct");

        if (scheduleScreen.isScheduleAvailable()) {
            test.assertEquals(scheduleScreen.getSelectedSortType(), SortType.DEPARTURE_TIME, "Sort type is correct");
        }
    }
}
