package org.minibus.aqa.test.ui;

import org.minibus.aqa.main.core.helpers.DateHelper;
import org.minibus.aqa.main.domain.api.models.RouteDTO;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.test.TestGroup;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.when;
import static org.minibus.aqa.main.domain.api.URI.ROUTES;


public class ScheduleCalendarTest extends BaseUiTest {

    @Test(groups = {TestGroup.UI},
            description = "When on the Schedule screen then the calendar has 7 days ahead")
    public void testWhenOnScheduleScreenThenCalendarIsWeekly() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        final int calendarCapacity = DayOfWeek.values().length;
        final List<LocalDate> calendarDates = DateHelper.getDaysFromNow(calendarCapacity);

        test.assertTrue(scheduleScreen.isOpened(), "'Schedule' screen is opened");
        test.assertEquals(scheduleScreen.getCalendar().getCapacity(), calendarCapacity, "the calendar has desired capacity");
        test.assertEquals(scheduleScreen.getCalendar().getDates(), calendarDates, "the calendar has date values in the correct order");
    }

    @Test(groups = {TestGroup.UI},
            description = "When on the Schedule screen then the calendar has 1 day selected by default")
    public void testWhenOnScheduleScreenThenCalendarHasFirstDaySelected() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        final int selectedDatePosition = 1;
        final int actualSelectedDatePosition = scheduleScreen.getCalendar().getSelectedDatePosition();

        test.assertTrue(scheduleScreen.isOpened(), "'Schedule' screen is opened");
        test.assertEquals(actualSelectedDatePosition, selectedDatePosition, "default selected calendar date position is correct");
    }

    @Test(groups = {TestGroup.UI},
            description = "When on the Schedule screen then the calendar has correct operational days")
    public void testWhenOnScheduleScreenThenCalendarHasOperationalDays() {
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.waitForLoading();

        final String departureCity = scheduleScreen.getDepartureCity();
        final String arrivalCity = scheduleScreen.getArrivalCity();

        List<RouteDTO> routes = when().get(ROUTES).then().extract().jsonPath().getList("$", RouteDTO.class);
        Optional<RouteDTO> opt = routes.stream()
                .filter(r -> r.getFrom().getName().equals(departureCity) && r.getTo().getName().equals(arrivalCity))
                .findFirst();

        if (opt.isPresent()) {
            final List<Integer> operationalDaysOfWeek = opt.get().getOpDays();
            final List<LocalDate> operationalDays = DateHelper.getDaysFromNow(DayOfWeek.values().length).stream()
                    .filter(d -> operationalDaysOfWeek.contains(d.getDayOfWeek().getValue()))
                    .collect(Collectors.toList());

            test.assertEquals(scheduleScreen.getCalendar().getOperationalDates(), operationalDays, "the calendar operational days match expected");
        } else {
            throw new RuntimeException(String.format("No route found: from='%s', to='%s'", departureCity, arrivalCity));
        }
    }
}
