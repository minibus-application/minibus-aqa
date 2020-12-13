package org.minibus.aqa.main.domain.screens.cities;

import io.qameta.allure.Step;
import org.minibus.aqa.main.core.env.Device;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.ButtonView;
import org.minibus.aqa.main.domain.screens.schedule.ScheduleScreen;
import org.minibus.aqa.main.domain.screens.BaseLoadableScreen;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CitiesScreen extends BaseLoadableScreen {

    @ViewInfo(name = "Close button",
            findBy = @FindBy(xpath = "//android.view.ViewGroup[contains(@resource-id, 'toolbar')]/android.widget.ImageButton[1]"))
    private ButtonView buttonClose;

    @ViewInfo(name = "City item", findBy = @FindBy(id = "ll_city_container"))
    private List<CityLayout> cities;

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

    @Step("Select '{cityName}' city")
    public ScheduleScreen selectCity(final String cityName) {
        getCityViewByName(cityName).tap();
        ScheduleScreen scheduleScreen = new ScheduleScreen();
        scheduleScreen.isOpened();
        return scheduleScreen;
    }

    public List<String> getCities() {
        return cities.stream().map(CityLayout::getCityName).collect(Collectors.toList());
    }

    public List<String> getRegions() {
        return cities.stream().map(CityLayout::getRegionName).collect(Collectors.toList());
    }

    private CityLayout getCityViewByName(final String cityName) {
        Optional<CityLayout> optCity = cities.stream().filter(c -> c.getCityName().equals(cityName)).findFirst();
        if (optCity.isPresent()) {
            return optCity.get();
        } else {
            throw new RuntimeException(cityName + " city is not found in the list");
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
