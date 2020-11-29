package org.minibus.aqa.main.domain.data.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectionData {

    private String depFieldValue;
    private String arrFieldValue;

    public String getDepCity() {
        return getFieldParts(depFieldValue)[0];
    }

    public String getDepCityRegion() {
        return getFieldParts(depFieldValue)[1];
    }

    public String getArrCity() {
        return getFieldParts(arrFieldValue)[0];
    }

    public String getArrCityRegion() {
        return getFieldParts(arrFieldValue)[1];
    }

    public String getDirectionDescription() {
        return getDepCity() + " — " + getArrCity();
    }

    private String[] getFieldParts(String fieldValue) {
        return fieldValue.split(", ");
    }
}
