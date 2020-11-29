package org.minibus.aqa.main.domain.screens.schedule;


import com.google.common.base.CharMatcher;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

@AndroidFindBy(id = "fl_trip")
public class TripWidget extends Widget {

    @AndroidFindBy(id = "tv_carrier")
    private AndroidElement textCarrier;

    @AndroidFindBy(xpath = "//android.widget.ImageView[./android.widget.TextView]")
    private AndroidElement iconRatingStart;

    @AndroidFindBy(id = "tv_carrier_rating")
    private AndroidElement textCarrierRating;

    @AndroidFindBy(id = "tv_dep_time")
    private AndroidElement textDepartureTime;

    @AndroidFindBy(id = "tv_duration")
    private AndroidElement textDuration;

    @AndroidFindBy(id = "tv_arr_time")
    private AndroidElement textArrivalTime;

    @AndroidFindBy(id = "tv_dep_station")
    private AndroidElement textDepartureStation;

    @AndroidFindBy(id = "tv_arr_station")
    private AndroidElement textArrivalStation;

    @AndroidFindBy(id = "tv_available_seats")
    private AndroidElement textAvailableSeats;

    @AndroidFindBy(id = "tv_cost")
    private AndroidElement textPrice;

    @AndroidFindBy(id = "btn_select")
    private AndroidElement btnSelect;

    protected TripWidget(WebElement element) {
        super(element);
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
