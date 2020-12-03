package org.minibus.aqa.main.core.page_factory.elements;

import org.minibus.aqa.main.core.page_factory.locators.ViewFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DirectionLayout extends AndroidLayout {

    @ViewFindBy(name = "Departure city field", findBy = @FindBy(id = "et_dep_city"))
    private AndroidView fieldDepartureCity;

    @ViewFindBy(name = "Arrival city field", findBy = @FindBy(id = "et_arr_city"))
    private AndroidView fieldArrivalCity;

    public DirectionLayout(WebElement wrappedElement, String logicalName, By by) {
        super(wrappedElement, logicalName, by);
    }

    public void openDepartureCities() {
        fieldDepartureCity.tap();
    }

    public void openArrivalCities() {
        fieldDepartureCity.tap();
    }
}
