package org.minibus.aqa.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("cost")
    private String cost;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("fromTime")
    private String departureTime;

    @JsonProperty("toTime")
    private String arrivalTime;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("vehicle")
    private VehicleDTO vehicle;

    @JsonProperty("route")
    private RouteDTO route;
}
