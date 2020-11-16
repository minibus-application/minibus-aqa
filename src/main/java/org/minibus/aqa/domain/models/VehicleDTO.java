package org.minibus.aqa.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("color")
    private String color;

    @JsonProperty("make")
    private String make;

    @JsonProperty("model")
    private String model;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("plateNum")
    private String plateNum;

    @JsonProperty("carrier")
    private CarrierDTO carrier;
}
