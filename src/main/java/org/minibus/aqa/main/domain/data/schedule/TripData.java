package org.minibus.aqa.main.domain.data.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripData {

    private String carrier;
    private String carrierRating;
    private String depTime;
    private String arrTime;
    private String duration;
    private String depStation;
    private String arrStation;
    private String availableSeats;
    private String price;
}
