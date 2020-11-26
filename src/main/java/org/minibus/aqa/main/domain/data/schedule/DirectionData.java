package org.minibus.aqa.main.domain.data.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DirectionData {

    private String departureFieldValue;
    private String arrivalFieldValue;

    public String getDepartureCity() {
        return getFieldParts(departureFieldValue)[0];
    }

    public String getDepartureCityRegion() {
        return getFieldParts(departureFieldValue)[1];
    }

    public String getArrivalCity() {
        return getFieldParts(arrivalFieldValue)[0];
    }

    public String getArrivalCityRegion() {
        return getFieldParts(arrivalFieldValue)[1];
    }

    public String getDirectionDescription() {
        return getDepartureCity() + " — " + getArrivalCity();
    }

    private String[] getFieldParts(String fieldValue) {
        return fieldValue.split(", ");
    }
}
