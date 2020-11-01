package org.minibus.aqa.tests.ui;

import org.assertj.core.api.Assertions;
import org.minibus.aqa.domain.api.models.RouteDTO;
import org.minibus.aqa.tests.BaseTest;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.when;


public class ScheduleScreenTest extends BaseTest {

    @Test(description = "When on the Schedule screen then the calendar has 7 days ahead")
    public void testWhenOnScheduleScreenThenCalendarIsWeekly() {
        ScheduleScreen scheduleScreen = new ScheduleScreen(driver);

        final int CALENDAR_CAPACITY = DayOfWeek.values().length;
        final List<LocalDate> calendarDates = getDaysFromNow(CALENDAR_CAPACITY);

        Assertions.assertThat(scheduleScreen.isOpened())
                .as("'Schedule screen' is opened").isTrue();
        Assertions.assertThat(scheduleScreen.getCalendar().getCapacity())
                .as("The calendar has desired capacity").isEqualTo(CALENDAR_CAPACITY);
        Assertions.assertThat(scheduleScreen.getCalendar().getDates())
                .as("The calendar has date values in the correct order").containsExactlyElementsOf(calendarDates);
    }

    @Test(description = "When on the Schedule screen then the calendar has 1 day selected by default")
    public void testWhenOnScheduleScreenThenCalendarHasFirstDaySelected() {
        ScheduleScreen scheduleScreen = new ScheduleScreen(driver);

        final int SELECTED_DATE_POSITION = DayOfWeek.values().length;

        Assertions.assertThat(scheduleScreen.isOpened())
                .as("'Schedule screen' is opened").isTrue();
        Assertions.assertThat(scheduleScreen.getCalendar().getCapacity())
                .as("Selected calendar date position is correct").isEqualTo(SELECTED_DATE_POSITION);
    }

    @Test(description = "When on the Schedule screen then the calendar has correct operational days")
    public void testWhenOnScheduleScreenThenCalendarHasOperationalDays() {
        ScheduleScreen scheduleScreen = new ScheduleScreen(driver);

        final String departureCity = scheduleScreen.getDepartureCity();
        final String arrivalCity = scheduleScreen.getArrivalCity();

        List<RouteDTO> routes = when().get("/routes").then().extract().jsonPath().getList(".", RouteDTO.class);
        Optional<RouteDTO> opt = routes.stream()
                .filter(r -> r.getFrom().getName().equals(departureCity) && r.getTo().getName().equals(arrivalCity))
                .findFirst();

        if (opt.isPresent()) {
            final List<Integer> operationalDaysOfWeek = opt.get().getOpDays();
            final List<LocalDate> operationalDays = getDaysFromNow(DayOfWeek.values().length).stream()
                    .filter(d -> operationalDaysOfWeek.contains(d.getDayOfWeek().getValue()))
                    .collect(Collectors.toList());

            Assertions.assertThat(scheduleScreen.getCalendar().getOperationalDays())
                    .as("The calendar operational days match expected").containsExactlyElementsOf(operationalDays);
        } else {
            throw new RuntimeException(String.format("No route found: departure=%s, arrival=%s", departureCity, arrivalCity));
        }
    }

    private List<LocalDate> getDaysFromNow(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> LocalDate.now().plus(i, ChronoUnit.DAYS).atStartOfDay().toLocalDate())
                .collect(Collectors.toList());
    }
}
