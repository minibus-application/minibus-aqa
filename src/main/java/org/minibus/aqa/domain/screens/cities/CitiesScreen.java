package org.minibus.aqa.domain.screens.cities;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.minibus.aqa.core.env.Device;
import org.minibus.aqa.domain.screens.BaseLoadableScreen;
import org.minibus.aqa.domain.screens.schedule.ScheduleScreen;

import java.util.List;
import java.util.Optional;


public class CitiesScreen extends BaseLoadableScreen {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[contains(@resource-id, 'toolbar')]/android.widget.ImageButton[1]")
    private AndroidElement btnClose;

    private List<CityWidget> cities;

    private Type type;

    public CitiesScreen(Type type) {
        super(Device.getDriver(), type.name());
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean isOpened(int timeoutSec) {
        return waitForLoading(timeoutSec);
    }

    public ScheduleScreen selectCity(String cityName) {
        ScheduleScreen scheduleScreen = getCityByName(cityName).select();
        scheduleScreen.isOpened();
        return scheduleScreen;
    }

    private CityWidget getCityByName(String cityName) {
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
