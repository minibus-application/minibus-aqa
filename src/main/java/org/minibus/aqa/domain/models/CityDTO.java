package org.minibus.aqa.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("region")
    private String region;

    @JsonProperty("station")
    private String station;

    @JsonProperty("lat")
    private float lat;

    @JsonProperty("lng")
    private float lng;
}
