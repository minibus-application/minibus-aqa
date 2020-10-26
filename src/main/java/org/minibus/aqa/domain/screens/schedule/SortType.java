package org.minibus.aqa.domain.screens.schedule;

public enum SortType {
    DEPARTURE_TIME("Departure time"),
    ARRIVAL_TIME("Arrival time"),
    CARRIER_RATING("Carrier rating"),
    AVAILABLE_SEATS("Available seats"),
    PRICE("Price");

    private final String type;

    SortType(String type) {
        this.type = type;
    }

    public static SortType fromString(String type) {
        for (SortType sortType : SortType.values()) {
            if (sortType.type.equalsIgnoreCase(type)) {
                return sortType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return type;
    }
}
