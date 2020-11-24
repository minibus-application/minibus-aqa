package org.minibus.aqa.main.domain.screens.cities;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.main.domain.screens.BaseLoadableScreen;

import java.util.List;
import java.util.Optional;


public class CitiesScreen extends BaseLoadableScreen {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[contains(@resource-id, 'toolbar')]/android.widget.ImageButton[1]")
    private AndroidElement btnClose;

    private List<CityWidget> cities;

    private Type type;

    public CitiesScreen(Type type) {
        super(Device.getDriver(), type.toString());
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean isOpened(int timeoutSec) {
        return waitForLoading(timeoutSec);
    }

    @Step("Select city: '{cityName}'")
    public ScheduleScreen selectCity(final String cityName) {
        ScheduleScreen scheduleScreen = getCityByName(cityName).select();
        scheduleScreen.isOpened();
        return scheduleScreen;
    }

    private CityWidget getCityByName(final String cityName) {
        Optional<CityWidget> optCity = cities.stream().filter(c -> c.getCityName().equals(cityName)).findFirst();
        if (optCity.isPresent()) {
            return optCity.get();
        } else {
            throw new RuntimeException("City with " + cityName + " name not found");
        }
    }

    public enum Type {
        DEPARTURE("Departure"),
        ARRIVAL("Arrival");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
