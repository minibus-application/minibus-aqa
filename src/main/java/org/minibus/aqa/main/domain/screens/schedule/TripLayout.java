package org.minibus.aqa.main.domain.screens.schedule;


import com.google.common.base.CharMatcher;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.main.core.pagefactory.annotations.ViewInfo;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidLayout;
import org.minibus.aqa.main.core.pagefactory.elements.AndroidView;
import org.minibus.aqa.main.core.pagefactory.elements.ButtonView;
import org.minibus.aqa.main.core.pagefactory.elements.TextView;
import org.minibus.aqa.main.domain.data.schedule.TripData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TripLayout extends AndroidLayout {

    @ViewInfo(name = "Carrier name", findBy = @FindBy(id = "tv_carrier"))
    private TextView textCarrier;

    @ViewInfo(name = "Carrier rating icon", findBy = @FindBy(xpath = "//android.widget.ImageView[./android.widget.TextView]"))
    private AndroidView iconRatingStart;

    @ViewInfo(name = "Carrier rating", findBy = @FindBy(id = "tv_carrier_rating"))
    private TextView textCarrierRating;

    @ViewInfo(name = "Departure time", findBy = @FindBy(id = "tv_dep_time"))
    private TextView textDepartureTime;

    @ViewInfo(name = "Duration", findBy = @FindBy(id = "tv_duration"))
    private TextView textDuration;

    @ViewInfo(name = "Arrival time", findBy = @FindBy(id = "tv_arr_time"))
    private TextView textArrivalTime;

    @ViewInfo(name = "Departure station", findBy = @FindBy(id = "tv_dep_station"))
    private TextView textDepartureStation;

    @ViewInfo(name = "Arrival station", findBy = @FindBy(id = "tv_arr_station"))
    private TextView textArrivalStation;

    @ViewInfo(name = "Available seats", findBy = @FindBy(id = "tv_available_seats"))
    private TextView textAvailableSeats;

    @ViewInfo(name = "Price", findBy = @FindBy(id = "tv_cost"))
    private TextView textPrice;

    @ViewInfo(name = "Select button", findBy = @FindBy(id = "btn_select"))
    private ButtonView buttonSelect;

    public TripLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public int getAvailableSeatsCount() {
        return Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(textAvailableSeats.getText()));
    }

    public boolean getPriceValue() {
        String priceString = StringUtils.substringBeforeLast(textPrice.getText(), "BYN").trim();
        return Boolean.parseBoolean(priceString);
    }

    public boolean getCarrierRating() {
        return Boolean.parseBoolean(textCarrierRating.getText());
    }
}
